package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户优惠券表
 *
 * @TableName user_coupon
 */
@TableName(value = "user_coupon")
@Data
@Schema(description = "用户优惠券实体")
public class UserCoupon implements Serializable {
    /**
     * 用户优惠券ID
     */
    @TableId(type = IdType.AUTO)
    @Schema(description = "用户优惠券ID", example = "1")
    private Long id;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID", example = "1001")
    private Long userId;

    /**
     * 优惠券ID
     */
    @Schema(description = "优惠券ID", example = "2001")
    private Long couponId;

    /**
     * 用户优惠券状态：0-未使用 1-已使用 2-已过期
     */
    @Schema(description = "用户优惠券状态：0-未使用 1-已使用 2-已过期", example = "0")
    private Integer status;

    /**
     * 使用的订单ID
     */
    @Schema(description = "使用的订单ID", example = "3001")
    private Long orderId;

    /**
     * 领取时间
     */
    @Schema(description = "领取时间", example = "2023-01-01T10:00:00")
    private LocalDateTime getTime;

    /**
     * 使用时间
     */
    @Schema(description = "使用时间", example = "2023-01-10T15:30:00")
    private LocalDateTime useTime;

    /**
     * 创建时间（带时区）
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间", example = "2023-01-01T10:00:00")
    private LocalDateTime createTime;

    /**
     * 更新时间（带时区）
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间", example = "2023-01-10T15:30:00")
    private LocalDateTime updateTime;
}