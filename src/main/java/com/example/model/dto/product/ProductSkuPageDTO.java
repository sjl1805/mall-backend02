package com.example.model.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Schema(description = "SKU分页查询参数")
public class ProductSkuPageDTO {
    @Schema(description = "商品ID（精确查询）", example = "123", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long productId;

    @Schema(description = "最低价格", example = "100.00")
    private BigDecimal minPrice;

    @Schema(description = "最高价格", example = "500.00")
    private BigDecimal maxPrice;

    @Schema(description = "SKU状态：0-下架 1-上架",
            allowableValues = {"0", "1"}, example = "1")
    private Integer status;

    @Schema(description = "创建时间起始")
    private Date createTimeStart;

    @Schema(description = "创建时间结束")
    private Date createTimeEnd;

    @Schema(description = "页码", example = "1", defaultValue = "1")
    private Integer page = 1;

    @Schema(description = "每页数量", example = "10", defaultValue = "10")
    private Integer size = 10;

    @Schema(description = "排序字段",
            allowableValues = {"price", "sales", "stock", "create_time"},
            example = "price")
    private String sortField;

    @Schema(description = "排序方式",
            allowableValues = {"ASC", "DESC"},
            example = "ASC")
    private String sortOrder;
} 