package com.example.model.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import com.example.model.entity.RecommendProduct;
import java.time.LocalDateTime;

@Data
@Schema(description = "推荐商品数据传输对象")
public class RecommendProductDTO {
    @Schema(description = "推荐ID（更新时必填）", example = "1")
    private Long id;

    @NotNull(message = "商品ID不能为空")
    @Schema(description = "商品ID", example = "1001", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long productId;

    @NotNull(message = "推荐类型不能为空")
    @Min(1)
    @Max(5)
    @Schema(description = "推荐类型（1-首页推荐 2-猜你喜欢 3-热销推荐）", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer type;

    @NotNull(message = "排序值不能为空")
    @Min(0)
    @Schema(description = "排序值", example = "100", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer sort;

    @NotNull(message = "开始时间不能为空")
    @FutureOrPresent(message = "开始时间必须大于等于当前时间")
    @Schema(description = "推荐开始时间", example = "2025-03-01 00:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime startTime;

    @NotNull(message = "结束时间不能为空")
    @Future(message = "结束时间必须大于当前时间")
    @Schema(description = "推荐结束时间", example = "2025-03-31 23:59:59", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime endTime;

    @NotBlank(message = "推荐原因不能为空")
    @Size(max = 200, message = "推荐原因最多200个字符")
    @Schema(description = "推荐原因", example = "热销商品", requiredMode = Schema.RequiredMode.REQUIRED)
    private String recommendReason;

    @Schema(description = "算法版本号", example = "v2.3.1")
    private String algorithmVersion;

    public static RecommendProductDTO fromEntity(RecommendProduct recommend) {
        RecommendProductDTO dto = new RecommendProductDTO();
        dto.setId(recommend.getId());
        dto.setProductId(recommend.getProductId());
        dto.setType(recommend.getType());
        dto.setSort(recommend.getSort());
        dto.setStartTime(recommend.getStartTime());
        dto.setEndTime(recommend.getEndTime());
        dto.setRecommendReason(recommend.getRecommendReason());
        dto.setAlgorithmVersion(recommend.getAlgorithmVersion());
        return dto;
    }

    public RecommendProduct toEntity() {
        RecommendProduct recommend = new RecommendProduct();
        recommend.setId(this.id);
        recommend.setProductId(this.productId);
        recommend.setType(this.type);
        recommend.setSort(this.sort);
        recommend.setStartTime(this.startTime);
        recommend.setEndTime(this.endTime);
        recommend.setRecommendReason(this.recommendReason);
        recommend.setAlgorithmVersion(this.algorithmVersion);
        return recommend;
    }
} 