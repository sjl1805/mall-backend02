package com.example.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单数据传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "订单信息DTO")
public class OrderDTO {
    
    @Schema(description = "订单ID")
    private Long id;
    
    @Schema(description = "订单编号")
    private String orderNo;
    
    @Schema(description = "用户ID")
    private Long userId;
    
    @Schema(description = "订单总金额")
    private BigDecimal totalAmount;
    
    @Schema(description = "订单状态：0-待支付 1-已支付 2-已发货 3-已完成 4-已取消")
    private Integer status;
    
    @Schema(description = "订单状态描述")
    private String statusDesc;
    
    @Schema(description = "收货人姓名")
    private String receiverName;
    
    @Schema(description = "收货人电话")
    private String receiverPhone;
    
    @Schema(description = "收货地址")
    private String receiverAddress;
    
    @Schema(description = "优惠金额")
    private BigDecimal discountAmount;
    
    @Schema(description = "支付时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paymentTime;
    
    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    
    @Schema(description = "订单商品列表")
    private List<OrderItemDTO> orderItems;
} 