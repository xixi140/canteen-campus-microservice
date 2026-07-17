package com.campus.order.feign.fallback;

import com.campus.common.result.R;
import com.campus.feign.MerchantFeignClient;
import com.campus.feign.dto.MerchantDishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 商户服务熔断降级工厂
 */
@Slf4j
@Component
public class MerchantFeignFallbackFactory implements FallbackFactory<MerchantFeignClient> {

    @Override
    public MerchantFeignClient create(Throwable cause) {
        log.error("商户服务调用失败，触发降级: {}", cause.getMessage());
        return new MerchantFeignClient() {
            @Override
            public R<MerchantDishVO> getDishById(Long id) {
                log.warn("【降级】查询商户菜品失败, dishId={}", id);
                return R.fail(503, "商户服务暂不可用，请稍后重试");
            }

            @Override
            public R<Void> deductStock(Long id, Integer quantity) {
                log.error("【降级】扣减商户库存失败, dishId={}", id);
                return R.fail(503, "库存服务异常，下单失败");
            }

            @Override
            public R<Boolean> checkStock(Long id, Integer quantity) {
                return R.fail(503, "库存检查服务暂不可用");
            }
        };
    }
}
