package com.example.model.dto.users;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.*;

import java.util.Date;

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

    @Min(0) @Max(2)
    @Schema(description = "使用状态：0-未使用 1-已使用 2-已过期", defaultValue = "0", example = "0")
    private Integer status = 0;

    @Schema(description = "关联订单ID", example = "1001")
    private Long orderId;

    @Schema(description = "使用时间（自动更新）", example = "2023-08-15 14:30:00")
    private Date useTime;
} 