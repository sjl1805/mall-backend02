package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

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
public class Products implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 商品ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 分类ID
     */
    private Long categoryId;
    /**
     * 商品名称
     */
    private String name;
    /**
     * 商品描述
     */
    private String description;
    /**
     * 价格
     */
    private BigDecimal price;
    /**
     * 库存
     */
    private Integer stock;
    /**
     * 商品图片JSON数组
     */
    private Object images;
    /**
     * 创建时间（带时区）
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 更新时间（带时区）
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    /**
     * 商品状态：0-下架 1-上架
     */
    private Integer status;
    /**
     * 非数据库字段，商品分类名称
     */
    @TableField(exist = false)
    private String categoryName;

    /**
     * 非数据库字段，商品销量
     */
    @TableField(exist = false)
    private Integer salesCount;

    /**
     * 非数据库字段，商品评分（1-5星）
     */
    @TableField(exist = false)
    private Double rating;

    /**
     * 非数据库字段，评价数量
     */
    @TableField(exist = false)
    private Integer reviewCount;

    /**
     * 非数据库字段，是否为新品
     */
    @TableField(exist = false)
    private Boolean isNew;

    /**
     * 非数据库字段，是否热门
     */
    @TableField(exist = false)
    private Boolean isHot;

    /**
     * 非数据库字段，商品标签列表
     */
    @TableField(exist = false)
    private List<String> tags;

    /**
     * 非数据库字段，商品规格信息
     */
    @TableField(exist = false)
    private List<ProductSpec> specs;
}