package com.example.model.dto;

import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import java.util.Map;

/**
 * 用户信息更新数据传输对象
 */
@Data
public class UserUpdateDTO {

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 手机号
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    /**
     * 邮箱
     */
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 年龄段
     */
    private String ageRange;

    /**
     * 性别：0-未知 1-男 2-女
     */
    private Integer gender;

    /**
     * 消费能力：1-低 2-中 3-高
     */
    private Integer consumptionLevel;

    /**
     * 活跃度：1-低 2-中 3-高
     */
    private Integer activityLevel;

    /**
     * 偏好分类列表
     */
    private List<Long> preferredCategories;

    /**
     * 用户标签列表
     */
    private List<Map<String, Object>> tags;
} 