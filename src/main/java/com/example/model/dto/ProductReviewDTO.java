package com.example.model.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import com.example.model.entity.ProductReview;

@Data
@Schema(description = "商品评价数据传输对象")
public class ProductReviewDTO {
    @Schema(description = "评价ID（更新时必填）", example = "1")
    private Long id;

    @NotNull(message = "订单ID不能为空")
    @Schema(description = "关联订单ID", example = "10001", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long orderId;

    @NotNull(message = "用户ID不能为空")
    @Schema(description = "用户ID", example = "20001", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long userId;

    @NotNull(message = "商品ID不能为空")
    @Schema(description = "商品ID", example = "3001", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long productId;

    @NotNull(message = "评分不能为空")
    @Min(value = 1, message = "评分最低1分")
    @Max(value = 5, message = "评分最高5分")
    @Schema(description = "评分（1-5分）", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer rating;

    @NotBlank(message = "评价内容不能为空")
    @Size(max = 500, message = "评价内容最多500个字符")
    @Schema(description = "评价内容", example = "商品质量非常好", requiredMode = Schema.RequiredMode.REQUIRED)
    private String content;

    @Schema(description = "评价图片（JSON数组）", example = "['/images/review1.jpg','/images/review2.jpg']")
    private String images;

    @Min(0)
    @Max(2)
    @Schema(description = "审核状态：0-待审核 1-已通过 2-已拒绝", example = "0")
    private Integer status;

    public static ProductReviewDTO fromEntity(ProductReview review) {
        ProductReviewDTO dto = new ProductReviewDTO();
        dto.setId(review.getId());
        dto.setOrderId(review.getOrderId());
        dto.setUserId(review.getUserId());
        dto.setProductId(review.getProductId());
        dto.setRating(review.getRating());
        dto.setContent(review.getContent());
        dto.setImages(review.getImages());
        dto.setStatus(review.getStatus());
        return dto;
    }

    public ProductReview toEntity() {
        ProductReview review = new ProductReview();
        review.setId(this.id);
        review.setOrderId(this.orderId);
        review.setUserId(this.userId);
        review.setProductId(this.productId);
        review.setRating(this.rating);
        review.setContent(this.content);
        review.setImages(this.images);
        review.setStatus(this.status);
        return review;
    }
} 