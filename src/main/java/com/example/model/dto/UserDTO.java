package com.example.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;

    @NotBlank(message = "用户名不能为空")
    @Size(min = 4, max = 16, message = "用户名长度4-16位")
    @Schema(description = "用户名", example = "john_doe")
    private String username;

    @Size(min = 6, max = 20, message = "密码长度6-20位")
    @Schema(description = "用户密码", example = "password123")
    private String password;

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

    private String avatar;

    @Min(value = 0, message = "性别参数错误")
    @Max(value = 2, message = "性别参数错误")
    @Schema(description = "用户性别：0未知 1男 2女", example = "1")
    private Integer gender;

    @Min(value = 0, message = "状态参数错误")
    @Max(value = 1, message = "状态参数错误")
    @Schema(description = "用户状态：0禁用 1启用", example = "1")
    private Integer status;

    @Min(value = 0, message = "角色参数错误")
    @Max(value = 1, message = "角色参数错误")
    @Schema(description = "用户角色", example = "0")
    private Integer role;
} 