package com.example.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 优惠券查询数据传输对象
 */
@Data
public class CouponQueryDTO {

    /**
     * 关键词
     */
    private String keyword;

    /**
     * 优惠券类型
     */
    private Integer type;

    /**
     * 优惠券状态
     */
    private Integer status;

    /**
     * 最小金额
     */
    private BigDecimal minAmount;

    /**
     * 最大金额
     */
    private BigDecimal maxAmount;

    /**
     * 最小门槛
     */
    private BigDecimal minThreshold;

    /**
     * 最大门槛
     */
    private BigDecimal maxThreshold;

    /**
     * 开始日期
     */
    private LocalDateTime startDate;

    /**
     * 结束日期
     */
    private LocalDateTime endDate;

    /**
     * 是否有库存
     */
    private Boolean hasStock;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序方式：asc/desc
     */
    private String sortOrder;

    /**
     * 页码
     */
    private Integer pageNum = 1;

    /**
     * 每页记录数
     */
    private Integer pageSize = 10;
} 