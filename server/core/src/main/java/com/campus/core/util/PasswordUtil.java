package com.campus.core.util;

import cn.hutool.crypto.digest.BCrypt;
import com.campus.core.constant.SystemConstants;

/**
 * 密码工具类
 */
public class PasswordUtil {

    /**
     * 加密密码
     */
    public static String encode(String rawPassword) {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt(SystemConstants.BCRYPT_strength));
    }

    /**
     * 验证密码
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        try {
            return BCrypt.checkpw(rawPassword, encodedPassword);
        } catch (Exception e) {
            return false;
        }
    }
}
