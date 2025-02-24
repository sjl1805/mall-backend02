package com.example.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDTO {
    private Long id;

    @NotNull(message = "分类ID不能为空")
    @Min(value = 1, message = "分类ID不合法")
    @Schema(description = "分类ID", example = "1")
    private Long categoryId;

    @NotBlank(message = "商品名称不能为空")
    @Size(max = 100, message = "商品名称最长100个字符")
    @Schema(description = "商品名称", example = "苹果")
    private String name;

    @NotBlank(message = "商品描述不能为空")
    @Size(max = 500, message = "描述最长500个字符")
    @Schema(description = "商品描述", example = "新鲜的苹果")
    private String description;

    @NotNull(message = "价格不能为空")
    @DecimalMin(value = "0.01", message = "价格不能小于0.01")
    @Schema(description = "商品价格", example = "9.99")
    private BigDecimal price;

    @NotNull(message = "库存不能为空")
    @Min(value = 0, message = "库存不能为负数")
    @Schema(description = "商品库存", example = "100")
    private Integer stock;

    @NotBlank(message = "商品图片不能为空")
    @Schema(description = "商品图片URL", example = "http://example.com/image.jpg")
    private String images;

    @NotNull(message = "状态不能为空")
    @Min(value = 0, message = "状态值不合法")
    @Max(value = 1, message = "状态值不合法")
    @Schema(description = "商品状态：0-下架 1-上架", example = "1")
    private Integer status;
} 