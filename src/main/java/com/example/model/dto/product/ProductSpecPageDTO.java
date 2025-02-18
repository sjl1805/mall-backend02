package com.example.model.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.Date;

@Data
@Schema(description = "商品规格分页查询参数")
public class ProductSpecPageDTO {
    @Schema(description = "商品ID（精确查询）", example = "123", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long productId;

    @Schema(description = "规格名称（模糊查询）", example = "颜色")
    private String specName;

    @Schema(description = "创建时间起始", example = "2023-01-01")
    private Date createTimeStart;

    @Schema(description = "创建时间结束", example = "2023-12-31")
    private Date createTimeEnd;

    @Schema(description = "页码", example = "1", defaultValue = "1")
    private Integer page = 1;

    @Schema(description = "每页数量", example = "10", defaultValue = "10")
    private Integer size = 10;

    @Schema(description = "排序字段", 
           allowableValues = {"create_time", "update_time"}, 
           example = "create_time")
    private String sortField;

    @Schema(description = "排序方式", 
           allowableValues = {"ASC", "DESC"}, 
           example = "ASC")
    private String sortOrder;
} 