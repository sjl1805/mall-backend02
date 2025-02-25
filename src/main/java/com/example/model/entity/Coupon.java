package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 优惠券表
 * 该实体类代表商城的优惠券数据，用于促销活动和用户激励
 * 支持满减券和折扣券两种类型，可设置使用门槛和有效期
 * 包含优惠券的基本信息及状态
 *
 * @TableName coupon
 */
@TableName(value = "coupon")
@Data
@Schema(description = "优惠券实体")
public class Coupon implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 优惠券ID
     */
    @TableId(type = IdType.AUTO)
    @Schema(description = "优惠券ID", example = "1")
    private Long id;
    /**
     * 优惠券名称
     */
    @Schema(description = "优惠券名称", example = "新人专享券")
    private String name;
    /**
     * 优惠券类型：1-满减券 2-折扣券
     */
    @Schema(description = "优惠券类型：1-满减券 2-折扣券", example = "1")
    private Integer type;
    /**
     * 优惠券面值
     */
    @Schema(description = "优惠券面值", example = "50.00")
    private BigDecimal value;
    /**
     * 优惠券数量
     */
    @Schema(description = "优惠券数量", example = "100")
    private Integer num;
    /**
     * 使用门槛
     */
    @Schema(description = "使用门槛（最低消费金额）", example = "200.00")
    private BigDecimal minAmount;
    /**
     * 生效时间
     */
    @Schema(description = "生效时间", example = "2023-01-01T00:00:00")
    private LocalDateTime startTime;
    /**
     * 失效时间
     */
    @Schema(description = "失效时间", example = "2023-12-31T23:59:59")
    private LocalDateTime endTime;
    /**
     * 状态：0-失效 1-生效
     */
    @Schema(description = "状态：0-失效 1-生效", example = "1")
    private Integer status;
    /**
     * 创建时间（带时区）
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间", example = "2023-01-01T12:00:00")
    private LocalDateTime createTime;
    /**
     * 更新时间（带时区）
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间", example = "2023-01-01T12:30:00")
    private LocalDateTime updateTime;
    /**
     * 非数据库字段，优惠券剩余有效期（天）
     */
    @TableField(exist = false)
    @Schema(description = "优惠券剩余有效期（天）", example = "30")
    private Integer remainDays;

    /**
     * 非数据库字段，优惠券已领取数量
     */
    @TableField(exist = false)
    @Schema(description = "优惠券已领取数量", example = "50")
    private Integer receivedCount;

    /**
     * 非数据库字段，优惠券已使用数量
     */
    @TableField(exist = false)
    @Schema(description = "优惠券已使用数量", example = "20")
    private Integer usedCount;

    /**
     * 非数据库字段，领取状态（前端使用）：0-未领取 1-已领取 2-已使用
     */
    @TableField(exist = false)
    @Schema(description = "领取状态：0-未领取 1-已领取 2-已使用", example = "1")
    private Integer receiveStatus;
}