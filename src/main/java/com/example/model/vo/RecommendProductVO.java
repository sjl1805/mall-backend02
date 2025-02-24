package com.example.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RecommendProductVO {
    private Long id;
    private Long productId;
    private String type;
    private Integer sort;
    private String status;
    private String algorithmVersion;
    private String recommendReason;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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