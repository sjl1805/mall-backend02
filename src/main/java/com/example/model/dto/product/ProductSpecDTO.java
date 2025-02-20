package com.example.model.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import com.example.model.entity.ProductSpec;

@Data
@Schema(description = "商品规格数据传输对象")
public class ProductSpecDTO {
    @Schema(description = "规格ID（更新时必填）", example = "1")
    private Long id;

    @NotNull(message = "商品ID不能为空")
    @Schema(description = "关联商品ID", example = "1001", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long productId;

    @NotBlank(message = "规格名称不能为空")
    @Size(max = 50, message = "规格名称最长50个字符")
    @Schema(description = "规格名称", example = "颜色", requiredMode = Schema.RequiredMode.REQUIRED)
    private String specName;

    @NotBlank(message = "规格值不能为空")
    @Size(max = 500, message = "规格值最长500个字符")
    @Schema(description = "规格可选值（JSON数组）", example = "[\"红色\",\"蓝色\",\"黑色\"]", requiredMode = Schema.RequiredMode.REQUIRED)
    private String specValues;

    public static ProductSpecDTO fromEntity(ProductSpec spec) {
        ProductSpecDTO dto = new ProductSpecDTO();
        dto.setId(spec.getId());
        dto.setProductId(spec.getProductId());
        dto.setSpecName(spec.getSpecName());
        dto.setSpecValues(spec.getSpecValues());
        return dto;
    }

    public ProductSpec toEntity() {
        ProductSpec spec = new ProductSpec();
        spec.setId(this.id);
        spec.setProductId(this.productId);
        spec.setSpecName(this.specName);
        spec.setSpecValues(this.specValues);
        return spec;
    }
} 