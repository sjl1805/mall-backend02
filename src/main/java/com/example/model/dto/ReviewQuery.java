package com.example.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "评价查询条件")
public class ReviewQuery {
    @Schema(description = "商品ID")
    private Long productId;
    
    @Schema(description = "用户ID")
    private Long userId;
    
    @Schema(description = "审核状态")
    private Integer status;
    
    @Schema(description = "最低评分")
    private Integer minRating;
    
    @Schema(description = "最高评分")
    private Integer maxRating;
    
    @Schema(description = "排序字段")
    private String sortField;
    
    @Schema(description = "排序方式（ASC/DESC）")
    private String sortOrder;
} 