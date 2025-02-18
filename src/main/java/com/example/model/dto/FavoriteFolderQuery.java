package com.example.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "收藏夹查询条件")
public class FavoriteFolderQuery {
    @Schema(description = "收藏夹名称")
    private String name;
    
    @Schema(description = "公开状态")
    private Integer isPublic;
    
    @Schema(description = "用户ID")
    private Long userId;
    
    @Schema(description = "排序字段")
    private String sortField;
    
    @Schema(description = "排序方式（ASC/DESC）")
    private String sortOrder;
} 