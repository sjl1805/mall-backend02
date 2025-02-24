package com.example.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class UserBehaviorVO {
    private Long id;

    @Schema(description = "用户ID", example = "1")
    private Long userId;

    @Schema(description = "商品ID", example = "1")
    private Long productId;

    @Schema(description = "行为类型：1-浏览 2-收藏 3-购买", example = "1")
    private Integer behaviorType;

    @Schema(description = "停留时长（秒）", example = "30")
    private Integer duration;

    @Schema(description = "行为权重", example = "1.5")
    private BigDecimal weight;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime behaviorTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    // 行为类型转换
    public String getBehaviorType() {
        return switch (behaviorType) {
            case 1 -> "浏览";
            case 2 -> "收藏";
            case 3 -> "购买";
            default -> "其他行为";
        };
    }
} 