package com.example.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserBehaviorDTO {
    private Long id;

    @NotNull(message = "用户ID不能为空")
    @Min(value = 1, message = "用户ID不合法")
    @Schema(description = "用户ID", example = "1")
    private Long userId;

    @NotNull(message = "商品ID不能为空")
    @Min(value = 1, message = "商品ID不合法")
    @Schema(description = "商品ID", example = "1")
    private Long productId;

    @NotNull(message = "行为类型不能为空")
    @Min(value = 1, message = "行为类型范围1-3")
    @Max(value = 3, message = "行为类型范围1-3")
    @Schema(description = "行为类型：1-浏览 2-收藏 3-购买", example = "1")
    private Integer behaviorType;

    @Min(value = 0, message = "停留时长不能为负数")
    @Schema(description = "停留时长（秒）", example = "30")
    private Integer duration;

    @DecimalMin(value = "0.0", message = "权重值不能小于0")
    @Schema(description = "行为权重", example = "1.5")
    private BigDecimal weight;
} 