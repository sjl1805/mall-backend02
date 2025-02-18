package com.example.model.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Data
@Schema(description = "商品SKU数据传输对象")
public class ProductSkuDTO {
    @Schema(description = "SKU ID（更新时必填）", example = "456")
    private Long id;

    @NotNull(message = "商品ID不能为空")
    @Schema(description = "商品ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "123")
    private Long productId;

    @NotBlank(message = "规格组合不能为空")
    @Schema(description = "规格值组合（JSON格式）", requiredMode = Schema.RequiredMode.REQUIRED, 
          example = "{\"颜色\":\"红色\",\"尺寸\":\"XL\"}")
    private String specValues;

    @NotNull(message = "价格不能为空")
    @DecimalMin(value = "0.01", message = "价格必须大于0")
    @Schema(description = "价格", requiredMode = Schema.RequiredMode.REQUIRED, example = "299.99")
    private BigDecimal price;

    @NotNull(message = "库存不能为空")
    @Min(value = 0, message = "库存不能为负")
    @Schema(description = "库存", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    private Integer stock;

    @Schema(description = "SKU主图", example = "/images/red-shirt-xl.jpg")
    private String mainImage = "/images/backend.png";

    @Min(0) @Max(1)
    @Schema(description = "状态：0-下架 1-上架", defaultValue = "1", example = "1")
    private Integer status = 1;
} 