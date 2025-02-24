package com.example.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RecommendProductVO {
    private Long id;

    @Schema(description = "商品ID", example = "1")
    private Long productId;

    @Schema(description = "推荐类型", example = "热门商品")
    private String type;

    @Schema(description = "排序值", example = "1")
    private Integer sort;

    @Schema(description = "状态", example = "生效中")
    private String status;

    @Schema(description = "算法版本", example = "v1.0")
    private String algorithmVersion;

    @Schema(description = "推荐理由", example = "根据用户历史购买记录推荐")
    private String recommendReason;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "推荐开始时间", example = "2023-10-01T00:00:00")
    private LocalDateTime startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "推荐结束时间", example = "2023-12-31T23:59:59")
    private LocalDateTime endTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间", example = "2023-09-30T10:00:00")
    private LocalDateTime createTime;

    // 推荐类型转换
    public String getType() {
        return switch (Integer.parseInt(type)) {
            case 1 -> "热门商品";
            case 2 -> "新品推荐";
            case 3 -> "算法推荐";
            default -> "其他类型";
        };
    }

    // 状态转换
    public String getStatus() {
        return "1".equals(status) ? "生效中" : "未生效";
    }
} 