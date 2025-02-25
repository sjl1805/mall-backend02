package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("users")
public class User implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 用户ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 密码
     */
    @JsonIgnore
    private String password;
    
    /**
     * 昵称
     */
    private String nickname;
    
    /**
     * 手机号
     */
    private String phone;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 头像
     */
    private String avatar;
    
    /**
     * 角色：0管理员 1普通用户 2商家
     */
    private Integer role;
    
    /**
     * 状态：0禁用 1启用
     */
    private Integer status;
    
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
     * 偏好分类JSON
     */
    private String preferredCategories;
    
    /**
     * 用户标签JSON
     */
    private String tags;
    
    /**
     * 最后活跃时间
     */
    private LocalDateTime lastActiveTime;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
} 