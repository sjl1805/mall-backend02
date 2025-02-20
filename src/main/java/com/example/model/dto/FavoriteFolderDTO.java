package com.example.model.dto.favorite;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import com.example.model.entity.FavoriteFolder;

@Data
@Schema(description = "收藏夹数据传输对象")
public class FavoriteFolderDTO {
    @Schema(description = "收藏夹ID（更新时必填）", example = "456")
    private Long id;

    @NotNull(message = "用户ID不能为空")
    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "123")
    private Long userId;

    @NotBlank(message = "收藏夹名称不能为空")
    @Size(max = 50, message = "名称最长50个字符")
    @Schema(description = "收藏夹名称", example = "我的最爱", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Size(max = 200, message = "描述最长200个字符")
    @Schema(description = "收藏夹描述", example = "常用商品收藏")
    private String description;

    @NotNull(message = "公开状态不能为空")
    @Min(0)
    @Max(1)
    @Schema(description = "是否公开：0-私有 1-公开", example = "1", defaultValue = "0")
    private Integer isPublic = 0;

    @Min(0)
    @Schema(description = "排序值", example = "100", defaultValue = "0")
    private Integer sort = 0;

    public static FavoriteFolderDTO fromEntity(FavoriteFolder folder) {
        FavoriteFolderDTO dto = new FavoriteFolderDTO();
        dto.setId(folder.getId());
        dto.setUserId(folder.getUserId());
        dto.setName(folder.getName());
        dto.setDescription(folder.getDescription());
        dto.setIsPublic(folder.getIsPublic());
        dto.setSort(folder.getSort());
        return dto;
    }

    public FavoriteFolder toEntity() {
        FavoriteFolder folder = new FavoriteFolder();
        folder.setId(this.id);
        folder.setUserId(this.userId);
        folder.setName(this.name);
        folder.setDescription(this.description);
        folder.setIsPublic(this.isPublic);
        folder.setSort(this.sort);
        return folder;
    }
} 