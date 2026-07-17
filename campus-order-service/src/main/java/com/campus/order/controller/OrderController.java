package com.campus.order.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.common.result.R;
import com.campus.order.entity.Order;
import com.campus.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 订单服务 Controller —— 下单入口
 *
 * 调用链路：
 * 前端 → Gateway(:8080) → order-service(:8084)
 *                          ├→ user-service(:8081)
 *                          ├→ canteen-service(:8082) 或 merchant-service(:8083)
 *                          └→ payment-service(:8085)
 */
@Slf4j
@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String NOTICE_URL = "http://localhost:8086/api/notice/create";

    @PostMapping("/create")
    public R<Map<String, Object>> createOrder(@RequestBody Map<String, Object> params) {
        if (!params.containsKey("requestId") || params.get("requestId") == null) {
            params.put("requestId", UUID.randomUUID().toString());
        }
        log.info("收到下单请求: userId={}, dishId={}", params.get("userId"), params.get("dishId"));
        Map<String, Object> result = orderService.createOrder(params);
        if (result.containsKey("orderId") && !result.containsKey("message")) {
            sendNotice(Long.valueOf(params.get("userId").toString()), "订单通知", "您的订单已创建成功，订单号：" + result.get("orderId"), "ORDER");
        }
        return R.ok(result);
    }

    @GetMapping("/{id}")
    public R<Order> getOrder(@PathVariable Long id) {
        Order order = orderService.getOrderDetail(id);
        return order == null ? R.fail("订单不存在") : R.ok(order);
    }

    @GetMapping("/user/{userId}")
    public R<List<Order>> listUserOrders(@PathVariable Long userId) {
        return R.ok(orderService.lambdaQuery().eq(Order::getUserId, userId).orderByDesc(Order::getCreateTime).list());
    }

    @GetMapping("/list")
    public R<List<Order>> listOrders(@RequestParam(required = false) String status) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<Order>().orderByDesc(Order::getCreateTime);
        if (status != null && !status.isEmpty()) wrapper.eq(Order::getStatus, status);
        return R.ok(orderService.list(wrapper));
    }

    @PutMapping("/{id}/cancel")
    public R<Order> cancelOrder(@PathVariable Long id) {
        Order order = orderService.getOrderDetail(id);
        if (order == null) return R.fail("订单不存在");
        if (!"PENDING_PAYMENT".equals(order.getStatus())) return R.fail("仅待支付订单可取消");
        order.setStatus("CANCELLED");
        orderService.updateById(order);
        sendNotice(order.getUserId(), "订单取消", "您的订单 #" + id + " 已取消", "ORDER");
        return R.ok(order);
    }

    @PutMapping("/{id}/pickup")
    public R<Order> confirmPickup(@PathVariable Long id) {
        Order order = orderService.getOrderDetail(id);
        if (order == null) return R.fail("订单不存在");
        if ("COMPLETED".equals(order.getStatus()) || "PAID".equals(order.getStatus()) || "PREPARING".equals(order.getStatus())) {
            order.setStatus("COMPLETED");
            orderService.updateById(order);
            sendNotice(order.getUserId(), "取餐提醒", "您的订单 #" + id + " 已完成，请到店取餐", "ORDER");
            return R.ok(order);
        }
        return R.fail("当前订单状态不允许取餐");
    }

    private void sendNotice(Long userId, String title, String content, String type) {
        try {
            Map<String, Object> body = Map.of("userId", userId, "title", title, "content", content, "type", type);
            restTemplate.postForEntity(NOTICE_URL, new HttpEntity<>(body), String.class);
        } catch (Exception e) {
            log.warn("发送通知失败: {}", e.getMessage());
        }
    }
}
