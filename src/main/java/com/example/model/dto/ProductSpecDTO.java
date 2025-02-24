package com.example.model.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProductSpecDTO {
    private Long id;

    @NotNull(message = "商品ID不能为空")
    @Min(value = 1, message = "商品ID不合法")
    private Long productId;

    @NotBlank(message = "规格名称不能为空")
    @Size(max = 50, message = "规格名称最长50个字符")
    private String specName;

    @NotBlank(message = "规格值不能为空")
    private String specValues; // 保持JSON字符串格式
} 