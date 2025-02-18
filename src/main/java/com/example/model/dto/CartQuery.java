package com.example.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "购物车查询条件")
public class CartQuery {
    @Schema(description = "用户ID")
    private Long userId;
    
    @Schema(description = "选中状态")
    private Integer checked;
} 