package com.example.model.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CouponDTO {
    private Long id;

    @NotBlank(message = "优惠券名称不能为空")
    private String name;

    @NotNull(message = "优惠券类型不能为空")
    @Min(value = 1, message = "优惠券类型不合法")
    @Max(value = 2, message = "优惠券类型不合法")
    private Integer type;

    @NotNull(message = "优惠券面值不能为空")
    private BigDecimal value;

    @NotNull(message = "使用门槛不能为空")
    private BigDecimal minAmount;

    @NotNull(message = "生效时间不能为空")
    private LocalDateTime startTime;

    @NotNull(message = "失效时间不能为空")
    private LocalDateTime endTime;

    @NotNull(message = "状态不能为空")
    @Min(value = 0, message = "状态值不合法")
    @Max(value = 1, message = "状态值不合法")
    private Integer status;
} 