package com.example.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 用户登录数据传输对象
 */
@Data
public class UserLoginDTO {
    @NotBlank(message = "用户名不能为空")
    @Schema(description = "用户名", example = "admin")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Schema(description = "用户密码", example = "123456")
    private String password;

    @Schema(description = "验证码", example = "123456")
    private String captcha;

    @Schema(description = "验证码ID", example = "captchaId")
    private String captchaId;
} 