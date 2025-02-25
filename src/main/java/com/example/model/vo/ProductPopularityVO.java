package com.example.model.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 商品热度VO类
 */
@Data
public class ProductPopularityVO {
    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 行为数量
     */
    private Integer count;

    /**
     * 热度得分
     */
    private BigDecimal score;
} 