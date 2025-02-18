package com.example.model.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.Date;

@Data
@Schema(description = "分类分页查询参数")
public class CategoryPageDTO {
    @Schema(description = "父分类ID（精确查询）")
    private Long parentId;

    @Schema(description = "分类名称（模糊查询）")
    private String name;

    @Schema(description = "层级：1-一级 2-二级 3-三级", allowableValues = {"1", "2", "3"})
    private Integer level;

    @Schema(description = "状态：0-禁用 1-启用", allowableValues = {"0", "1"})
    private Integer status;

    @Schema(description = "创建时间起始")
    private Date createTimeStart;

    @Schema(description = "创建时间结束")
    private Date createTimeEnd;

    @Schema(description = "页码", example = "1")
    private Integer page = 1;

    @Schema(description = "每页数量", example = "10")
    private Integer size = 10;

    @Schema(description = "排序字段", allowableValues = {"createTime", "updateTime", "sort"})
    private String sortField;

    @Schema(description = "排序方式", allowableValues = {"ASC", "DESC"})
    private String sortOrder;
} 