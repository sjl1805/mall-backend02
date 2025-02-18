package com.example.model.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.*;

@Data
@Schema(description = "商品规格数据传输对象")
public class ProductSpecDTO {
    @Schema(description = "规格ID（更新时必填）", example = "456")
    private Long id;

    @NotNull(message = "商品ID不能为空")
    @Schema(description = "商品ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "123")
    private Long productId;

    @NotBlank(message = "规格名称不能为空")
    @Size(max = 64, message = "规格名称最长64个字符")
    @Schema(description = "规格名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "颜色")
    private String specName;

    @NotBlank(message = "规格值不能为空")
    @Schema(description = "规格值（JSON格式）", requiredMode = Schema.RequiredMode.REQUIRED, 
          example = "[\"红色\",\"蓝色\",\"绿色\"]")
    private String specValues;
} 