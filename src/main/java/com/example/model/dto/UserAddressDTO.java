package com.example.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserAddressDTO {
    private Long id;

    @NotNull(message = "用户ID不能为空")
    @Min(value = 1, message = "用户ID不合法")
    @Schema(description = "用户ID", example = "1")
    private Long userId;

    @NotBlank(message = "收货人姓名不能为空")
    @Size(max = 20, message = "姓名最长20个字符")
    @Schema(description = "收货人姓名", example = "张三")
    private String receiverName;

    @NotBlank(message = "联系电话不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @Schema(description = "收货人电话", example = "13800138000")
    private String receiverPhone;

    @NotBlank(message = "省份不能为空")
    @Size(max = 20, message = "省份名称过长")
    @Schema(description = "省份", example = "广东省")
    private String province;

    @NotBlank(message = "城市不能为空")
    @Size(max = 20, message = "城市名称过长")
    @Schema(description = "城市", example = "广州市")
    private String city;

    @NotBlank(message = "区县不能为空")
    @Size(max = 20, message = "区县名称过长")
    @Schema(description = "区县", example = "天河区")
    private String district;

    @NotBlank(message = "详细地址不能为空")
    @Size(max = 100, message = "地址最长100个字符")
    @Schema(description = "详细地址", example = "天汇大厦")
    private String detailAddress;

    @NotNull(message = "默认状态不能为空")
    @Min(value = 0, message = "状态值不合法")
    @Max(value = 1, message = "状态值不合法")
    @Schema(description = "默认状态：0非默认 1默认", example = "1")
    private Integer isDefault;
} 