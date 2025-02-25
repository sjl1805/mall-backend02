package com.example.model.dto;

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
public class OrderItemDTO {
    
    private Long id;
    private Long orderId;
    private Long productId;
    private Long skuId;
    private String productName;
    private String productImage;
    private Map<String, String> specifications; // 商品规格
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal totalAmount;
} 