package com.example.model.dto.favorite;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "商品收藏分页查询参数")
public class ProductFavoritePageDTO {
    @Schema(description = "用户ID（精确查询）", example = "123", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long userId;

    @Schema(description = "商品ID（精确查询）", example = "456")
    private Long productId;

    @Schema(description = "收藏夹ID（精确查询）", example = "789")
    private Long folderId;

    @Schema(description = "创建时间起始")
    private Date createTimeStart;

    @Schema(description = "创建时间结束")
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
            example = "DESC")
    private String sortOrder;
} 