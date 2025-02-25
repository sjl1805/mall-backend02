package com.example.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "商品详情DTO")
public class ProductDetailDTO {
    
    @Schema(description = "商品ID")
    private Long id;
    
    @Schema(description = "分类ID")
    private Long categoryId;
    
    @Schema(description = "分类名称")
    private String categoryName;
    
    @Schema(description = "商品名称")
    private String name;
    
    @Schema(description = "商品描述")
    private String description;
    
    @Schema(description = "商品价格")
    private BigDecimal price;
    
    @Schema(description = "商品库存")
    private Integer stock;
    
    @Schema(description = "商品主图")
    private String imageMain;
    
    @Schema(description = "商品图片列表")
    private List<String> imageList;
    
    @Schema(description = "商品标签列表")
    private List<ProductTagDTO> tagList;
    
    @Schema(description = "商品状态：0-下架 1-上架")
    private Integer status;
    
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    @Schema(description = "商品SKU列表")
    private List<ProductSkuDTO> skuList;
    
    @Schema(description = "相似商品推荐")
    private List<ProductDTO> similarProducts;
    
    @Schema(description = "评价数量")
    private Integer reviewCount;
    
    @Schema(description = "平均评分")
    private BigDecimal averageRating;
} 