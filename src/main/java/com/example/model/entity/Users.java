package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户实体类
 *
 * @TableName users
 */
@Data
@TableName("users")
public class Users implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 用户ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 用户名（唯一）
     */
    private String username;
    /**
     * BCrypt加密后的密码哈希值
     */
    private String password;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 手机号（唯一）
     */
    private String phone;
    /**
     * 邮箱（唯一）
     */
    private String email;
    /**
     * 头像地址
     */
    private String avatar = "/images/default-avatar.png";
    /**
     * 性别：0-未知 1-男 2-女
     */
    private Integer gender = 0;
    /**
     * 状态：0-禁用 1-启用
     */
    private Integer status = 1;
    /**
     * 角色：0-游客 1-普通用户 2-商户  9-管理员
     */
    private Integer role = 2;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}