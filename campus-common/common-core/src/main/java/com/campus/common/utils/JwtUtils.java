package com.campus.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

/**
 * JWT 工具类 (JJWT 0.12.x API)
 * 用于生成和解析 JWT Token
 */
public class JwtUtils {

    // HMAC-SHA256 密钥（至少256位=32字节）
    private static final String SECRET_STRING = "CampusCanteenCloud2026SecretKeyForJWT!!";
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(
            SECRET_STRING.getBytes(StandardCharsets.UTF_8));
    private static final long EXPIRE = 7 * 24 * 60 * 60 * 1000L; // 7天

    /**
     * 生成 JWT Token
     */
    public static String generateToken(Long userId, String username, String role) {
        return Jwts.builder()
                .subject(username)
                .claim("userId", userId)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRE))
                .signWith(SECRET_KEY)
                .compact();
    }

    /**
     * 解析 JWT Token
     */
    public static Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 从 Token 中获取用户ID
     */
    public static Long getUserId(String token) {
        Claims claims = parseToken(token);
        return claims.get("userId", Long.class);
    }

    /**
     * 从 Token 中获取角色
     */
    public static String getRole(String token) {
        Claims claims = parseToken(token);
        return claims.get("role", String.class);
    }

    /**
     * 验证 Token 是否过期
     */
    public static boolean isExpired(String token) {
        try {
            Claims claims = parseToken(token);
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }
}
