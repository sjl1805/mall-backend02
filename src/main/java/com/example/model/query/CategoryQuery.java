package com.example.model.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "分类查询条件")
public class CategoryQuery {
    @Schema(description = "分类名称")
    private String name;
    
    @Schema(description = "状态")
    private Integer status;
    
    @Schema(description = "层级")
    private Integer level;
    
    @Schema(description = "父分类ID")
    private Long parentId;
    
    @Schema(description = "排序字段")
    private String sortField;
    
    @Schema(description = "排序方式（ASC/DESC）")
    private String sortOrder;
} 