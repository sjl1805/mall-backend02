package com.example.model.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.URL;
import com.example.model.entity.Category;
/**
 * 分类数据传输对象
 *
 * @author 毕业设计学生
 */
@Data
@Schema(description = "分类数据传输对象")
public class CategoryDTO {
    @Schema(description = "分类ID（更新时必填）", example = "1")
    private Long id;

    @NotNull(message = "父分类ID不能为空")
    @Schema(description = "父分类ID（0表示一级分类）", example = "0", defaultValue = "0")
    private Long parentId = 0L;

    @NotBlank(message = "分类名称不能为空")
    @Size(min = 2, max = 20, message = "分类名称长度2-20个字符")
    @Schema(description = "分类名称", example = "手机数码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @URL(message = "图标地址格式不正确")
    @Schema(description = "分类图标地址", example = "https://example.com/icon.png")
    private String icon;

    @NotNull(message = "层级不能为空")
    @Min(value = 1, message = "层级范围1-3")
    @Max(value = 3, message = "层级范围1-3")
    @Schema(description = "层级：1-一级 2-二级 3-三级", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer level;

    @Min(value = 0, message = "排序值最小为0")
    @Schema(description = "排序值", example = "10", defaultValue = "0")
    private Integer sort = 0;

    @NotNull(message = "状态不能为空")
    @Min(value = 0, message = "状态参数错误")
    @Max(value = 1, message = "状态参数错误")
    @Schema(description = "状态：0-禁用 1-启用", example = "1", defaultValue = "1")
    private Integer status = 1;

    public static CategoryDTO fromEntity(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setParentId(category.getParentId());
        dto.setName(category.getName());
        dto.setIcon(category.getIcon());
        dto.setLevel(category.getLevel());
        dto.setSort(category.getSort());
        dto.setStatus(category.getStatus());
        return dto;
    }

    public Category toEntity() {
        Category category = new Category();
        category.setId(this.id);
        category.setParentId(this.parentId);
        category.setName(this.name);
        category.setIcon(this.icon);
        category.setLevel(this.level);
        category.setSort(this.sort);
        category.setStatus(this.status);
        return category;
    }
} 