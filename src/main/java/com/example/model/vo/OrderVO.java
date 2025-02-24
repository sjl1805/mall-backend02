package com.example.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderVO {
    private Long id;

    @Schema(description = "订单号", example = "ORD123456")
    private String orderNo;

    @Schema(description = "用户ID", example = "1")
    private Long userId;

    @Schema(description = "订单总金额", example = "99.99")
    private BigDecimal totalAmount;

    @Schema(description = "实付金额", example = "89.99")
    private BigDecimal payAmount;

    @Schema(description = "订单状态：0-待支付 1-已支付 2-已发货 3-已完成 4-已取消 5-退款中", example = "1")
    private Integer status;

    @Schema(description = "收货人姓名", example = "张三")
    private String receiverName;

    @Schema(description = "收货人电话", example = "13800138000")
    private String receiverPhone;

    @Schema(description = "收货地址", example = "广东省广州市天河区天汇大厦")
    private String receiverAddress;

    @Schema(description = "支付时间", example = "2023-10-01T10:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paymentTime;

    @Schema(description = "发货时间", example = "2023-10-02T10:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deliveryTime;

    @Schema(description = "收货时间", example = "2023-10-03T10:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime receiveTime;

    @Schema(description = "支付方式：1-支付宝 2-微信 3-银联", example = "1")
    private Integer paymentMethod;

    @Schema(description = "物流公司", example = "顺丰快递")
    private String logisticsCompany;

    @Schema(description = "运单号", example = "SF123456789")
    private String trackingNumber;

    @Schema(description = "评价状态：0未评价 1已评价", example = "0")
    private Integer commentStatus;

    @Schema(description = "时区信息", example = "GMT+8")
    private String timezone;

    // 状态转换
    public String getStatus() {
        return switch (status) {
            case 0 -> "待支付";
            case 1 -> "已支付";
            case 2 -> "已发货";
            case 3 -> "已完成";
            case 4 -> "已取消";
            case 5 -> "退款中";
            default -> "未知状态";
        };
    }
} 