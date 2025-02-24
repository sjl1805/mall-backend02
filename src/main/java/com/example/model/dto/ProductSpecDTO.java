package com.example.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProductSpecDTO {
    private Long id;

    @NotNull(message = "商品ID不能为空")
    @Min(value = 1, message = "商品ID不合法")
    @Schema(description = "商品ID", example = "1")
    private Long productId;

    @NotBlank(message = "规格名称不能为空")
    @Size(max = 50, message = "规格名称最长50个字符")
    @Schema(description = "规格名称", example = "颜色")
    private String specName;

    @NotBlank(message = "规格值不能为空")
    @Schema(description = "规格值，JSON格式", example = "{\"color\":\"red\",\"size\":\"M\"}")
    private String specValues; // 保持JSON字符串格式
} 