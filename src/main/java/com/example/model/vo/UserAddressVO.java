package com.example.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserAddressVO {
    private Long id;

    @Schema(description = "用户ID", example = "1")
    private Long userId;

    @Schema(description = "收货人姓名", example = "张三")
    private String receiverName;

    @Schema(description = "收货人电话", example = "13800138000")
    private String receiverPhone;

    @Schema(description = "省份", example = "广东省")
    private String province;

    @Schema(description = "城市", example = "广州市")
    private String city;

    @Schema(description = "区县", example = "天河区")
    private String district;

    @Schema(description = "详细地址", example = "天汇大厦")
    private String detailAddress;

    @Schema(description = "默认状态：0非默认 1默认", example = "1")
    private Integer isDefault;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    // 组合完整地址
    public String getFullAddress() {
        return String.format("%s %s %s %s", province, city, district, detailAddress);
    }

    // 默认状态转换
    public String getIsDefault() {
        return "1".equals(isDefault) ? "是" : "否";
    }
} 