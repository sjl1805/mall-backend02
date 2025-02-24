package com.example.model.dto;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PageDTO<T> {
    @Schema(description = "当前页", example = "1")
    private Integer current = 1;    // 当前页，默认第一页

    @Schema(description = "每页数量", example = "10")
    private Integer size = 10;      // 每页数量，默认10条

    @Schema(description = "排序字段", example = "create_time")
    private String orderBy;

    @Schema(description = "排序方向", example = "desc")
    private String direction;

    @Schema(description = "查询条件")
    private T query;

    public <E> Page<E> toPage() {
        return new Page<>(current, size);
    }
}
