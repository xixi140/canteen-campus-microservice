package com.campus.order.feign.fallback;

import com.campus.common.result.R;
import com.campus.feign.CanteenFeignClient;
import com.campus.feign.dto.CanteenDishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 食堂服务熔断降级工厂
 *
 * 当 canteen-service 不可用时触发降级：
 * - 返回友好提示而非直接报错
 * - 记录日志用于排查
 * - 保证核心流程不会因为下游服务故障而崩溃
 */
@Slf4j
@Component
public class CanteenFeignFallbackFactory implements FallbackFactory<CanteenFeignClient> {

    @Override
    public CanteenFeignClient create(Throwable cause) {
        log.error("食堂服务调用失败，触发降级: {}", cause.getMessage());
        return new CanteenFeignClient() {
            @Override
            public R<CanteenDishVO> getDishById(Long id) {
                log.warn("【降级】查询食堂菜品失败, dishId={}", id);
                return R.fail(503, "食堂服务暂不可用，请稍后重试");
            }

            @Override
            public R<Void> deductStock(Long id, Integer quantity) {
                log.error("【降级】扣减食堂库存失败, dishId={}, quantity={}", id, quantity);
                return R.fail(503, "库存服务异常，下单失败");
            }

            @Override
            public R<Boolean> checkStock(Long id, Integer quantity) {
                return R.fail(503, "库存检查服务暂不可用");
            }
        };
    }
}
