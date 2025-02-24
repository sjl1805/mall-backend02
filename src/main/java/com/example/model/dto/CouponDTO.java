package com.example.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CouponDTO {
    private Long id;

    @NotBlank(message = "优惠券名称不能为空")
    @Schema(description = "优惠券名称", example = "满减券")
    private String name;

    @NotNull(message = "优惠券类型不能为空")
    @Min(value = 1, message = "优惠券类型不合法")
    @Max(value = 2, message = "优惠券类型不合法")
    @Schema(description = "优惠券类型：1-满减券 2-折扣券", example = "1")
    private Integer type;

    @NotNull(message = "优惠券面值不能为空")
    @Schema(description = "优惠券面值", example = "20.00")
    private BigDecimal value;

    @NotNull(message = "使用门槛不能为空")
    @Schema(description = "使用门槛", example = "100.00")
    private BigDecimal minAmount;

    @NotNull(message = "生效时间不能为空")
    @Schema(description = "生效时间", example = "2023-10-01T00:00:00")
    private LocalDateTime startTime;

    @NotNull(message = "失效时间不能为空")
    @Schema(description = "失效时间", example = "2023-12-31T23:59:59")
    private LocalDateTime endTime;

    @NotNull(message = "状态不能为空")
    @Min(value = 0, message = "状态值不合法")
    @Max(value = 1, message = "状态值不合法")
    @Schema(description = "状态：0-失效 1-生效", example = "1")
    private Integer status;
} 