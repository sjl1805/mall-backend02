package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 优惠券实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "coupon", autoResultMap = true)
public class Coupon implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 优惠券名称
     */
    private String name;
    
    /**
     * 优惠券类型：1-满减券 2-折扣券 3-无门槛券
     */
    private Integer type;
    
    /**
     * 优惠金额
     */
    private BigDecimal amount;
    
    /**
     * 使用门槛金额
     */
    private BigDecimal minAmount;
    
    /**
     * 折扣率（折扣券专用）
     */
    private BigDecimal discount;
    
    /**
     * 发行数量（null表示不限量）
     */
    private Integer quantity;
    
    /**
     * 每人限领数量（null表示不限制）
     */
    private Integer userLimit;
    
    /**
     * 开始时间
     */
    private LocalDateTime startTime;
    
    /**
     * 结束时间
     */
    private LocalDateTime endTime;
    
    /**
     * 使用说明
     */
    private String description;
    
    /**
     * 状态：0-未启用 1-已启用 2-已结束
     */
    private Integer status;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Long> categoryLimit;
    
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Long> productLimit;
} 