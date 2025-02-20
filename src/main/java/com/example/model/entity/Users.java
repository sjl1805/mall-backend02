package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户实体类
 *
 * @TableName users
 */
@Schema(description = "用户实体")
@Data
@TableName("users")
public class Users implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 用户ID
     */
    @Schema(description = "用户ID", example = "10001")
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 用户名（唯一）
     */
    @Schema(description = "用户名", example = "john_doe")
    private String username;
    /**
     * BCrypt加密后的密码哈希值
     */
    @Schema(description = "加密后的密码", example = "$2a$10$k6n948le92cuts2LWj4mrubyCqe4XaS3Yi8K6EiiBmAuMIy5m/B5zq")
    private String password;
    /**
     * 用户昵称
     */
    @Schema(description = "用户昵称", example = "张三")
    private String nickname;
    /**
     * 手机号（唯一）
     */
    @Schema(description = "手机号码", example = "13800000001")
    private String phone;
    /**
     * 邮箱（唯一）
     */
    @Schema(description = "电子邮箱", example = "user@example.com")
    private String email;
    /**
     * 头像地址
     */
    @Schema(description = "头像URL", example = "/images/avatars/1.jpg")
    private String avatar = "/images/default-avatar.png";
    /**
     * 性别：0-未知 1-男 2-女
     */
    @Schema(description = "性别：0-未知 1-男 2-女", example = "1")
    private Integer gender = 0;
    /**
     * 状态：0-禁用 1-启用
     */
    @Schema(description = "账户状态：0-禁用 1-启用", example = "1")
    private Integer status = 1;
    /**
     * 角色：0-游客 1-普通用户 2-商户  9-管理员
     */
    @Schema(description = "用户角色：0-游客 1-用户 2-商户 9-管理员", example = "1")
    private Integer role = 2;
    /**
     * 创建时间
     */
    @Schema(description = "注册时间", example = "2023-08-01T10:15:30")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @Schema(description = "最后更新时间", example = "2023-08-01T10:15:30")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}