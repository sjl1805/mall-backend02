package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户表
 *
 * @TableName users
 */
@TableName(value = "users")
@Data
@Schema(description = "用户实体")
public class Users implements Serializable {
    /**
     * 用户ID
     */
    @TableId(type = IdType.AUTO)
    @Schema(description = "用户ID", example = "1")
    private Long id;

    /**
     * 用户名
     */
    @Schema(description = "用户名", example = "zhangsan")
    private String username;

    /**
     * BCrypt加密后的密码哈希值
     */
    @Schema(description = "BCrypt加密后的密码哈希值", example = "$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt/3CLZ7D9T/OiyznTuXk7s/loS")
    private String password;

    /**
     * 昵称
     */
    @Schema(description = "昵称", example = "张三")
    private String nickname;

    /**
     * 手机号
     */
    @Schema(description = "手机号", example = "13800138000")
    private String phone;

    /**
     * 邮箱
     */
    @Schema(description = "邮箱", example = "zhangsan@example.com")
    private String email;

    /**
     * 头像
     */
    @Schema(description = "头像URL", example = "http://example.com/avatars/zhangsan.jpg")
    private String avatar;

    /**
     * 性别：0未知 1男 2女
     */
    @Schema(description = "性别：0未知 1男 2女", example = "1")
    private Integer gender;

    /**
     * 状态：0禁用 1启用
     */
    @Schema(description = "状态：0禁用 1启用", example = "1")
    private Integer status;

    /**
     * 用户角色
     */
    @Schema(description = "用户角色：1-普通用户 2-VIP用户 9-管理员", example = "1")
    private Integer role;

    /**
     * 创建时间（带时区）
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间", example = "2023-01-01T12:00:00")
    private LocalDateTime createTime;

    /**
     * 更新时间（带时区）
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间", example = "2023-01-01T12:30:00")
    private LocalDateTime updateTime;
}