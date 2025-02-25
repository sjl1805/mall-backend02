package com.example.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 优惠券数据传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "优惠券信息DTO")
public class CouponDTO {
    
    @Schema(description = "优惠券ID")
    private Long id;
    
    @Schema(description = "优惠券名称")
    private String name;
    
    @Schema(description = "类型：1-满减 2-折扣 3-无门槛")
    private Integer type;
    
    @Schema(description = "类型描述文字")
    private String typeDesc;
    
    @Schema(description = "优惠金额/折扣率")
    private BigDecimal amount;
    
    @Schema(description = "使用门槛金额")
    private BigDecimal threshold;
    
    @Schema(description = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    
    @Schema(description = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    
    @Schema(description = "总数量")
    private Integer total;
    
    @Schema(description = "剩余数量")
    private Integer remain;
    
    @Schema(description = "状态：0-禁用 1-启用")
    private Integer status;
    
    @Schema(description = "是否已过期")
    private Boolean expired;
    
    @Schema(description = "是否可用")
    private Boolean available;
    
    @Schema(description = "优惠券描述")
    private String description;
} 