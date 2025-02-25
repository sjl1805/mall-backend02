package com.example.common.validator;

import org.springframework.stereotype.Component;

/**
 * 用户参数验证工具类
 * <p>
 * 提供用户相关参数的验证方法
 */
@Component
public class UserValidator {

    /**
     * 验证用户名
     *
     * @param username 用户名
     * @return 是否有效
     */
    public boolean isValidUsername(String username) {
        return username != null && username.matches("^[a-zA-Z0-9_]{4,16}$");
    }

    /**
     * 验证密码
     *
     * @param password 密码
     * @return 是否有效
     */
    public boolean isValidPassword(String password) {
        return password != null && password.matches("^[a-zA-Z0-9_!@#$%^&*]{6,20}$");
    }

    /**
     * 验证手机号
     *
     * @param phone 手机号
     * @return 是否有效
     */
    public boolean isValidPhone(String phone) {
        return phone != null && phone.matches("^1[3-9]\\d{9}$");
    }

    /**
     * 验证邮箱
     *
     * @param email 邮箱
     * @return 是否有效
     */
    public boolean isValidEmail(String email) {
        return email != null && email.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
    }

    /**
     * 验证验证码
     *
     * @param verifyCode 验证码
     * @return 是否有效
     */
    public boolean isValidVerifyCode(String verifyCode) {
        return verifyCode != null && verifyCode.matches("^\\d{6}$");
    }
} 