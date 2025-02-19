package com.example.model.dto.favorite;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "收藏夹分页查询参数")
public class FavoriteFolderPageDTO {
    @Schema(description = "用户ID（精确查询）", example = "123")
    private Long userId;

    @Schema(description = "收藏夹名称（模糊查询）", example = "我的收藏")
    private String name;

    @Schema(description = "公开状态：0-私密 1-公开",
            allowableValues = {"0", "1"}, example = "1")
    private Integer isPublic;

    @Schema(description = "创建时间起始", example = "2023-01-01")
    private Date createTimeStart;

    @Schema(description = "创建时间结束", example = "2023-12-31")
    private Date createTimeEnd;

    @Schema(description = "页码", example = "1", defaultValue = "1")
    private Integer page = 1;

    @Schema(description = "每页数量", example = "10", defaultValue = "10")
    private Integer size = 10;

    @Schema(description = "排序字段",
            allowableValues = {"sort", "createTime", "itemCount"},
            example = "sort")
    private String sortField;

    @Schema(description = "排序方式",
            allowableValues = {"ASC", "DESC"},
            example = "ASC")
    private String sortOrder;
} 