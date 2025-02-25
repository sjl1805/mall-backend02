package com.example.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品详情数据传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailDTO {
    
    private Long id;
    private Long categoryId;
    private String categoryName;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private String imageMain;
    private List<String> imageList;
    private List<ProductTagDTO> tagList;
    private Integer status; // 商品状态：0-下架 1-上架
    private LocalDateTime createTime;
    
    // 商品SKU列表
    private List<ProductSkuDTO> skuList;
    
    // 相似商品推荐
    private List<ProductDTO> similarProducts;
    
    // 评价统计
    private Integer reviewCount;
    private BigDecimal averageRating;
} 