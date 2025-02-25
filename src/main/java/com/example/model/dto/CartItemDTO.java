package com.example.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 购物车商品数据传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "购物车商品项DTO")
public class CartItemDTO {
    
    @Schema(description = "购物车项ID")
    private Long id;
    
    @Schema(description = "用户ID")
    private Long userId;
    
    @Schema(description = "商品ID")
    private Long productId;
    
    @Schema(description = "SKU ID")
    private Long skuId;
    
    @Schema(description = "商品名称")
    private String productName;
    
    @Schema(description = "商品图片")
    private String productImage;
    
    @Schema(description = "商品单价")
    private BigDecimal price;
    
    @Schema(description = "商品数量")
    private Integer quantity;
    
    @Schema(description = "商品总价")
    private BigDecimal totalPrice;
    
    @Schema(description = "选中状态：0-未选中 1-已选中")
    private Integer checked; // 选中状态：0-未选中 1-已选中
    
    @Schema(description = "商品规格")
    private Map<String, String> specifications; // SKU规格
} 