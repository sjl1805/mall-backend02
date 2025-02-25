package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

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
public class Coupon implements Serializable {
    /**
     * 优惠券ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 优惠券名称
     */
    private String name;

    /**
     * 优惠券类型：1-满减券 2-折扣券
     */
    private Integer type;

    /**
     * 优惠券面值
     */
    private BigDecimal value;

    /**
     * 优惠券数量
     */
    private Integer num;

    /**
     * 使用门槛
     */
    private BigDecimal minAmount;

    /**
     * 生效时间
     */
    private LocalDateTime startTime;

    /**
     * 失效时间
     */
    private LocalDateTime endTime;

    /**
     * 状态：0-失效 1-生效
     */
    private Integer status;

    /**
     * 创建时间（带时区）
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间（带时区）
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    
    /**
     * 非数据库字段，优惠券剩余有效期（天）
     */
    @TableField(exist = false)
    private Integer remainDays;
    
    /**
     * 非数据库字段，优惠券已领取数量
     */
    @TableField(exist = false)
    private Integer receivedCount;
    
    /**
     * 非数据库字段，优惠券已使用数量
     */
    @TableField(exist = false)
    private Integer usedCount;
    
    /**
     * 非数据库字段，领取状态（前端使用）：0-未领取 1-已领取 2-已使用
     */
    @TableField(exist = false)
    private Integer receiveStatus;
}