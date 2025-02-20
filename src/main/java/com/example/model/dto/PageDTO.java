package com.example.model.dto;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
@Data
public class PageDTO<T> {
    @Schema(description = "页码", example = "1")
    private Integer page;

    @Schema(description = "每页数量", example = "10")
    private Integer size;
}
