package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户优惠券表
 *
 * @TableName user_coupon
 */
@Schema(description = "用户优惠券实体")
@TableName(value = "user_coupon")
@Data
public class UserCoupon implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 用户优惠券ID
     */
    @Schema(description = "用户优惠券ID", example = "100001")
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 用户ID
     */
    @Schema(description = "用户ID", example = "10001")
    private Long userId;
    /**
     * 优惠券ID
     */
    @Schema(description = "优惠券ID", example = "5001")
    private Long couponId;
    /**
     * 用户优惠券状态：0-未使用 1-已使用 2-已过期
     */
    @Schema(description = "使用状态：0-未用 1-已用 2-过期", example = "0")
    private Integer status;
    /**
     * 使用的订单ID
     */
    @Schema(description = "使用订单ID", example = "null")
    private Long orderId;
    /**
     * 领取时间
     */
    @Schema(description = "领取时间", example = "2023-08-01T10:15:30")
    private LocalDateTime getTime;
    /**
     * 使用时间
     */
    @Schema(description = "使用时间", example = "null")
    private LocalDateTime useTime;
    /**
     * 创建时间（带时区）
     */
    @Schema(description = "创建时间", example = "2023-08-01T10:15:30")
    private LocalDateTime createTime;
    /**
     * 更新时间（带时区）
     */
    @Schema(description = "更新时间", example = "2023-08-01T10:15:30")
    private LocalDateTime updateTime;
}