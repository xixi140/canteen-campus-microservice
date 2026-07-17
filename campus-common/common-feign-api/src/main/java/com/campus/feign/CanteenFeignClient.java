package com.campus.feign;

import com.campus.common.result.R;
import com.campus.feign.dto.CanteenDishVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 食堂服务 Feign 客户端接口
 *
 * 设计原则：
 * - 接口定义在 common-feign-api 中，作为服务契约
 * - 提供方(canteen-service)实现该接口
 * - 消费方(order-service)引入该模块即可调用
 */
@FeignClient(name = "canteen-service", path = "/api/canteen")
public interface CanteenFeignClient {

    /**
     * 根据ID查询食堂菜品
     */
    @GetMapping("/dishes/{id}")
    R<CanteenDishVO> getDishById(@PathVariable("id") Long id);

    /**
     * 扣减菜品库存（下单时调用）
     * 幂等性：使用 requestId 防止重复扣减
     */
    @PutMapping("/dishes/{id}/stock/deduct")
    R<Void> deductStock(@PathVariable("id") Long id,
                        @RequestParam("quantity") Integer quantity);

    /**
     * 查询菜品库存是否充足
     */
    @GetMapping("/dishes/{id}/stock/check")
    R<Boolean> checkStock(@PathVariable("id") Long id,
                          @RequestParam("quantity") Integer quantity);
}
