package com.campus.order.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.common.exception.BizException;
import com.campus.common.result.R;
import com.campus.feign.*;
import com.campus.feign.dto.*;
import com.campus.order.entity.Order;
import com.campus.order.mapper.OrderMapper;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

/**
 * 订单服务 —— 微服务核心业务逻辑
 *
 * 下单流程（跨4个服务调用）：
 * 1. 幂等性校验 (requestId)
 * 2. 验证用户 (user-service)
 * 3. 查询菜品信息 + 扣减库存 (canteen-service 或 merchant-service)
 * 4. 创建订单记录 (order_db)
 * 5. 创建支付单 (payment-service)
 * 6. 返回完整订单
 *
 * 高内聚低耦合体现：
 * - 订单服务只关心订单流程编排，不关心菜品/用户/支付的具体实现
 * - 通过 Feign 接口契约调用，各个服务独立演进
 * - Sentinel 保障故障隔离，一个服务挂了不影响订单查询
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService extends ServiceImpl<OrderMapper, Order> {

    private final UserFeignClient userFeignClient;
    private final CanteenFeignClient canteenFeignClient;
    private final MerchantFeignClient merchantFeignClient;
    private final PaymentFeignClient paymentFeignClient;

    /**
     * 创建订单 —— 核心跨服务业务流程
     *
     * @param params {userId, sourceType, dishId, quantity, requestId}
     */
    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional(name = "create-order", rollbackFor = Exception.class)
    @SentinelResource(value = "createOrder", fallback = "createOrderFallback")
    public Map<String, Object> createOrder(Map<String, Object> params) {
        Long userId = Long.valueOf(params.get("userId").toString());
        String sourceType = params.get("sourceType").toString();
        Long dishId = Long.valueOf(params.get("dishId").toString());
        Integer quantity = Integer.valueOf(params.get("quantity").toString());
        String requestId = params.get("requestId").toString();

        // ====== 步骤1：幂等性校验（防止重复下单） ======
        Order existing = baseMapper.findByRequestId(requestId);
        if (existing != null) {
            log.info("【幂等性】重复请求被拦截, requestId={}, orderId={}", requestId, existing.getId());
            return Map.of("orderId", existing.getId(), "status", existing.getStatus(),
                    "message", "订单已存在（幂等性保护）");
        }

        // ====== 步骤2：验证用户身份 ======
        R<UserVO> userResult = userFeignClient.getById(userId);
        if (!userResult.isSuccess() || userResult.getData() == null) {
            log.error("用户验证失败, userId={}", userId);
            throw new BizException("用户不存在或状态异常");
        }
        UserVO user = userResult.getData();
        log.info("用户验证通过: userId={}, nickname={}", user.getId(), user.getNickname());

        // ====== 步骤3：查询菜品 + 扣减库存 ======
        String dishName;
        BigDecimal unitPrice;

        if ("CANTEEN".equals(sourceType)) {
            // 3a. 食堂菜品 → 调用 canteen-service
            R<CanteenDishVO> dishResult = canteenFeignClient.getDishById(dishId);
            if (!dishResult.isSuccess() || dishResult.getData() == null) {
                throw new BizException("食堂菜品不存在");
            }
            CanteenDishVO dish = dishResult.getData();
            dishName = dish.getName();
            unitPrice = dish.getPrice();

            // 扣减库存
            R<Void> stockResult = canteenFeignClient.deductStock(dishId, quantity);
            if (!stockResult.isSuccess()) {
                throw new BizException(stockResult.getMessage());
            }
            log.info("食堂菜品扣减库存成功: dishId={}, name={}, quantity={}", dishId, dishName, quantity);

        } else if ("MERCHANT".equals(sourceType)) {
            // 3b. 商户菜品 → 调用 merchant-service
            R<MerchantDishVO> dishResult = merchantFeignClient.getDishById(dishId);
            if (!dishResult.isSuccess() || dishResult.getData() == null) {
                throw new BizException("商户菜品不存在");
            }
            MerchantDishVO dish = dishResult.getData();
            dishName = dish.getName();
            unitPrice = dish.getPrice();

            // 扣减库存
            R<Void> stockResult = merchantFeignClient.deductStock(dishId, quantity);
            if (!stockResult.isSuccess()) {
                throw new BizException(stockResult.getMessage());
            }
            log.info("商户菜品扣减库存成功: dishId={}, name={}, quantity={}", dishId, dishName, quantity);

        } else {
            throw new BizException("不支持的菜品来源类型: " + sourceType);
        }

        // ====== 步骤4：创建订单 ======
        Order order = new Order();
        order.setRequestId(requestId);
        order.setUserId(userId);
        order.setSourceType(sourceType);
        order.setDishId(dishId);
        order.setDishName(dishName);
        order.setQuantity(quantity);
        order.setUnitPrice(unitPrice);
        order.setTotalAmount(unitPrice.multiply(new BigDecimal(quantity)));
        order.setStatus("PENDING_PAYMENT");
        save(order);
        log.info("订单创建成功: orderId={}, amount={}", order.getId(), order.getTotalAmount());

        // ====== 步骤5：创建支付单 ======
        R<PaymentVO> payResult = paymentFeignClient.create(
                order.getId(), order.getTotalAmount(), requestId);
        if (payResult.isSuccess() && payResult.getData() != null) {
            order.setPaymentId(payResult.getData().getId());
            order.setStatus("PAID");
            updateById(order);
            log.info("支付单创建成功: paymentId={}, orderId={}", payResult.getData().getId(), order.getId());
        } else {
            log.warn("支付单创建失败，订单保持待支付状态: orderId={}", order.getId());
        }

        // ====== 步骤6：返回结果 ======
        return Map.of(
                "orderId", order.getId(),
                "dishName", dishName,
                "quantity", quantity,
                "totalAmount", order.getTotalAmount(),
                "status", order.getStatus(),
                "paymentId", order.getPaymentId() != null ? order.getPaymentId() : 0L
        );
    }

    /**
     * 查询订单详情
     */
    public Order getOrderDetail(Long orderId) {
        return getById(orderId);
    }
}
