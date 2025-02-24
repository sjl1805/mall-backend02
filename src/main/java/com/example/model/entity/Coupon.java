package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.hibernate.validator.constraints.Range;
/**
 * 优惠券表
 *
 * @TableName coupon
 */
@TableName(value = "coupon")
@Data
@Schema(description = "优惠券实体")
public class Coupon {
    /**
     * 优惠券ID
     */
    @TableId(type = IdType.AUTO)
    @Schema(description = "优惠券ID", example = "1")
    private Long id;

    /**
     * 优惠券名称
     */
    @NotBlank(message = "优惠券名称不能为空")
    @Size(max = 32, message = "名称最长32个字符")
    @Schema(description = "优惠券名称", example = "双十一满减券")
    private String name;

    /**
     * 优惠券类型：1-满减券 2-折扣券
     */
    @NotNull(message = "类型不能为空")
    @Range(min = 1, max = 2, message = "类型参数不合法")
    @Schema(description = "类型 1-满减 2-折扣", example = "1")
    private Integer type;

    /**
     * 优惠券面值
     */
    @NotNull(message = "面值不能为空")
    @DecimalMin(value = "0.01", message = "面值不能小于0.01")
    @Schema(description = "优惠券面值", example = "100.00")
    private BigDecimal value;

    /**
     * 使用门槛
     */
    @NotNull(message = "使用门槛不能为空")
    @DecimalMin(value = "0.00", message = "门槛值不能小于0")
    @Schema(description = "最低使用金额", example = "500.00")
    private BigDecimal minAmount;

    /**
     * 生效时间
     */
    @FutureOrPresent(message = "生效时间必须大于当前时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "生效时间", example = "2024-11-01 00:00:00")
    private LocalDateTime startTime;

    /**
     * 失效时间
     */
    @Future(message = "失效时间必须大于当前时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "失效时间", example = "2024-11-12 23:59:59")
    private LocalDateTime endTime;

    /**
     * 状态：0-失效 1-生效
     */
    @Range(min = 0, max = 1, message = "状态参数不合法")
    @Schema(description = "状态 0-失效 1-生效", example = "1")
    private Integer status = 1;

    /**
     * 创建时间（带时区）
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间", example = "2024-01-01 00:00:00")
    private LocalDateTime createTime;

    /**
     * 更新时间（带时区）
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间", example = "2024-01-01 00:00:00")
    private LocalDateTime updateTime;
}