package com.example.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户数据传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户信息DTO")
public class UserDTO {
    
    @Schema(description = "用户ID")
    private Long id;
    
    @Schema(description = "用户名")
    private String username;
    
    @Schema(description = "用户昵称")
    private String nickname;
    
    @Schema(description = "手机号码")
    private String phone;
    
    @Schema(description = "电子邮箱")
    private String email;
    
    @Schema(description = "头像URL")
    private String avatar;
    
    @Schema(description = "角色：0-管理员 1-普通用户 2-商家")
    private Integer role;
    
    @Schema(description = "账号状态：0-禁用 1-启用")
    private Integer status;
    
    @Schema(description = "年龄段，如：'18-24'")
    private String ageRange;
    
    @Schema(description = "性别：0-未知 1-男 2-女")
    private Integer gender;
    
    @Schema(description = "消费能力：1-低 2-中 3-高")
    private Integer consumptionLevel;
    
    @Schema(description = "活跃度：1-低 2-中 3-高")
    private Integer activityLevel;
    
    @Schema(description = "最后活跃时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastActiveTime;
    
    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
} 