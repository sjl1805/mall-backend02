package com.example.model.dto.favorite;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import com.example.model.entity.ProductFavorite;

@Data
@Schema(description = "商品收藏数据传输对象")
public class ProductFavoriteDTO {
    @Schema(description = "收藏ID（更新时必填）", example = "1001")
    private Long id;

    @NotNull(message = "用户ID不能为空")
    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "123")
    private Long userId;

    @NotNull(message = "商品ID不能为空")
    @Schema(description = "商品ID", example = "1001", requiredMode = RequiredMode.REQUIRED)
    private Long productId;

    @NotNull(message = "收藏夹ID不能为空")
    @Schema(description = "收藏夹ID", example = "1", requiredMode = RequiredMode.REQUIRED)
    private Long folderId;


    public static ProductFavoriteDTO fromEntity(ProductFavorite favorite) {
        ProductFavoriteDTO dto = new ProductFavoriteDTO();
        dto.setId(favorite.getId());
        dto.setUserId(favorite.getUserId());
        dto.setProductId(favorite.getProductId());
        dto.setFolderId(favorite.getFolderId());
        return dto;
    }

    public ProductFavorite toEntity() {
        ProductFavorite favorite = new ProductFavorite();
        favorite.setId(this.id);
        favorite.setUserId(this.userId);
        favorite.setProductId(this.productId);
        favorite.setFolderId(this.folderId);
        return favorite;
    }
} 