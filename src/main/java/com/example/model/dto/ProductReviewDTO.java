package com.example.model.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProductReviewDTO {
    private Long id;

    @NotNull(message = "订单ID不能为空")
    @Min(value = 1, message = "订单ID不合法")
    private Long orderId;

    @NotNull(message = "用户ID不能为空")
    @Min(value = 1, message = "用户ID不合法")
    private Long userId;

    @NotNull(message = "商品ID不能为空")
    @Min(value = 1, message = "商品ID不合法")
    private Long productId;

    @NotNull(message = "评分不能为空")
    @Min(value = 1, message = "评分范围1-5分")
    @Max(value = 5, message = "评分范围1-5分")
    private Integer rating;

    @NotBlank(message = "评价内容不能为空")
    @Size(max = 500, message = "评价内容最长500字")
    private String content;

    private String images; // JSON格式图片地址

    @NotNull(message = "审核状态不能为空")
    @Min(value = 0, message = "状态值不合法")
    @Max(value = 2, message = "状态值不合法")
    private Integer status;
} 