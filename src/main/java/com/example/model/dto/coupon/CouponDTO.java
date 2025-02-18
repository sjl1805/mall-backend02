package com.example.model.dto.coupon;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "优惠券数据传输对象")
public class CouponDTO {
    @Schema(description = "优惠券ID（更新时必填）")
    private Long id;

    @NotBlank(message = "优惠券名称不能为空")
    @Size(max = 32, message = "名称最长32个字符")
    @Schema(description = "优惠券名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotNull(message = "优惠券类型不能为空")
    @Min(value = 1, message = "类型参数错误")
    @Max(value = 2, message = "类型参数错误")
    @Schema(description = "类型：1-满减券 2-折扣券", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer type;

    @NotNull(message = "优惠值不能为空")
    @DecimalMin(value = "0.01", message = "优惠值必须大于0")
    @Schema(description = "优惠值（满减金额/折扣比例）", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal value;

    @NotNull(message = "使用门槛不能为空")
    @DecimalMin(value = "0.00", message = "门槛金额不能为负")
    @Schema(description = "最低使用金额", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal minAmount;

    @NotNull(message = "生效时间不能为空")
    @Schema(description = "生效时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime startTime;

    @NotNull(message = "失效时间不能为空")
    @Schema(description = "失效时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime endTime;

    @Min(value = 0, message = "状态参数错误")
    @Max(value = 1, message = "状态参数错误")
    @Schema(description = "状态：0-失效 1-生效", defaultValue = "1")
    private Integer status = 1;
} 