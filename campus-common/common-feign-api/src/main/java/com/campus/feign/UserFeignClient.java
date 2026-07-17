package com.campus.feign;

import com.campus.common.result.R;
import com.campus.feign.dto.UserVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 用户服务 Feign 客户端接口
 */
@FeignClient(name = "user-service", path = "/api/user")
public interface UserFeignClient {

    /**
     * 根据ID查询用户
     */
    @GetMapping("/{id}")
    R<UserVO> getById(@PathVariable("id") Long id);

    /**
     * 验证用户是否存在且状态正常
     */
    @GetMapping("/{id}/validate")
    R<Boolean> validateUser(@PathVariable("id") Long id);
}
