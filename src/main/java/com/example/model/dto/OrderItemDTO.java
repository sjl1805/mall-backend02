package com.example.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 订单项数据传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "订单商品项DTO")
public class OrderItemDTO {
    
    @Schema(description = "订单项ID")
    private Long id;
    
    @Schema(description = "订单ID")
    private Long orderId;
    
    @Schema(description = "商品ID")
    private Long productId;
    
    @Schema(description = "SKU ID")
    private Long skuId;
    
    @Schema(description = "商品名称")
    private String productName;
    
    @Schema(description = "商品图片")
    private String productImage;
    
    @Schema(description = "商品规格，如：{\"颜色\":\"红色\",\"尺寸\":\"XL\"}")
    private Map<String, String> specifications;
    
    @Schema(description = "商品单价")
    private BigDecimal price;
    
    @Schema(description = "购买数量")
    private Integer quantity;
    
    @Schema(description = "商品总价")
    private BigDecimal totalAmount;
} 