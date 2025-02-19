package com.example.model.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;

@Data
@Schema(description = "商品SKU数据传输对象")
public class ProductSkuDTO {
    @Schema(description = "SKU ID（更新时必填）", example = "1")
    private Long id;

    @NotNull(message = "商品ID不能为空")
    @Schema(description = "关联商品ID", example = "1001", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long productId;

    @NotBlank(message = "规格值不能为空")
    @Size(max = 200, message = "规格值最长200个字符")
    @Schema(description = "规格组合值（JSON格式）", example = "{\"颜色\":\"黑色\",\"内存\":\"256GB\"}", requiredMode = Schema.RequiredMode.REQUIRED)
    private String specValues;

    @DecimalMin(value = "0.01", message = "价格必须大于0")
    @Schema(description = "销售价格", example = "5999.00", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal price;

    @Min(value = 0, message = "库存不能为负数")
    @Schema(description = "库存数量", example = "100", defaultValue = "0")
    private Integer stock = 0;

    @Min(value = 0, message = "销量不能为负数")
    @Schema(description = "累计销量", example = "50", defaultValue = "0")
    private Integer sales = 0;

    @URL(message = "主图地址格式不正确")
    @Schema(description = "主图URL", example = "/images/sku1.jpg")
    private String mainImage;

    @NotNull(message = "状态不能为空")
    @Min(value = 0, message = "状态参数错误")
    @Max(value = 1, message = "状态参数错误")
    @Schema(description = "状态：0-下架 1-上架", example = "1", defaultValue = "1")
    private Integer status = 1;
} 