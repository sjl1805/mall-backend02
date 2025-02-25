package com.example.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AvatarUpdateDTO {
    @NotBlank(message = "头像URL不能为空")
    private String avatarUrl;
} 