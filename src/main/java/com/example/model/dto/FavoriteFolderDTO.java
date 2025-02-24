package com.example.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FavoriteFolderDTO {
    private Long id;

    @NotNull(message = "用户ID不能为空")
    @Min(value = 1, message = "用户ID不合法")
    @Schema(description = "用户ID", example = "1")
    private Long userId;

    @NotBlank(message = "收藏夹名称不能为空")
    @Size(max = 50, message = "收藏夹名称最长50个字符")
    @Schema(description = "收藏夹名称", example = "我的收藏夹")
    private String name;

    @Size(max = 200, message = "收藏夹描述最长200个字符")
    @Schema(description = "收藏夹描述", example = "这是我的私人收藏夹")
    private String description;

    @NotNull(message = "公开状态不能为空")
    @Schema(description = "公开状态：0-私密 1-公开", example = "1")
    private Integer isPublic;

    @Schema(description = "排序字段", example = "1")
    private Integer sort; // 排序字段
} 