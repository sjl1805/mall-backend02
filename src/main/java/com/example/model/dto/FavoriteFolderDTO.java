package com.example.model.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class FavoriteFolderDTO {
    private Long id;

    @NotNull(message = "用户ID不能为空")
    @Min(value = 1, message = "用户ID不合法")
    private Long userId;

    @NotBlank(message = "收藏夹名称不能为空")
    @Size(max = 50, message = "收藏夹名称最长50个字符")
    private String name;

    @Size(max = 200, message = "收藏夹描述最长200个字符")
    private String description;

    @NotNull(message = "公开状态不能为空")
    private Integer isPublic;

    private Integer sort; // 排序字段
} 