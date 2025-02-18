package com.example.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Schema(description = "商品查询条件")
public class ProductQuery {
    @Schema(description = "关键词（商品名称/描述）")
    private String keyword;
    
    @Schema(description = "分类ID")
    private Long categoryId;
    
    @Schema(description = "最低价格")
    private BigDecimal minPrice;
    
    @Schema(description = "最高价格")
    private BigDecimal maxPrice;
    
    @Schema(description = "商品状态")
    private Integer status;
    
    @Schema(description = "开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startTime;
    
    @Schema(description = "结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;
    
    @Schema(description = "排序字段")
    private String sortField;
    
    @Schema(description = "排序方式（ASC/DESC）")
    private String sortOrder;
} 