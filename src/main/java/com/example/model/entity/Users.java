package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Range;

/**
 * 用户表
 *
 * @TableName users
 */
@TableName(value = "users")
@Data
@Schema(description = "用户实体")
public class Users {
    /**
     * 用户ID
     */
    @TableId(type = IdType.AUTO)
    @Schema(description = "用户ID", example = "1")
    private Long id;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Size(min = 4, max = 20, message = "用户名长度4-20个字符")
    @Schema(description = "用户名", example = "testuser")
    private String username;

    /**
     * BCrypt加密后的密码哈希值
     */
    @JsonIgnore // 序列化时忽略密码字段
    @Schema(description = "加密后的密码", accessMode = Schema.AccessMode.READ_ONLY)
    private String password;

    /**
     * 昵称
     */
    @Size(max = 20, message = "昵称最长20个字符")
    @Schema(description = "用户昵称", example = "测试用户")
    private String nickname;

    /**
     * 手机号
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @Schema(description = "手机号", example = "13800138000")
    private String phone;

    /**
     * 邮箱
     */
    @Email(message = "邮箱格式不正确")
    @Schema(description = "邮箱地址", example = "test@example.com")
    private String email;

    /**
     * 头像
     */
    @Schema(description = "头像URL", example = "/images/avatar.jpg")
    private String avatar = "/images/backend.png";

    /**
     * 性别：0未知 1男 2女
     */
    @Range(min = 0, max = 2, message = "性别参数不合法")
    @Schema(description = "性别 0-未知 1-男 2-女", example = "0")
    private Integer gender = 0;

    /**
     * 状态：0禁用 1启用
     */
    @Range(min = 0, max = 1, message = "状态参数不合法")
    @Schema(description = "用户状态 0-禁用 1-启用", example = "1")
    private Integer status = 1;

    /**
     * 角色：0-超级管理员 1-普通用户 2-员工 9-系统管理员
     */
    @Range(min = 0, max = 9, message = "角色参数不合法")
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "用户角色 0-超级管理员 1-用户 2-员工 9-管理员", example = "1")
    private Integer role = 1;

    /**
     * 创建时间（带时区）
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间", example = "2024-01-01 00:00:00")
    private LocalDateTime createTime;

    /**
     * 更新时间（带时区）
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间", example = "2024-01-01 00:00:00")
    private LocalDateTime updateTime;
}