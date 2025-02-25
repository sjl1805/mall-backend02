package com.example.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * 创建订单项数据传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemCreateDTO {
    
    @NotNull(message = "商品ID不能为空")
    private Long productId;
    
    @NotNull(message = "SKU ID不能为空")
    private Long skuId;
    
    @NotNull(message = "商品数量不能为空")
    @Min(value = 1, message = "商品数量必须大于0")
    private Integer quantity;
} 