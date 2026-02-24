package com.campus.core.util;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.JWTValidator;
import com.campus.core.constant.SystemConstants;

import java.util.Date;
import java.util.Map;

/**
 * JWT 工具类
 */
public class JwtUtil {

    private static final byte[] KEY = SystemConstants.JWT_SECRET.getBytes();

    /**
     * 创建 Token
     */
    public static String createToken(Map<String, Object> payload) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + SystemConstants.JWT_EXPIRATION);

        return JWTUtil.createToken(payload, KEY)
                .setExpiresAt(expiration)
                .sign();
    }

    /**
     * 验证 Token
     */
    public static boolean verify(String token) {
        try {
            JWT jwt = JWTUtil.parseToken(token);
            JWTValidator.of(jwt).validateDate(new Date());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 从 Token 中获取内容
     */
    public static Map<String, Object> getPayload(String token) {
        JWT jwt = JWTUtil.parseToken(token);
        return jwt.getPayloads();
    }

    /**
     * 获取用户 ID
     */
    public static Long getUserId(String token) {
        Map<String, Object> payload = getPayload(token);
        Object userId = payload.get("userId");
        return userId != null ? Long.valueOf(userId.toString()) : null;
    }

    /**
     * 获取用户类型
     */
    public static String getUserType(String token) {
        Map<String, Object> payload = getPayload(token);
        return (String) payload.get("userType");
    }
}
