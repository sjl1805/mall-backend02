package com.example.model.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@Schema(description = "商品数据传输对象")
public class ProductsDTO {
    @Schema(description = "商品ID（更新时必填）", example = "1")
    private Long id;

    @NotNull(message = "分类ID不能为空")
    @Schema(description = "分类ID", example = "1001", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long categoryId;

    @NotBlank(message = "商品名称不能为空")
    @Size(min = 2, max = 50, message = "商品名称长度2-50个字符")
    @Schema(description = "商品名称", example = "华为Mate50", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Size(max = 500, message = "描述最多500个字符")
    @Schema(description = "商品描述", example = "旗舰智能手机")
    private String description;

    @DecimalMin(value = "0.01", message = "价格必须大于0")
    @Schema(description = "商品价格", example = "5999.00", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal price;

    @Min(value = 0, message = "库存不能为负数")
    @Schema(description = "商品库存", example = "100", defaultValue = "0")
    private Integer stock = 0;

    @Schema(description = "商品图片URL列表", example = "['/images/phone1.jpg','/images/phone2.jpg']")
    private List<String> images;

    @NotNull(message = "状态不能为空")
    @Min(value = 0, message = "状态参数错误")
    @Max(value = 1, message = "状态参数错误")
    @Schema(description = "状态：0-下架 1-上架", example = "1", defaultValue = "1")
    private Integer status = 1;
} 