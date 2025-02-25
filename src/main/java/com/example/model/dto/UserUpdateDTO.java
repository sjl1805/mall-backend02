package com.example.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * 用户更新数据传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户更新DTO")
public class UserUpdateDTO {
    
    @Schema(description = "用户ID", required = true)
    private Long id;
    
    @Schema(description = "用户昵称", example = "张三")
    @Size(max = 30, message = "昵称不能超过30个字符")
    private String nickname;
    
    @Schema(description = "手机号码", example = "13812345678")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号码格式不正确")
    private String phone;
    
    @Schema(description = "电子邮箱", example = "example@mail.com")
    @Email(message = "邮箱格式不正确")
    private String email;
    
    @Schema(description = "头像URL", example = "https://example.com/avatar.jpg")
    private String avatar;
    
    @Schema(description = "年龄段", example = "18-24")
    private String ageRange;
    
    @Schema(description = "性别：0-未知 1-男 2-女", example = "1")
    private Integer gender;
    
    @Schema(description = "消费能力：1-低 2-中 3-高", example = "2")
    private Integer consumptionLevel;
    
    @Schema(description = "活跃度：1-低 2-中 3-高", example = "3")
    private Integer activityLevel;
    
    @Schema(description = "偏好分类JSON", example = "[1, 3, 5]")
    private String preferredCategories;
    
    @Schema(description = "用户标签JSON", example = "[\"运动\", \"科技\", \"阅读\"]")
    private String tags;
} 