package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 优惠券实体类
 */
@Data
@TableName(value = "coupon", autoResultMap = true)
public class Coupon {
    
    /**
     * 优惠券ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 优惠券名称
     */
    private String name;
    
    /**
     * 类型：1-满减 2-折扣 3-无门槛
     */
    private Integer type;
    
    /**
     * 优惠金额/折扣率
     */
    private BigDecimal amount;
    
    /**
     * 使用门槛金额
     */
    private BigDecimal threshold;
    
    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    
    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    
    /**
     * 总数量
     */
    private Integer total;
    
    /**
     * 剩余数量
     */
    private Integer remain;
    
    /**
     * 分类限制JSON
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Long> categoryLimit;
    
    /**
     * 商品限制JSON
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Long> productLimit;
    
    /**
     * 状态：0-禁用 1-启用
     */
    private Integer status;
    
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    
    /**
     * 优惠券折扣金额
     */
    private BigDecimal discount;
} 