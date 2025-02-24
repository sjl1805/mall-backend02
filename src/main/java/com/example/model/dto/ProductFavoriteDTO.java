package com.example.model.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProductFavoriteDTO {
    private Long id;

    @NotNull(message = "用户ID不能为空")
    @Min(value = 1, message = "用户ID不合法")
    private Long userId;

    @NotNull(message = "商品ID不能为空")
    @Min(value = 1, message = "商品ID不合法")
    private Long productId;

    private Long folderId; // 收藏夹ID（可为空）
} 