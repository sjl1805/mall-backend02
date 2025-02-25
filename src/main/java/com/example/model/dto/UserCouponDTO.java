package com.example.model.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

/**
 * 用户领取优惠券数据传输对象
 */
@Data
public class UserCouponDTO {

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /**
     * 优惠券ID
     */
    @NotNull(message = "优惠券ID不能为空")
    private Long couponId;
} 