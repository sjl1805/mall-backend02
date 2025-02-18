package com.example.model.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.*;
import java.util.Date;

@Data
@Schema(description = "推荐商品数据传输对象")
public class RecommendProductDTO {
    @Schema(description = "推荐ID（更新时必填）", example = "1001")
    private Long id;

    @NotNull(message = "商品ID不能为空")
    @Schema(description = "商品ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "456")
    private Long productId;

    @NotNull(message = "推荐类型不能为空")
    @Min(1) @Max(2)
    @Schema(description = "推荐类型：1-热门商品 2-新品推荐", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer type;

    @NotNull(message = "排序值不能为空")
    @Min(value = 0, message = "排序值最小为0")
    @Schema(description = "排序值", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    private Integer sort;

    @NotNull(message = "状态不能为空")
    @Min(0) @Max(1)
    @Schema(description = "推荐状态：0-未生效 1-生效中", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @NotNull(message = "开始时间不能为空")
    @Schema(description = "推荐开始时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "2023-08-01 00:00:00")
    private Date startTime;

    @NotNull(message = "结束时间不能为空")
    @Schema(description = "推荐结束时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "2023-08-31 23:59:59")
    private Date endTime;

    @NotBlank(message = "算法版本不能为空")
    @Schema(description = "算法版本号", requiredMode = Schema.RequiredMode.REQUIRED, example = "v2.3.1")
    private String algorithmVersion;

    @Size(max = 500, message = "推荐理由最长500个字符")
    @Schema(description = "推荐理由", example = "本月热销TOP10商品")
    private String recommendReason;
} 