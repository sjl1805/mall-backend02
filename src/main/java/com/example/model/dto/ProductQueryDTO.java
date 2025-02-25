package com.example.model.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 商品查询数据传输对象
 */
@Data
public class ProductQueryDTO {

    /**
     * 关键词
     */
    private String keyword;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 标签ID
     */
    private Long tagId;

    /**
     * 商品状态：0-下架 1-上架
     */
    private Integer status;

    /**
     * 最低价格
     */
    private BigDecimal minPrice;

    /**
     * 最高价格
     */
    private BigDecimal maxPrice;

    /**
     * 最低库存
     */
    private Integer minStock;

    /**
     * 最高库存
     */
    private Integer maxStock;

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