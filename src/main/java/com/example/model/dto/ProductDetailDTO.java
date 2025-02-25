package com.example.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 商品详情数据传输对象
 */
@Data
public class ProductDetailDTO {

    /**
     * 商品ID
     */
    private Long id;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 分类路径
     */
    private List<CategoryDTO> categoryPath;

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
     * 主图URL
     */
    private String imageMain;

    /**
     * 商品图片列表
     */
    private List<String> images;

    /**
     * 商品标签列表
     */
    private List<ProductTagDTO> tags;

    /**
     * 商品状态：0-下架 1-上架
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

    /**
     * 相关推荐商品
     */
    private List<ProductDTO> recommendProducts;
} 