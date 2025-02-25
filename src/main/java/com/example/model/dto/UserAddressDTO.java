package com.example.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * 用户地址数据传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户地址DTO")
public class UserAddressDTO {
    
    @Schema(description = "地址ID")
    private Long id;
    
    @Schema(description = "用户ID")
    private Long userId;
    
    @Schema(description = "默认地址：0-否 1-是")
    private Integer isDefault;
    
    @Schema(description = "收货人姓名", required = true, example = "张三")
    @NotBlank(message = "收货人姓名不能为空")
    @Size(max = 20, message = "收货人姓名不能超过20个字符")
    private String receiverName;
    
    @Schema(description = "联系电话", required = true, example = "13812345678")
    @NotBlank(message = "联系电话不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号码格式不正确")
    private String receiverPhone;
    
    @Schema(description = "省份", required = true, example = "广东省")
    @NotBlank(message = "省份不能为空")
    @Size(max = 20, message = "省份不能超过20个字符")
    private String province;
    
    @Schema(description = "城市", required = true, example = "深圳市")
    @NotBlank(message = "城市不能为空")
    @Size(max = 20, message = "城市不能超过20个字符")
    private String city;
    
    @Schema(description = "区/县", required = true, example = "南山区")
    @NotBlank(message = "区/县不能为空")
    @Size(max = 20, message = "区/县不能超过20个字符")
    private String district;
    
    @Schema(description = "详细地址", required = true, example = "科技园路1号")
    @NotBlank(message = "详细地址不能为空")
    @Size(max = 100, message = "详细地址不能超过100个字符")
    private String detailAddress;
    
    @Schema(description = "完整地址（自动生成）", example = "广东省深圳市南山区科技园路1号")
    public String getFullAddress() {
        return province + city + district + detailAddress;
    }
} 