package com.example.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RecommendProductDTO {
    private Long id;

    @NotNull(message = "商品ID不能为空")
    @Min(value = 1, message = "商品ID不合法")
    @Schema(description = "商品ID", example = "1")
    private Long productId;

    @NotNull(message = "推荐类型不能为空")
    @Min(value = 1, message = "推荐类型范围1-3")
    @Max(value = 3, message = "推荐类型范围1-3")
    @Schema(description = "推荐类型：1-类型A 2-类型B 3-类型C", example = "1")
    private Integer type;

    @Min(value = 0, message = "排序值不能为负数")
    @Schema(description = "排序值", example = "1")
    private Integer sort;

    @NotNull(message = "状态不能为空")
    @Min(value = 0, message = "状态值不合法")
    @Max(value = 1, message = "状态值不合法")
    @Schema(description = "状态：0禁用 1启用", example = "1")
    private Integer status;

    @FutureOrPresent(message = "开始时间不能早于当前时间")
    @Schema(description = "推荐开始时间", example = "2023-10-01T00:00:00")
    private LocalDateTime startTime;

    @Future(message = "结束时间必须晚于当前时间")
    @Schema(description = "推荐结束时间", example = "2023-12-31T23:59:59")
    private LocalDateTime endTime;

    @Size(max = 50, message = "算法版本最长50个字符")
    @Schema(description = "推荐算法版本", example = "v1.0")
    private String algorithmVersion;

    @Size(max = 100, message = "推荐理由最长100个字符")
    @Schema(description = "推荐理由", example = "根据用户历史购买记录推荐")
    private String recommendReason;
} 