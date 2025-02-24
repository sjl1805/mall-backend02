package com.example.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class FavoriteFolderVO {
    private Long id;

    @Schema(description = "用户ID", example = "1")
    private Long userId;

    @Schema(description = "收藏夹名称", example = "我的收藏夹")
    private String name;

    @Schema(description = "收藏夹描述", example = "这是我的私人收藏夹")
    private String description;

    @Schema(description = "公开状态：0-私密 1-公开", example = "1")
    private Integer isPublic;

    @Schema(description = "收藏项数量", example = "10")
    private Integer itemCount; // 收藏项数量

    @Schema(description = "排序字段", example = "1")
    private Integer sort; // 排序字段
} 