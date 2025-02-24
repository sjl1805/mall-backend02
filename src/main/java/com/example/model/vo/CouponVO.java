package com.example.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CouponVO {
    private Long id;

    @Schema(description = "优惠券名称", example = "满减券")
    private String name;

    @Schema(description = "优惠券类型：1-满减券 2-折扣券", example = "1")
    private Integer type;

    @Schema(description = "优惠券面值", example = "20.00")
    private BigDecimal value;

    @Schema(description = "优惠券数量", example = "100")
    private Integer num;

    @Schema(description = "使用门槛", example = "100.00")
    private BigDecimal minAmount;

    @Schema(description = "生效时间", example = "2023-10-01T00:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @Schema(description = "失效时间", example = "2023-12-31T23:59:59")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    @Schema(description = "状态：0-失效 1-生效", example = "1")
    private Integer status;

    // 状态转换
    public String getStatus() {
        return switch (status) {
            case 0 -> "失效";
            case 1 -> "生效";
            default -> "未知状态";
        };
    }
} 