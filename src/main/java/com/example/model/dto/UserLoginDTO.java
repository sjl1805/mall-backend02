package com.example.model.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * 用户登录数据传输对象
 */
@Data
public class UserLoginDTO {

    /**
     * 用户名/手机号/邮箱
     */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;
} 