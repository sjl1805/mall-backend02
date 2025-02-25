package com.example.model.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * 用户收货地址数据传输对象
 */
@Data
public class UserAddressDTO {

    /**
     * 地址ID
     */
    private Long id;

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /**
     * 默认地址：0-否 1-是
     */
    private Integer isDefault;

    /**
     * 收货人
     */
    @NotBlank(message = "收货人不能为空")
    @Size(max = 20, message = "收货人姓名长度不能超过20个字符")
    private String receiverName;

    /**
     * 联系电话
     */
    @NotBlank(message = "联系电话不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String receiverPhone;

    /**
     * 省份
     */
    @NotBlank(message = "省份不能为空")
    private String province;

    /**
     * 城市
     */
    @NotBlank(message = "城市不能为空")
    private String city;

    /**
     * 区/县
     */
    @NotBlank(message = "区/县不能为空")
    private String district;

    /**
     * 详细地址
     */
    @NotBlank(message = "详细地址不能为空")
    @Size(max = 100, message = "详细地址长度不能超过100个字符")
    private String detailAddress;
    
    /**
     * 完整地址（省市区+详细地址，非数据库字段）
     */
    private String fullAddress;
} 