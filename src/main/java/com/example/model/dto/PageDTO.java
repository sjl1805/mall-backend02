package com.example.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "分页参数DTO")
public class PageDTO<T> {
    
    @Schema(description = "查询参数")
    private T query;

    @Schema(description = "当前页码", example = "1")
    private Integer current = 1;

    @Schema(description = "每页数量", example = "10")
    private Integer size = 10;

    @Schema(description = "总记录数", example = "100")
    private Long total;
    
    @Schema(description = "总页数", example = "10")
    private Long pages;
}
