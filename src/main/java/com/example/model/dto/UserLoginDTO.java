package com.example.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserLoginDTO {
    @NotBlank(message = "用户名不能为空")
    @Schema(description = "用户名", example = "john_doe")
    private String username;
    
    @NotBlank(message = "密码不能为空")
    @Schema(description = "用户密码", example = "password123")
    private String password;

    @Schema(description = "验证码", example = "123456")
    private String captcha;

    @Schema(description = "验证码ID", example = "captchaId")
    private String captchaId;
} 