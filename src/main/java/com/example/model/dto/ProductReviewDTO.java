package com.example.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProductReviewDTO {
    private Long id;

    @NotNull(message = "订单ID不能为空")
    @Min(value = 1, message = "订单ID不合法")
    @Schema(description = "订单ID", example = "1")
    private Long orderId;

    @NotNull(message = "用户ID不能为空")
    @Min(value = 1, message = "用户ID不合法")
    @Schema(description = "用户ID", example = "1")
    private Long userId;

    @NotNull(message = "商品ID不能为空")
    @Min(value = 1, message = "商品ID不合法")
    @Schema(description = "商品ID", example = "1")
    private Long productId;

    @NotNull(message = "评分不能为空")
    @Min(value = 1, message = "评分范围1-5分")
    @Max(value = 5, message = "评分范围1-5分")
    @Schema(description = "评分：1-5分", example = "5")
    private Integer rating;

    @NotBlank(message = "评价内容不能为空")
    @Size(max = 500, message = "评价内容最长500字")
    @Schema(description = "评价内容", example = "这款商品非常好！")
    private String content;

    @Schema(description = "评价图片，JSON格式", example = "[\"http://example.com/image1.jpg\", \"http://example.com/image2.jpg\"]")
    private String images;

    @NotNull(message = "审核状态不能为空")
    @Min(value = 0, message = "状态值不合法")
    @Max(value = 2, message = "状态值不合法")
    @Schema(description = "审核状态：0-待审核 1-已通过 2-已拒绝", example = "1")
    private Integer status;
} 