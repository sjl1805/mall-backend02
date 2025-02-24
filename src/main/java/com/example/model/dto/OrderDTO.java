package com.example.model.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderDTO {
    private Long id;

    @NotBlank(message = "订单号不能为空")
    private String orderNo;

    @NotNull(message = "用户ID不能为空")
    @Min(value = 1, message = "用户ID不合法")
    private Long userId;

    @NotNull(message = "订单总金额不能为空")
    private BigDecimal totalAmount;

    @NotNull(message = "实付金额不能为空")
    private BigDecimal payAmount;

    @NotNull(message = "订单状态不能为空")
    @Min(value = 0, message = "状态值不合法")
    @Max(value = 5, message = "状态值不合法")
    private Integer status;

    @NotBlank(message = "收货人姓名不能为空")
    private String receiverName;

    @NotBlank(message = "收货人电话不能为空")
    private String receiverPhone;

    @NotBlank(message = "收货地址不能为空")
    private String receiverAddress;

    private LocalDateTime paymentTime;
    private LocalDateTime deliveryTime;
    private LocalDateTime receiveTime;

    @NotNull(message = "支付方式不能为空")
    @Min(value = 1, message = "支付方式不合法")
    @Max(value = 3, message = "支付方式不合法")
    private Integer paymentMethod;

    private String logisticsCompany;
    private String trackingNumber;

    @NotNull(message = "评价状态不能为空")
    @Min(value = 0, message = "状态值不合法")
    @Max(value = 1, message = "状态值不合法")
    private Integer commentStatus;

    private String timezone;
} 