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
public class OrderDTO {
    private Long id;

    @NotBlank(message = "订单号不能为空")
    @Schema(description = "订单号", example = "ORD123456")
    private String orderNo;

    @NotNull(message = "用户ID不能为空")
    @Min(value = 1, message = "用户ID不合法")
    @Schema(description = "用户ID", example = "1")
    private Long userId;

    @NotNull(message = "订单总金额不能为空")
    @Schema(description = "订单总金额", example = "99.99")
    private BigDecimal totalAmount;

    @NotNull(message = "实付金额不能为空")
    @Schema(description = "实付金额", example = "89.99")
    private BigDecimal payAmount;

    @NotNull(message = "订单状态不能为空")
    @Min(value = 0, message = "状态值不合法")
    @Max(value = 5, message = "状态值不合法")
    @Schema(description = "订单状态：0-待支付 1-已支付 2-已发货 3-已完成 4-已取消 5-退款中", example = "1")
    private Integer status;

    @NotBlank(message = "收货人姓名不能为空")
    @Schema(description = "收货人姓名", example = "张三")
    private String receiverName;

    @NotBlank(message = "收货人电话不能为空")
    @Schema(description = "收货人电话", example = "13800138000")
    private String receiverPhone;

    @NotBlank(message = "收货地址不能为空")
    @Schema(description = "收货地址", example = "广东省广州市天河区天汇大厦")
    private String receiverAddress;

    @Schema(description = "支付时间", example = "2023-10-01T10:00:00")
    private LocalDateTime paymentTime;

    @Schema(description = "发货时间", example = "2023-10-02T10:00:00")
    private LocalDateTime deliveryTime;

    @Schema(description = "收货时间", example = "2023-10-03T10:00:00")
    private LocalDateTime receiveTime;

    @NotNull(message = "支付方式不能为空")
    @Min(value = 1, message = "支付方式不合法")
    @Max(value = 3, message = "支付方式不合法")
    @Schema(description = "支付方式：1-支付宝 2-微信 3-银联", example = "1")
    private Integer paymentMethod;

    @Schema(description = "物流公司", example = "顺丰快递")
    private String logisticsCompany;

    @Schema(description = "运单号", example = "SF123456789")
    private String trackingNumber;

    @NotNull(message = "评价状态不能为空")
    @Min(value = 0, message = "状态值不合法")
    @Max(value = 1, message = "状态值不合法")
    @Schema(description = "评价状态：0未评价 1已评价", example = "0")
    private Integer commentStatus;

    @Schema(description = "时区信息", example = "GMT+8")
    private String timezone;
} 