package com.example.model.dto.users;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import com.example.model.entity.Users;

@Data
@Schema(description = "用户信息传输对象")
public class UserDTO {
    @Schema(description = "用户ID（更新时必填）")
    private Long id;

    @NotBlank(message = "用户名不能为空")
    @Size(min = 4, max = 20, message = "用户名长度4-20位")
    @Schema(description = "用户名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @Size(min = 6, max = 20, message = "密码长度6-20位")
    @Schema(description = "密码（仅新增时必填）")
    private String password;

    @Size(max = 64, message = "昵称最长64位")
    @Schema(description = "昵称")
    private String nickname;

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @Schema(description = "手机号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String phone;

    @Email(message = "邮箱格式不正确")
    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "头像地址")
    private String avatar;

    @Min(value = 0, message = "性别参数错误")
    @Max(value = 2, message = "性别参数错误")
    @Schema(description = "性别：0-未知 1-男 2-女", defaultValue = "0")
    private Integer gender = 0;

    @Min(value = 0, message = "状态参数错误")
    @Max(value = 1, message = "状态参数错误")
    @Schema(description = "状态：0-禁用 1-启用", defaultValue = "1")
    private Integer status = 1;

    @Min(value = 0, message = "角色参数错误")
    @Max(value = 9, message = "角色参数错误")
    @Schema(description = "角色：0-游客 1-普通用户 2-商家 9管理员",
            allowableValues = {"0", "1", "2", "9"},
            defaultValue = "1")
    private Integer role = 1;

    public static UserDTO fromEntity(Users user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setNickname(user.getNickname());
        dto.setPhone(user.getPhone());
        dto.setEmail(user.getEmail());
        dto.setAvatar(user.getAvatar());
        dto.setGender(user.getGender());
        dto.setStatus(user.getStatus());
        dto.setRole(user.getRole());
        return dto;
    }

    public Users toEntity() {
        Users user = new Users();
        user.setId(this.id);
        user.setUsername(this.username);
        user.setNickname(this.nickname);
        user.setPhone(this.phone);
        user.setEmail(this.email);
        user.setAvatar(this.avatar);
        user.setGender(this.gender);
        user.setStatus(this.status);
        user.setRole(this.role);
        return user;
    }
} 