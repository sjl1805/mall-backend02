package com.example.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.Date;

@Data
@Schema(description = "推荐商品查询条件")
public class RecommendQuery {
    @Schema(description = "推荐类型")
    private Integer type;
    
    @Schema(description = "推荐状态")
    private Integer status;
    
    @Schema(description = "开始时间")
    private Date startTime;
    
    @Schema(description = "结束时间")
    private Date endTime;
    
    @Schema(description = "算法版本")
    private String algorithmVersion;
    
    @Schema(description = "排序字段")
    private String sortField;
    
    @Schema(description = "排序方式（ASC/DESC）")
    private String sortOrder;
} 