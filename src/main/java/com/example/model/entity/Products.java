package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品表
 * 该实体类表示商城的商品信息，是系统的核心数据实体之一
 * 包含商品的基本信息、价格、库存、图片等属性
 * 商品与分类、规格、评价等实体有关联关系
 *
 * @TableName products
 */
@TableName(value = "products")
@Data
@Schema(description = "商品实体")
public class Products implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 商品ID
     */
    @TableId(type = IdType.AUTO)
    @Schema(description = "商品ID", example = "1")
    private Long id;
    /**
     * 分类ID
     */
    @Schema(description = "分类ID", example = "100")
    private Long categoryId;
    /**
     * 商品名称
     */
    @Schema(description = "商品名称", example = "苹果手机 iPhone 14 Pro")
    private String name;
    /**
     * 商品描述
     */
    @Schema(description = "商品描述", example = "iPhone 14 Pro搭载A16仿生芯片，配备4800万像素主摄，支持卫星通信")
    private String description;
    /**
     * 价格
     */
    @Schema(description = "商品价格", example = "7999.00")
    private BigDecimal price;
    /**
     * 库存
     */
    @Schema(description = "商品库存", example = "200")
    private Integer stock;
    /**
     * 商品图片JSON数组
     */
    @Schema(description = "商品图片JSON数组", example = "[\"http://example.com/img1.jpg\",\"http://example.com/img2.jpg\"]")
    private Object images;
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
    /**
     * 商品状态：0-下架 1-上架
     */
    @Schema(description = "商品状态：0-下架 1-上架", example = "1")
    private Integer status;
    /**
     * 非数据库字段，商品分类名称
     */
    @TableField(exist = false)
    @Schema(description = "商品分类名称", example = "手机数码")
    private String categoryName;

    /**
     * 非数据库字段，商品销量
     */
    @TableField(exist = false)
    @Schema(description = "商品销量", example = "500")
    private Integer salesCount;

    /**
     * 非数据库字段，商品评分（1-5星）
     */
    @TableField(exist = false)
    @Schema(description = "商品评分（1-5星）", example = "4.8")
    private Double rating;

    /**
     * 非数据库字段，评价数量
     */
    @TableField(exist = false)
    @Schema(description = "评价数量", example = "120")
    private Integer reviewCount;

    /**
     * 非数据库字段，是否为新品
     */
    @TableField(exist = false)
    @Schema(description = "是否为新品", example = "true")
    private Boolean isNew;

    /**
     * 非数据库字段，是否热门
     */
    @TableField(exist = false)
    @Schema(description = "是否热门", example = "true")
    private Boolean isHot;

    /**
     * 非数据库字段，商品标签列表
     */
    @TableField(exist = false)
    @Schema(description = "商品标签列表", example = "[\"新品\",\"热门\",\"限时促销\"]")
    private List<String> tags;

    /**
     * 非数据库字段，商品规格信息
     */
    @TableField(exist = false)
    @Schema(description = "商品规格信息")
    private List<ProductSpec> specs;
}