package com.example.model.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Schema(description = "商品分页查询参数")
public class ProductsPageDTO {
    @Schema(description = "分类ID（精确查询）")
    private Long categoryId;

    @Schema(description = "商品名称（模糊查询）")
    private String name;

    @Schema(description = "价格范围最小值")
    private BigDecimal minPrice;

    @Schema(description = "价格范围最大值")
    private BigDecimal maxPrice;

    @Schema(description = "库存最小值")
    private Integer minStock;

    @Schema(description = "状态：0-下架 1-上架", allowableValues = {"0", "1"})
    private Integer status;

    @Schema(description = "创建时间起始")
    private Date createTimeStart;

    @Schema(description = "创建时间结束")
    private Date createTimeEnd;

    @Schema(description = "页码", example = "1")
    private Integer page = 1;

    @Schema(description = "每页数量", example = "10")
    private Integer size = 10;

    @Schema(description = "排序字段", allowableValues = {"createTime", "updateTime", "price", "sales"})
    private String sortField;

    @Schema(description = "排序方式", allowableValues = {"ASC", "DESC"})
    private String sortOrder;
} 