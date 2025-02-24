package com.example.model.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductSkuDTO {
    private Long id;

    @NotNull(message = "商品ID不能为空")
    @Min(value = 1, message = "商品ID不合法")
    private Long productId;

    @NotBlank(message = "规格组合不能为空")
    private String specValues;

    @NotNull(message = "价格不能为空")
    @DecimalMin(value = "0.01", message = "价格不能小于0.01")
    private BigDecimal price;

    @NotNull(message = "库存不能为空")
    @Min(value = 0, message = "库存不能为负数")
    private Integer stock;

    @NotBlank(message = "主图不能为空")
    private String mainImage;

    @NotNull(message = "状态不能为空")
    @Min(value = 0, message = "状态值不合法")
    @Max(value = 1, message = "状态值不合法")
    private Integer status;
} 