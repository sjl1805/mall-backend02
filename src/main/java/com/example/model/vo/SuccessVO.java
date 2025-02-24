package com.example.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class SuccessVO {
    @Schema(description = "token", example = "****")
    private String token;
    private Long id;

    @Schema(description = "用户名", example = "john_doe")
    private String username;

    @Schema(description = "用户昵称", example = "John")
    private String nickname;

    @Schema(description = "用户手机号", example = "13800138000")
    private String phone;

    @Schema(description = "用户邮箱", example = "example@example.com")
    private String email;

    @Schema(description = "用户头像URL", example = "http://example.com/avatar.jpg")
    private String avatar;

    @Schema(description = "用户性别：0未知 1男 2女", example = "1")
    private Integer gender;

    @Schema(description = "用户状态：0禁用 1启用", example = "1")
    private Integer status;

    @Schema(description = "用户角色", example = "0")
    private Integer role;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    // 性别转换方法
    public String getGender() {
        return switch (gender) {
            case 1 -> "男";
            case 2 -> "女";
            default -> "未知";
        };
    }
}
