package com.example.model.dto;

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
public class ProductSkuDTO {
    
    private Long id;
    private Long productId;
    private String skuCode;
    private Map<String, String> specifications; // 规格JSON解析后的键值对
    private BigDecimal price;
    private Integer stock;
    private String image;
    private Integer status; // 状态：0-禁用 1-启用
} 