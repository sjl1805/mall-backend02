package com.example.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 购物车统计视图对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartStatsVO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 购物车商品种类数量
     */
    private Integer itemCount;
    
    /**
     * 购物车商品总数量
     */
    private Integer productCount;
    
    /**
     * 购物车商品总价
     */
    private BigDecimal totalAmount;
    
    /**
     * 已选中商品种类数量
     */
    private Integer checkedItemCount;
    
    /**
     * 已选中商品总数量
     */
    private Integer checkedProductCount;
    
    /**
     * 已选中商品总价
     */
    private BigDecimal checkedAmount;
} 