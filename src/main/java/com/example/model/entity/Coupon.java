package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 优惠券表
 * @TableName coupon
 */
@TableName(value ="coupon")
@Data
public class Coupon {
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
     * 使用门槛
     */
    private BigDecimal minAmount;

    /**
     * 生效时间
     */
    private Date startTime;

    /**
     * 失效时间
     */
    private Date endTime;

    /**
     * 状态：0-失效 1-生效
     */
    private Integer status;

    /**
     * 创建时间（带时区）
     */
    private Date createTime;

    /**
     * 更新时间（带时区）
     */
    private Date updateTime;
}