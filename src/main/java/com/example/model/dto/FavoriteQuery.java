package com.example.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "收藏查询条件")
public class FavoriteQuery {
    @Schema(description = "用户ID")
    private Long userId;
    
    @Schema(description = "商品ID")
    private Long productId;
    
    @Schema(description = "收藏夹ID")
    private Long folderId;
    
    @Schema(description = "排序字段")
    private String sortField;
    
    @Schema(description = "排序方式（ASC/DESC）")
    private String sortOrder;
} 