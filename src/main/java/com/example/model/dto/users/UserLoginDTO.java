package com.example.model.dto.users;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
@Schema(description = "用户登录参数")
public class UserLoginDTO {
    @NotBlank(message = "账号不能为空")
    @Schema(description = "用户名/手机号", example = "admin", requiredMode = Schema.RequiredMode.REQUIRED)
    private String account;

    @NotBlank(message = "密码不能为空")
    @Schema(description = "密码", example = "123456", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

} 