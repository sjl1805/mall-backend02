package com.example.model.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.*;

@Data
@Schema(description = "商品评价数据传输对象")
public class ProductReviewDTO {
    @Schema(description = "评价ID（更新时必填）", example = "789")
    private Long id;

    @NotNull(message = "订单ID不能为空")
    @Schema(description = "订单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1001")
    private Long orderId;

    @NotNull(message = "用户ID不能为空")
    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "456")
    private Long userId;

    @NotNull(message = "商品ID不能为空")
    @Schema(description = "商品ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "123")
    private Long productId;

    @NotNull(message = "评分不能为空")
    @Min(1) @Max(5)
    @Schema(description = "评分：1-5分", requiredMode = Schema.RequiredMode.REQUIRED, example = "5")
    private Integer rating;

    @Size(max = 500, message = "评价内容最长500个字符")
    @Schema(description = "评价内容", example = "商品质量非常好，物流速度快")
    private String content;

    @Schema(description = "评价图片（JSON数组）", 
          example = "[\"https://example.com/image1.jpg\",\"https://example.com/image2.jpg\"]")
    private String images;

    @Min(0) @Max(2)
    @Schema(description = "审核状态：0-待审核 1-已通过 2-已拒绝", example = "0")
    private Integer status;
} 