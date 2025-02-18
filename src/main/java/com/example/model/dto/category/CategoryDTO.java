package com.example.model.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.*;

@Data
@Schema(description = "分类数据传输对象")
public class CategoryDTO {
    @Schema(description = "分类ID（更新时必填）")
    private Long id;

    @Schema(description = "父分类ID（0表示一级分类）", defaultValue = "0")
    private Long parentId = 0L;

    @NotBlank(message = "分类名称不能为空")
    @Size(max = 20, message = "分类名称最长20个字符")
    @Schema(description = "分类名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "分类图标地址")
    private String icon;

    @NotNull(message = "层级不能为空")
    @Min(value = 1, message = "层级范围1-3")
    @Max(value = 3, message = "层级范围1-3")
    @Schema(description = "层级：1-一级 2-二级 3-三级", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer level;

    @Min(value = 0, message = "排序值最小为0")
    @Schema(description = "排序值", defaultValue = "0")
    private Integer sort = 0;

    @Min(value = 0, message = "状态参数错误")
    @Max(value = 1, message = "状态参数错误")
    @Schema(description = "状态：0-禁用 1-启用", defaultValue = "1")
    private Integer status = 1;
} 