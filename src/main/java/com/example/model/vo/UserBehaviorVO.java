package com.example.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class UserBehaviorVO {
    private Long id;
    private Long userId;
    private Long productId;
    private String behaviorType;
    private Integer duration;
    private BigDecimal weight;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime behaviorTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    // 行为类型转换
    public String getBehaviorType() {
        return switch (Integer.parseInt(behaviorType)) {
            case 1 -> "浏览";
            case 2 -> "收藏";
            case 3 -> "购买";
            default -> "其他行为";
        };
    }
} 