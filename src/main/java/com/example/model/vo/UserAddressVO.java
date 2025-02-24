package com.example.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserAddressVO {
    private Long id;
    private Long userId;
    private String isDefault;
    private String receiverName;
    private String receiverPhone;
    private String province;
    private String city;
    private String district;
    private String detailAddress;
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