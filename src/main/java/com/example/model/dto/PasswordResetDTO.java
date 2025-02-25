package com.example.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PasswordResetDTO {
    @NotBlank(message = "新密码不能为空")
    private String newPassword;
} 