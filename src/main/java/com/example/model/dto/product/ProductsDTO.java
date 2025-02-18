package com.example.model.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Data
@Schema(description = "商品数据传输对象")
public class ProductsDTO {
    @Schema(description = "商品ID（更新时必填）")
    private Long id;

    @NotNull(message = "分类ID不能为空")
    @Schema(description = "分类ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long categoryId;

    @NotBlank(message = "商品名称不能为空")
    @Size(max = 100, message = "名称最长100个字符")
    @Schema(description = "商品名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotBlank(message = "商品描述不能为空")
    @Size(max = 1000, message = "描述最长1000个字符")
    @Schema(description = "商品描述", requiredMode = Schema.RequiredMode.REQUIRED)
    private String description;

    @NotNull(message = "价格不能为空")
    @DecimalMin(value = "0.01", message = "价格必须大于0")
    @Schema(description = "价格", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal price;

    @NotNull(message = "库存不能为空")
    @Min(value = 0, message = "库存不能为负数")
    @Schema(description = "库存", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer stock;

    @Schema(description = "商品图片（JSON数组）")
    private String images;

    @Min(value = 0, message = "状态参数错误")
    @Max(value = 1, message = "状态参数错误")
    @Schema(description = "商品状态：0-下架 1-上架", defaultValue = "1")
    private Integer status = 1;
} 