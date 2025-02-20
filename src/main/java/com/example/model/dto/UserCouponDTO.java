package com.example.model.dto;

import com.example.model.entity.UserCoupon;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "用户优惠券数据传输对象")
public class UserCouponDTO {
    @Schema(description = "用户优惠券ID（更新时必填）", example = "456")
    private Long id;

    @NotNull(message = "用户ID不能为空")
    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "123")
    private Long userId;

    @NotNull(message = "优惠券ID不能为空")
    @Schema(description = "优惠券ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "789")
    private Long couponId;

    @Min(0)
    @Max(2)
    @Schema(description = "使用状态：0-未使用 1-已使用 2-已过期", defaultValue = "0", example = "0")
    private Integer status = 0;

    @Schema(description = "关联订单ID", example = "1001")
    private Long orderId;

    @Schema(description = "使用时间（自动更新）", example = "2023-08-15 14:30:00")
    private LocalDateTime useTime;

    public static UserCouponDTO fromEntity(UserCoupon userCoupon) {
        UserCouponDTO dto = new UserCouponDTO();
        dto.setId(userCoupon.getId());
        dto.setUserId(userCoupon.getUserId());
        dto.setCouponId(userCoupon.getCouponId());
        dto.setStatus(userCoupon.getStatus());
        dto.setOrderId(userCoupon.getOrderId());
        dto.setUseTime(userCoupon.getUseTime());
        return dto;
    }

    public UserCoupon toEntity() {
        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setId(this.id);
        userCoupon.setUserId(this.userId);
        userCoupon.setCouponId(this.couponId);
        userCoupon.setStatus(this.status);
        userCoupon.setOrderId(this.orderId);
        userCoupon.setUseTime(this.useTime);
        return userCoupon;
    }
} 