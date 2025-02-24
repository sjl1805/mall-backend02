package com.example.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductFavoriteDTO {
    private Long id;

    @NotNull(message = "用户ID不能为空")
    @Min(value = 1, message = "用户ID不合法")
    @Schema(description = "用户ID", example = "1")
    private Long userId;

    @NotNull(message = "商品ID不能为空")
    @Min(value = 1, message = "商品ID不合法")
    @Schema(description = "商品ID", example = "1")
    private Long productId;

    @Schema(description = "收藏夹ID（NULL表示未分类）", example = "1")
    private Long folderId; // 收藏夹ID（可为空）
} 