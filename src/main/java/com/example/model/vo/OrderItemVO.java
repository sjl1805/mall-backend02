package com.example.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemVO {
    private Long id;

    @Schema(description = "订单ID", example = "1")
    private Long orderId;

    @Schema(description = "商品ID", example = "1")
    private Long productId;

    @Schema(description = "商品名称", example = "苹果")
    private String productName;

    @Schema(description = "商品主图URL", example = "http://example.com/image.jpg")
    private String productImage;

    @Schema(description = "商品单价", example = "9.99")
    private BigDecimal price;

    @Schema(description = "购买数量", example = "2")
    private Integer quantity;

    @Schema(description = "商品总价", example = "19.98")
    private BigDecimal totalAmount;
} 