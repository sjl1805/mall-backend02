package com.example.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户优惠券数据传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCouponDTO {
    
    private Long id;
    private Long userId;
    private CouponDTO coupon; // 优惠券详情
    private Integer status; // 状态：0-未使用 1-已使用 2-已过期
    private String statusDesc; // 状态描述
    private Long orderId; // 使用订单ID
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime getTime; // 获取时间
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime useTime; // 使用时间
    
    private Boolean canUse; // 当前是否可用
} 