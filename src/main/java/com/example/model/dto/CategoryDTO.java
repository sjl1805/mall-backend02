package com.example.model.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CategoryDTO {
    private Long id;

    @NotNull(message = "父分类ID不能为空")
    private Long parentId;

    @NotBlank(message = "分类名称不能为空")
    @Size(max = 50, message = "分类名称最长50个字符")
    private String name;

    @Size(max = 200, message = "分类图标最长200个字符")
    private String icon;

    @NotNull(message = "层级不能为空")
    @Min(value = 1, message = "层级不合法")
    @Max(value = 3, message = "层级不合法")
    private Integer level;

    private Integer sort; // 排序字段

    @NotNull(message = "状态不能为空")
    @Min(value = 0, message = "状态值不合法")
    @Max(value = 1, message = "状态值不合法")
    private Integer status;
} 