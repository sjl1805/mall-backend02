package com.example.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserCouponDTO {
    private Long id;

    @NotNull(message = "用户ID不能为空")
    @Min(value = 1, message = "用户ID不合法")
    @Schema(description = "用户ID", example = "1")
    private Long userId;

    @NotNull(message = "优惠券ID不能为空")
    @Min(value = 1, message = "优惠券ID不合法")
    @Schema(description = "优惠券ID", example = "1")
    private Long couponId;

    @NotNull(message = "状态不能为空")
    @Min(value = 0, message = "状态值不合法")
    @Max(value = 2, message = "状态值不合法")
    @Schema(description = "优惠券状态：0未使用 1已使用 2已过期", example = "0")
    private Integer status;

    @Min(value = 0, message = "订单ID不合法")
    @Schema(description = "使用的订单ID", example = "1")
    private Long orderId;
} 