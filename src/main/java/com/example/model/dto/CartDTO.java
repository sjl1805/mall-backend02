package com.example.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 购物车数据传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "购物车DTO")
public class CartDTO {
    
    @Schema(description = "用户ID")
    private Long userId;
    
    @Schema(description = "购物车商品列表")
    private List<CartItemDTO> items;
    
    @Schema(description = "商品总数量")
    private Integer totalQuantity; // 商品总数量
    
    @Schema(description = "商品总金额")
    private BigDecimal totalAmount; // 商品总金额
    
    @Schema(description = "已选中的商品数量")
    private Integer checkedCount; // 已选中的商品数量
    
    @Schema(description = "已选中的商品总金额")
    private BigDecimal checkedAmount; // 已选中的商品总金额
} 