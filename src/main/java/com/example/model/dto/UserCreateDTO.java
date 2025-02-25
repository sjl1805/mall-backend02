package com.example.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * 用户创建数据传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户创建DTO")
public class UserCreateDTO {
    
    @Schema(description = "用户名", required = true, example = "zhangsan")
    @NotBlank(message = "用户名不能为空")
    @Size(min = 4, max = 20, message = "用户名长度必须在4-20个字符之间")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "用户名只能包含字母、数字和下划线")
    private String username;
    
    @Schema(description = "密码", required = true, example = "password123")
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20个字符之间")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z0-9]).+$", message = "密码必须包含大小写字母或数字")
    private String password;
    
    @Schema(description = "用户昵称", example = "张三")
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
} 