package com.example.model.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "推荐商品分页查询参数")
public class RecommendProductPageDTO {
    @Schema(description = "推荐类型：1-热门商品 2-新品推荐",
            allowableValues = {"1", "2"}, example = "1")
    private Integer type;

    @Schema(description = "推荐状态：0-未生效 1-生效中",
            allowableValues = {"0", "1"}, example = "1")
    private Integer status;

    @Schema(description = "开始时间范围起始", example = "2023-01-01 00:00:00")
    private Date startTimeBegin;

    @Schema(description = "开始时间范围结束", example = "2023-12-31 23:59:59")
    private Date startTimeEnd;

    @Schema(description = "算法版本号", example = "v2.3.1")
    private String algorithmVersion;

    @Schema(description = "页码", example = "1", defaultValue = "1")
    private Integer page = 1;

    @Schema(description = "每页数量", example = "10", defaultValue = "10")
    private Integer size = 10;

    @Schema(description = "排序字段",
            allowableValues = {"sort", "start_time", "create_time"},
            example = "sort")
    private String sortField;

    @Schema(description = "排序方式",
            allowableValues = {"ASC", "DESC"},
            example = "ASC")
    private String sortOrder;
} 