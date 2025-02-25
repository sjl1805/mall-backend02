package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 优惠券实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("coupon")
public class Coupon implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
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
    private LocalDateTime startTime;
    
    /**
     * 结束时间
     */
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
    private String categoryLimit;
    
    /**
     * 商品限制JSON
     */
    private String productLimit;
    
    /**
     * 状态：0-禁用 1-启用
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
} 