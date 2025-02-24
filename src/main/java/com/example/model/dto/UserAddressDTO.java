package com.example.model.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserAddressDTO {
    private Long id;

    @NotNull(message = "用户ID不能为空")
    @Min(value = 1, message = "用户ID不合法")
    private Long userId;

    @NotBlank(message = "收货人姓名不能为空")
    @Size(max = 20, message = "姓名最长20个字符")
    private String receiverName;

    @NotBlank(message = "联系电话不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String receiverPhone;

    @NotBlank(message = "省份不能为空")
    @Size(max = 20, message = "省份名称过长")
    private String province;

    @NotBlank(message = "城市不能为空")
    @Size(max = 20, message = "城市名称过长")
    private String city;

    @NotBlank(message = "区县不能为空")
    @Size(max = 20, message = "区县名称过长")
    private String district;

    @NotBlank(message = "详细地址不能为空")
    @Size(max = 100, message = "地址最长100个字符")
    private String detailAddress;

    @NotNull(message = "默认状态不能为空")
    @Min(value = 0, message = "状态值不合法")
    @Max(value = 1, message = "状态值不合法")
    private Integer isDefault;
} 