package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品SKU表
 *
 * @TableName product_sku
 */
@TableName(value = "product_sku")
@Data
@Schema(description = "商品SKU实体")
public class ProductSku implements Serializable {
    /**
     * SKU ID
     */
    @TableId(type = IdType.AUTO)
    @Schema(description = "SKU ID", example = "1")
    private Long id;

    /**
     * 商品ID
     */
    @Schema(description = "商品ID", example = "100")
    private Long productId;

    /**
     * 规格值组合（JSON）
     */
    @Schema(description = "规格值组合（JSON）", example = "{\"颜色\":\"黑色\",\"内存\":\"256GB\"}")
    private String specValues;

    /**
     * 价格
     */
    @Schema(description = "价格", example = "7999.00")
    private BigDecimal price;

    /**
     * 库存
     */
    @Schema(description = "库存", example = "50")
    private Integer stock;

    /**
     * 销量
     */
    @Schema(description = "销量", example = "120")
    private Integer sales;

    /**
     * SKU主图
     */
    @Schema(description = "SKU主图", example = "http://example.com/images/iphone14-black.jpg")
    private String mainImage;

    /**
     * SKU状态：0-下架 1-上架
     */
    @Schema(description = "SKU状态：0-下架 1-上架", example = "1")
    private Integer status;

    /**
     * 创建时间（带时区）
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间", example = "2023-01-01T12:00:00")
    private LocalDateTime createTime;

    /**
     * 更新时间（带时区）
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间", example = "2023-01-01T12:30:00")
    private LocalDateTime updateTime;
}