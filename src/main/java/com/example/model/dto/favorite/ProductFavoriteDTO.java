package com.example.model.dto.favorite;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
@Schema(description = "商品收藏数据传输对象")
public class ProductFavoriteDTO {
    @Schema(description = "收藏ID（更新时必填）", example = "1001")
    private Long id;

    @NotNull(message = "用户ID不能为空")
    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "123")
    private Long userId;

    @NotNull(message = "商品ID不能为空")
    @Schema(description = "商品ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "456")
    private Long productId;

    @Schema(description = "收藏夹ID（空表示未分类）", example = "789")
    private Long folderId;
} 