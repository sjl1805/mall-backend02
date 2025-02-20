package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品SKU表
 *
 * @TableName product_sku
 */
@Schema(description = "商品SKU实体")
@TableName(value = "product_sku")
@Data
public class ProductSku implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * SKU ID
     */
    @Schema(description = "SKU ID", example = "3001")
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 商品ID
     */
    @Schema(description = "关联商品ID", example = "2001")
    private Long productId;
    /**
     * 规格值组合（JSON）
     */
    @Schema(description = "规格组合JSON", example = "{\"颜色\":\"黑色\",\"存储\":\"128GB\"}")
    private String specValues;
    /**
     * 价格
     */
    @Schema(description = "SKU价格", example = "5999.00")
    private BigDecimal price;
    /**
     * 库存
     */
    @Schema(description = "SKU库存", example = "50")
    private Integer stock;
    /**
     * 销量
     */
    @Schema(description = "累计销量", example = "150")
    private Integer sales;
    /**
     * SKU主图
     */
    @Schema(description = "SKU主图地址", example = "/images/sku/mate50_black.jpg")
    private String mainImage;
    /**
     * SKU状态：0-下架 1-上架
     */
    @Schema(description = "SKU状态：0-下架 1-上架", example = "1")
    private Integer status;
    /**
     * 创建时间（带时区）
     */
    @Schema(description = "创建时间", example = "2023-08-01T10:15:30")
    private LocalDateTime createTime;
    /**
     * 更新时间（带时区）
     */
    @Schema(description = "更新时间", example = "2023-08-01T10:15:30")
    private LocalDateTime updateTime;
}