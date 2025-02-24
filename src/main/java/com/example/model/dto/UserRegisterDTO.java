package com.example.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 用户注册数据传输对象
 */
@Data
public class UserRegisterDTO {
    @NotBlank(message = "用户名不能为空")
    @Size(min = 4, max = 16, message = "用户名长度4-16位")
    @Schema(description = "用户名", example = "john_doe")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度6-20位")
    @Schema(description = "用户密码", example = "password123")
    private String password;

    @NotBlank(message = "确认密码不能为空")
    @Schema(description = "确认用户密码", example = "password123")
    private String confirmPassword;

    @NotBlank(message = "昵称不能为空")
    @Size(max = 20, message = "昵称最长20个字符")
    @Schema(description = "用户昵称", example = "John")
    private String nickname;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @Schema(description = "用户手机号", example = "13800138000")
    private String phone;

    @Email(message = "邮箱格式不正确")
    @Schema(description = "用户邮箱", example = "example@example.com")
    private String email;

    @Schema(description = "用户角色", example = "1")
    private Integer role;
} 