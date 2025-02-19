package com.example.model.dto.coupon;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Schema(description = "优惠券数据传输对象")
public class CouponDTO {
    @Schema(description = "优惠券ID（更新时必填）")
    private Long id;

    @NotBlank(message = "优惠券名称不能为空")
    @Size(max = 50, message = "名称最长50个字符")
    @Schema(description = "优惠券名称", example = "新用户专享", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotNull(message = "优惠券类型不能为空")
    @Min(1) @Max(3)
    @Schema(description = "优惠券类型：1-满减券 2-折扣券 3-运费券", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer type;

    @NotNull(message = "优惠值不能为空")
    @DecimalMin(value = "0.01", message = "优惠值最小为0.01")
    @Schema(description = "优惠值（满减金额/折扣率）", example = "50.00", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal value;

    @DecimalMin(value = "0.00", message = "最低消费金额不能为负")
    @Schema(description = "最低消费金额（仅满减券需要）", example = "100.00")
    private BigDecimal minAmount;

    @NotNull(message = "开始时间不能为空")
    @FutureOrPresent(message = "开始时间必须大于等于当前时间")
    @Schema(description = "生效时间", example = "2025-03-01 00:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date startTime;

    @NotNull(message = "结束时间不能为空")
    @Future(message = "结束时间必须大于当前时间")
    @Schema(description = "过期时间", example = "2025-03-31 23:59:59", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date endTime;

    @Min(value = 0, message = "状态参数错误")
    @Max(value = 1, message = "状态参数错误")
    @Schema(description = "状态：0-失效 1-生效", defaultValue = "1")
    private Integer status = 1;
} 