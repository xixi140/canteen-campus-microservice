package com.campus.user.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.common.exception.BizException;
import com.campus.common.utils.JwtUtils;
import com.campus.user.entity.User;
import com.campus.user.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    /**
     * 用户注册
     */
    public Map<String, String> register(User user) {
        // 校验用户名是否已存在
        long count = count(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, user.getUsername()));
        if (count > 0) {
            throw new BizException("用户名已存在");
        }
        // 密码 MD5 加密
        user.setPassword(DigestUtils.md5DigestAsHex(
                user.getPassword().getBytes(StandardCharsets.UTF_8)));
        user.setRole("STUDENT"); // 默认学生角色
        save(user);
        return Map.of("message", "注册成功");
    }

    /**
     * 用户登录 —— 返回 JWT Token
     */
    public Map<String, String> login(String username, String password) {
        String encryptedPassword = DigestUtils.md5DigestAsHex(
                password.getBytes(StandardCharsets.UTF_8));
        User user = getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username)
                .eq(User::getPassword, encryptedPassword));
        if (user == null) {
            throw new BizException("用户名或密码错误");
        }
        // 签发 JWT Token
        String token = JwtUtils.generateToken(user.getId(), user.getUsername(), user.getRole());
        Map<String, String> result = new HashMap<>();
        result.put("token", token);
        result.put("userId", String.valueOf(user.getId()));
        result.put("nickname", user.getNickname());
        result.put("role", user.getRole());
        return result;
    }
}
