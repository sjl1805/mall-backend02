package com.example.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProductReviewVO {
    private Long id;
    private Long orderId;
    private Long userId;
    private Long productId;
    private Integer rating;
    private String content;
    private List<String> images;
    private String status;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    // 关联用户信息
    private String username;
    private String avatar;

    // 状态转换
    public String getStatus() {
        return switch (status) {
            case 0 -> "待审核";
            case 1 -> "已通过";
            case 2 -> "已拒绝";
            default -> "未知状态";
        };
    }

    // 转换图片JSON字符串为列表
    public void setImages(String images) {
        // 使用JSON工具转换，如：this.images = JSONUtil.parseArray(images, String.class);
    }
} 