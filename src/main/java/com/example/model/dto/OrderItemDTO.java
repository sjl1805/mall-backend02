package com.example.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDTO {
    private Long id;

    @NotNull(message = "订单ID不能为空")
    @Min(value = 1, message = "订单ID不合法")
    @Schema(description = "订单ID", example = "1")
    private Long orderId;

    @NotNull(message = "商品ID不能为空")
    @Min(value = 1, message = "商品ID不合法")
    @Schema(description = "商品ID", example = "1")
    private Long productId;

    @NotBlank(message = "商品名称不能为空")
    @Schema(description = "商品名称", example = "苹果")
    private String productName;

    @NotBlank(message = "商品主图不能为空")
    @Schema(description = "商品主图URL", example = "http://example.com/image.jpg")
    private String productImage;

    @NotNull(message = "商品单价不能为空")
    @Schema(description = "商品单价", example = "9.99")
    private BigDecimal price;

    @NotNull(message = "购买数量不能为空")
    @Min(value = 1, message = "购买数量必须大于0")
    @Schema(description = "购买数量", example = "2")
    private Integer quantity;

    @NotNull(message = "商品总价不能为空")
    @Schema(description = "商品总价", example = "19.98")
    private BigDecimal totalAmount;
} 