package com.example.model.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "商品评价分页查询参数")
public class ProductReviewPageDTO {
    @Schema(description = "商品ID（精确查询）", example = "123")
    private Long productId;

    @Schema(description = "用户ID（精确查询）", example = "456")
    private Long userId;

    @Schema(description = "审核状态：0-待审核 1-已通过 2-已拒绝",
            allowableValues = {"0", "1", "2"}, example = "1")
    private Integer status;

    @Schema(description = "最低评分", example = "3")
    private Integer minRating;

    @Schema(description = "最高评分", example = "5")
    private Integer maxRating;

    @Schema(description = "创建时间起始", example = "2023-01-01")
    private Date createTimeStart;

    @Schema(description = "创建时间结束", example = "2023-12-31")
    private Date createTimeEnd;

    @Schema(description = "页码", example = "1", defaultValue = "1")
    private Integer page = 1;

    @Schema(description = "每页数量", example = "10", defaultValue = "10")
    private Integer size = 10;

    @Schema(description = "排序字段",
            allowableValues = {"create_time", "rating"},
            example = "create_time")
    private String sortField;

    @Schema(description = "排序方式",
            allowableValues = {"ASC", "DESC"},
            example = "DESC")
    private String sortOrder;
} 