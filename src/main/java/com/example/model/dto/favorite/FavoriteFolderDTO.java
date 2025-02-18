package com.example.model.dto.favorite;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.*;

@Data
@Schema(description = "收藏夹数据传输对象")
public class FavoriteFolderDTO {
    @Schema(description = "收藏夹ID（更新时必填）", example = "456")
    private Long id;

    @NotNull(message = "用户ID不能为空")
    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "123")
    private Long userId;

    @NotBlank(message = "收藏夹名称不能为空")
    @Size(max = 32, message = "名称最长32个字符")
    @Schema(description = "收藏夹名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "我的收藏")
    private String name;

    @Size(max = 128, message = "描述最长128个字符")
    @Schema(description = "收藏夹描述", example = "常用商品收藏")
    private String description;

    @Min(0) @Max(1)
    @Schema(description = "公开状态：0-私密 1-公开", defaultValue = "0", example = "1")
    private Integer isPublic = 0;

    @Min(value = 0, message = "排序值不能为负数")
    @Schema(description = "排序值", defaultValue = "0", example = "1")
    private Integer sort = 0;
} 