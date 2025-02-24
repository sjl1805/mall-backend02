package com.example.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CategoryDTO {
    private Long id;

    @NotNull(message = "父分类ID不能为空")
    @Schema(description = "父分类ID", example = "0")
    private Long parentId;

    @NotBlank(message = "分类名称不能为空")
    @Size(max = 50, message = "分类名称最长50个字符")
    @Schema(description = "分类名称", example = "电子产品")
    private String name;

    @Size(max = 200, message = "分类图标最长200个字符")
    @Schema(description = "分类图标", example = "http://example.com/icon.png")
    private String icon;

    @NotNull(message = "层级不能为空")
    @Min(value = 1, message = "层级不合法")
    @Max(value = 3, message = "层级不合法")
    @Schema(description = "层级：1一级 2二级 3三级", example = "1")
    private Integer level;

    @Schema(description = "排序字段", example = "1")
    private Integer sort; // 排序字段

    @NotNull(message = "状态不能为空")
    @Min(value = 0, message = "状态值不合法")
    @Max(value = 1, message = "状态值不合法")
    @Schema(description = "状态：0-禁用 1-启用", example = "1")
    private Integer status;
} 