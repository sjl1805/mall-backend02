package com.example.model.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Schema(description = "SKU查询条件")
public class SkuQuery {
    @Schema(description = "商品ID")
    private Long productId;
    
    @Schema(description = "最低价格")
    private BigDecimal minPrice;
    
    @Schema(description = "最高价格")
    private BigDecimal maxPrice;
    
    @Schema(description = "SKU状态")
    private Integer status;
    
    @Schema(description = "排序字段")
    private String sortField;
    
    @Schema(description = "排序方式（ASC/DESC）")
    private String sortOrder;
} 