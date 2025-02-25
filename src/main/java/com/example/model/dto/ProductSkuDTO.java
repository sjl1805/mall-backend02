package com.example.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 商品SKU数据传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "商品SKU信息DTO")
public class ProductSkuDTO {
    
    @Schema(description = "SKU ID")
    private Long id;
    
    @Schema(description = "商品ID")
    private Long productId;
    
    @Schema(description = "SKU编码")
    private String skuCode;
    
    @Schema(description = "商品规格，如：{\"颜色\":\"红色\",\"尺寸\":\"XL\"}")
    private Map<String, String> specifications;
    
    @Schema(description = "SKU价格")
    private BigDecimal price;
    
    @Schema(description = "SKU库存")
    private Integer stock;
    
    @Schema(description = "SKU图片")
    private String image;
    
    @Schema(description = "状态：0-禁用 1-启用")
    private Integer status;
} 