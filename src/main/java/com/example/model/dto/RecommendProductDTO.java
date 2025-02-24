package com.example.model.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RecommendProductDTO {
    private Long id;

    @NotNull(message = "商品ID不能为空")
    @Min(value = 1, message = "商品ID不合法")
    private Long productId;

    @NotNull(message = "推荐类型不能为空")
    @Min(value = 1, message = "推荐类型范围1-3")
    @Max(value = 3, message = "推荐类型范围1-3")
    private Integer type;

    @Min(value = 0, message = "排序值不能为负数")
    private Integer sort;

    @NotNull(message = "状态不能为空")
    @Min(value = 0, message = "状态值不合法")
    @Max(value = 1, message = "状态值不合法")
    private Integer status;

    @FutureOrPresent(message = "开始时间不能早于当前时间")
    private LocalDateTime startTime;

    @Future(message = "结束时间必须晚于当前时间")
    private LocalDateTime endTime;

    @Size(max = 50, message = "算法版本最长50个字符")
    private String algorithmVersion;

    @Size(max = 100, message = "推荐理由最长100个字符")
    private String recommendReason;
} 