package com.example.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * 创建订单数据传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "创建订单DTO")
public class OrderCreateDTO {
    
    @Schema(description = "收货地址ID", required = true)
    @NotNull(message = "地址ID不能为空")
    private Long addressId; // 收货地址ID
    
    @Schema(description = "优惠券ID")
    private Long couponId; // 优惠券ID
    
    @Schema(description = "订单商品列表", required = true)
    @NotEmpty(message = "订单商品不能为空")
    @Size(min = 1, message = "订单至少需要一件商品")
    @Valid
    private List<OrderItemCreateDTO> items; // 订单商品
    
    @Schema(description = "支付方式，如：alipay, wechat", example = "alipay")
    private String paymentType; // 支付方式
    
    @Schema(description = "订单备注", example = "请尽快发货")
    @Size(max = 200, message = "备注不能超过200个字符")
    private String orderNote; // 订单备注
} 