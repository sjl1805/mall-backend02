package com.example.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductSkuDTO {
    private Long id;

    @NotNull(message = "商品ID不能为空")
    @Min(value = 1, message = "商品ID不合法")
    @Schema(description = "商品ID", example = "1")
    private Long productId;

    @NotBlank(message = "规格组合不能为空")
    @Schema(description = "规格值组合（JSON）", example = "{\"color\":\"red\",\"size\":\"M\"}")
    private String specValues;

    @NotNull(message = "价格不能为空")
    @DecimalMin(value = "0.01", message = "价格不能小于0.01")
    @Schema(description = "商品单价", example = "99.99")
    private BigDecimal price;

    @NotNull(message = "库存不能为空")
    @Min(value = 0, message = "库存不能为负数")
    @Schema(description = "商品库存", example = "100")
    private Integer stock;

    @NotBlank(message = "主图不能为空")
    @Schema(description = "SKU主图URL", example = "http://example.com/image.jpg")
    private String mainImage;

    @NotNull(message = "状态不能为空")
    @Min(value = 0, message = "状态值不合法")
    @Max(value = 1, message = "状态值不合法")
    @Schema(description = "SKU状态：0-下架 1-上架", example = "1")
    private Integer status;
} 