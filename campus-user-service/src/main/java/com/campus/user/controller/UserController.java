package com.campus.user.controller;

import com.campus.common.result.R;
import com.campus.feign.dto.UserVO;
import com.campus.user.entity.User;
import com.campus.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 用户服务 Controller
 *
 * 同时实现 Feign 接口契约 —— 供订单服务远程调用
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // ========== 对外 REST API ==========

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public R<Map<String, String>> register(@RequestBody User user) {
        return R.ok(userService.register(user));
    }

    /**
     * 用户登录 —— 返回 JWT Token
     */
    @PostMapping("/login")
    public R<Map<String, String>> login(@RequestParam String username,
                                        @RequestParam String password) {
        return R.ok(userService.login(username, password));
    }

    // ========== Feign 内部调用接口 ==========

    /**
     * 根据ID查询用户（供 order-service 通过 Feign 调用）
     */
    @GetMapping("/{id}")
    public R<UserVO> getById(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user == null) {
            return R.fail("用户不存在");
        }
        UserVO vo = new UserVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setNickname(user.getNickname());
        vo.setPhone(user.getPhone());
        vo.setRole(user.getRole());
        return R.ok(vo);
    }

    /**
     * 验证用户是否存在且状态正常
     */
    @GetMapping("/{id}/validate")
    public R<Boolean> validateUser(@PathVariable Long id) {
        User user = userService.getById(id);
        return R.ok(user != null && user.getDeleted() == 0);
    }
}
