package com.campus.order.feign.fallback;

import com.campus.common.result.R;
import com.campus.feign.UserFeignClient;
import com.campus.feign.dto.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 用户服务熔断降级工厂
 */
@Slf4j
@Component
public class UserFeignFallbackFactory implements FallbackFactory<UserFeignClient> {

    @Override
    public UserFeignClient create(Throwable cause) {
        log.error("用户服务调用失败，触发降级: {}", cause.getMessage());
        return new UserFeignClient() {
            @Override
            public R<UserVO> getById(Long id) {
                return R.fail(503, "用户服务暂不可用");
            }

            @Override
            public R<Boolean> validateUser(Long id) {
                return R.fail(503, "用户验证服务暂不可用");
            }
        };
    }
}
