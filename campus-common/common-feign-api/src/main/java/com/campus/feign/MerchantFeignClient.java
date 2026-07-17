package com.campus.feign;

import com.campus.common.result.R;
import com.campus.feign.dto.MerchantDishVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 商户服务 Feign 客户端接口
 */
@FeignClient(name = "merchant-service", path = "/api/merchant")
public interface MerchantFeignClient {

    /**
     * 根据ID查询商户菜品
     */
    @GetMapping("/dishes/{id}")
    R<MerchantDishVO> getDishById(@PathVariable("id") Long id);

    /**
     * 扣减菜品库存
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
