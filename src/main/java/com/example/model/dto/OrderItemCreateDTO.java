package com.example.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "创建订单商品项DTO")
public class OrderItemCreateDTO {
    
    @Schema(description = "商品ID", required = true, example = "1")
    @NotNull(message = "商品ID不能为空")
    private Long productId;
    
    @Schema(description = "SKU ID", required = true, example = "10")
    @NotNull(message = "SKU ID不能为空")
    private Long skuId;
    
    @Schema(description = "购买数量", required = true, example = "2")
    @NotNull(message = "商品数量不能为空")
    @Min(value = 1, message = "商品数量必须大于0")
    private Integer quantity;
} 