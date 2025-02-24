package com.example.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CategoryVO {
    private Long id;

    @Schema(description = "父分类ID", example = "0")
    private Long parentId;

    @Schema(description = "分类名称", example = "电子产品")
    private String name;

    @Schema(description = "分类图标", example = "http://example.com/icon.png")
    private String icon;

    @Schema(description = "层级：1一级 2二级 3三级", example = "1")
    private Integer level;

    @Schema(description = "排序字段", example = "1")
    private Integer sort; // 排序字段

    @Schema(description = "状态：0-禁用 1-启用", example = "1")
    private Integer status;
} 