package com.example.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 购物车视图对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartVO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 购物车ID
     */
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 商品ID
     */
    private Long productId;
    
    /**
     * 商品名称
     */
    private String productName;
    
    /**
     * 商品图片
     */
    private String productImage;
    
    /**
     * SKU ID
     */
    private Long skuId;
    
    /**
     * SKU规格值（例如：颜色:红色,尺寸:XL）
     */
    private String skuSpecs;
    
    /**
     * 商品单价
     */
    private BigDecimal price;
    
    /**
     * 商品数量
     */
    private Integer quantity;
    
    /**
     * 商品小计
     */
    private BigDecimal subtotal;
    
    /**
     * 选中状态：0-未选中 1-已选中
     */
    private Integer checked;
    
    /**
     * 库存状态：true-有库存 false-无库存
     */
    private Boolean stockStatus;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
} 