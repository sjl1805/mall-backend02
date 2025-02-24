package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;
import org.hibernate.validator.constraints.Range;

/**
 * 用户优惠券表
 *
 * @TableName user_coupon
 */
@TableName(value = "user_coupon")
@Data
@Schema(description = "用户优惠券实体")
public class UserCoupon {
    /**
     * 用户优惠券ID
     */
    @TableId(type = IdType.AUTO)
    @Schema(description = "用户优惠券ID", example = "1")
    private Long id;

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    @Schema(description = "用户ID", example = "10001")
    private Long userId;

    /**
     * 优惠券ID
     */
    @NotNull(message = "优惠券ID不能为空")
    @Schema(description = "优惠券ID", example = "2001")
    private Long couponId;

    /**
     * 用户优惠券状态：0-未使用 1-已使用 2-已过期
     */
    @Range(min = 0, max = 2, message = "优惠券状态参数不合法")
    @Schema(description = "状态 0-未使用 1-已使用 2-已过期", example = "0")
    private Integer status = 0;

    /**
     * 使用的订单ID
     */
    @Schema(description = "关联订单ID", example = "30001")
    private Long orderId;

    /**
     * 领取时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "领取时间", example = "2024-03-18 10:00:00")
    private LocalDateTime getTime;

    /**
     * 使用时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "使用时间", example = "2024-03-18 14:30:00")
    private LocalDateTime useTime;

    /**
     * 创建时间（带时区）
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间", example = "2024-03-18 10:00:00")
    private LocalDateTime createTime;

    /**
     * 更新时间（带时区）
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间", example = "2024-03-18 10:05:00")
    private LocalDateTime updateTime;
}