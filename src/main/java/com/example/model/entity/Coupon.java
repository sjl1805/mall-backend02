package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 优惠券表
 *
 * @TableName coupon
 */
@Schema(description = "优惠券实体")
@TableName(value = "coupon")
@Data
public class Coupon implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 优惠券ID
     */
    @Schema(description = "优惠券ID", example = "5001")
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 优惠券名称
     */
    @Schema(description = "优惠券名称", example = "新人满减券")
    private String name;
    /**
     * 优惠券类型：1-满减券 2-折扣券
     */
    @Schema(description = "类型：1-满减 2-折扣", example = "1")
    private Integer type;
    /**
     * 优惠券面值
     */
    @Schema(description = "面值（满减金额/折扣率）", example = "200.00")
    private BigDecimal value;
    /**
     * 使用门槛
     */
    @Schema(description = "最低使用金额", example = "1000.00")
    private BigDecimal minAmount;
    /**
     * 生效时间
     */
    @Schema(description = "生效时间", example = "2025-01-01T00:00:00")
    private LocalDateTime startTime;
    /**
     * 失效时间
     */
    @Schema(description = "失效时间", example = "2025-12-31T23:59:59")
    private LocalDateTime endTime;
    /**
     * 状态：0-失效 1-生效
     */
    @Schema(description = "状态：0-失效 1-生效", example = "1")
    private Integer status;
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