package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品SKU表
 *
 * @TableName product_sku
 */
@TableName(value = "product_sku")
@Data
public class ProductSku {
    /**
     * SKU ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 规格值组合（JSON）
     */
    private String specValues;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 库存
     */
    private Integer stock;

    /**
     * 销量
     */
    private Integer sales;

    /**
     * SKU主图
     */
    private String mainImage;

    /**
     * SKU状态：0-下架 1-上架
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