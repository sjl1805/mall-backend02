package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单表
 *
 * @TableName orders
 */
@TableName(value = "orders")
@Data
@Schema(description = "订单实体")
public class Orders implements Serializable {
    /**
     * 订单ID
     */
    @TableId(type = IdType.AUTO)
    @Schema(description = "订单ID", example = "1")
    private Long id;

    /**
     * 订单号
     */
    @Schema(description = "订单号", example = "202306150001")
    private String orderNo;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID", example = "1001")
    private Long userId;

    /**
     * 订单总金额
     */
    @Schema(description = "订单总金额", example = "12998.00")
    private BigDecimal totalAmount;

    /**
     * 实付金额
     */
    @Schema(description = "实付金额", example = "12498.00")
    private BigDecimal payAmount;

    /**
     * 订单状态：0-待支付 1-已支付 2-已发货 3-已完成 4-已取消 5-退款中
     */
    @Schema(description = "订单状态：0-待支付 1-已支付 2-已发货 3-已完成 4-已取消 5-退款中", example = "1")
    private Integer status;

    /**
     * 收货人姓名
     */
    @Schema(description = "收货人姓名", example = "张三")
    private String receiverName;

    /**
     * 收货人电话
     */
    @Schema(description = "收货人电话", example = "13800138000")
    private String receiverPhone;

    /**
     * 收货地址
     */
    @Schema(description = "收货地址", example = "北京市朝阳区XXX大厦1001室")
    private String receiverAddress;

    /**
     * 支付时间
     */
    @Schema(description = "支付时间", example = "2023-01-01T12:10:30")
    private LocalDateTime paymentTime;

    /**
     * 发货时间
     */
    @Schema(description = "发货时间", example = "2023-01-02T09:30:00")
    private LocalDateTime deliveryTime;

    /**
     * 收货时间
     */
    @Schema(description = "收货时间", example = "2023-01-05T18:20:15")
    private LocalDateTime receiveTime;

    /**
     * 支付方式：1-支付宝 2-微信 3-银联
     */
    @Schema(description = "支付方式：1-支付宝 2-微信 3-银联", example = "2")
    private Integer paymentMethod;

    /**
     * 物流公司
     */
    @Schema(description = "物流公司", example = "顺丰速运")
    private String logisticsCompany;

    /**
     * 运单号
     */
    @Schema(description = "运单号", example = "SF1234567890")
    private String trackingNumber;

    /**
     * 评价状态：0未评价 1已评价
     */
    @Schema(description = "评价状态：0未评价 1已评价", example = "0")
    private Integer commentStatus;

    /**
     * 创建时间（带时区）
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间", example = "2023-01-01T12:00:00")
    private LocalDateTime createTime;

    /**
     * 更新时间（带时区）
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间", example = "2023-01-01T12:30:00")
    private LocalDateTime updateTime;

    /**
     * 时区信息
     */
    @Schema(description = "时区信息", example = "Asia/Shanghai")
    private String timezone;
}