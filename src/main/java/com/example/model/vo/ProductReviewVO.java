package com.example.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProductReviewVO {
    private Long id;

    @Schema(description = "订单ID", example = "1")
    private Long orderId;

    @Schema(description = "用户ID", example = "1")
    private Long userId;

    @Schema(description = "商品ID", example = "1")
    private Long productId;

    @Schema(description = "评分：1-5分", example = "5")
    private Integer rating;

    @Schema(description = "评价内容", example = "这款商品非常好！")
    private String content;

    @Schema(description = "评价图片，JSON格式", example = "[\"http://example.com/image1.jpg\", \"http://example.com/image2.jpg\"]")
    private List<String> images;

    @Schema(description = "审核状态：0-待审核 1-已通过 2-已拒绝", example = "1")
    private Integer status;

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