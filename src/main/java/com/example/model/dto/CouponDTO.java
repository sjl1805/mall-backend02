package com.example.model.dto;

import lombok.Data;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 优惠券数据传输对象
 */
@Data
public class CouponDTO {

    /**
     * 优惠券ID
     */
    private Long id;

    /**
     * 优惠券名称
     */
    @NotBlank(message = "优惠券名称不能为空")
    private String name;

    /**
     * 类型：1-满减 2-折扣 3-无门槛
     */
    @NotNull(message = "优惠券类型不能为空")
    @Min(value = 1, message = "优惠券类型值无效")
    @Max(value = 3, message = "优惠券类型值无效")
    private Integer type;

    /**
     * 优惠金额/折扣率
     */
    @NotNull(message = "优惠金额/折扣率不能为空")
    @DecimalMin(value = "0.01", message = "优惠金额/折扣率必须大于0")
    private BigDecimal amount;

    /**
     * 使用门槛金额
     */
    @NotNull(message = "使用门槛金额不能为空")
    @PositiveOrZero(message = "使用门槛金额不能为负数")
    private BigDecimal threshold;

    /**
     * 开始时间
     */
    @NotNull(message = "开始时间不能为空")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @NotNull(message = "结束时间不能为空")
    @Future(message = "结束时间必须是将来时间")
    private LocalDateTime endTime;

    /**
     * 总数量
     */
    @NotNull(message = "总数量不能为空")
    @Min(value = 1, message = "总数量必须大于0")
    private Integer total;

    /**
     * 分类限制
     */
    private List<Long> categoryLimit;

    /**
     * 商品限制
     */
    private List<Long> productLimit;

    /**
     * 状态：0-禁用 1-启用
     */
    private Integer status;
} 