package com.example.model.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDTO {
    private Long id;

    @NotNull(message = "订单ID不能为空")
    @Min(value = 1, message = "订单ID不合法")
    private Long orderId;

    @NotNull(message = "商品ID不能为空")
    @Min(value = 1, message = "商品ID不合法")
    private Long productId;

    @NotBlank(message = "商品名称不能为空")
    private String productName;

    @NotBlank(message = "商品主图不能为空")
    private String productImage;

    @NotNull(message = "商品单价不能为空")
    private BigDecimal price;

    @NotNull(message = "购买数量不能为空")
    @Min(value = 1, message = "购买数量必须大于0")
    private Integer quantity;

    @NotNull(message = "商品总价不能为空")
    private BigDecimal totalAmount;
} 