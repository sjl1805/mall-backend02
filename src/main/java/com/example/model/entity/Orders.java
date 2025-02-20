package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单表
 *
 * @TableName orders
 */
@Schema(description = "订单实体")
@TableName(value = "orders")
@Data
public class Orders implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 订单ID
     */
    @Schema(description = "订单ID", example = "6001")
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 订单号
     */
    @Schema(description = "订单编号", example = "202309010001")
    private String orderNo;
    /**
     * 用户ID
     */
    @Schema(description = "用户ID", example = "10001")
    private Long userId;
    /**
     * 订单总金额
     */
    @Schema(description = "订单总金额", example = "11998.00")
    private BigDecimal totalAmount;
    /**
     * 实付金额
     */
    @Schema(description = "实际支付金额", example = "11798.00")
    private BigDecimal payAmount;
    /**
     * 订单状态：0-待支付 1-已支付 2-已发货 3-已完成 4-已取消
     */
    @Schema(description = "订单状态：0-待支付 1-已支付 2-已发货 3-已完成 4-已取消", example = "1")
    private Integer status;
    /**
     * 收货人姓名
     */
    @Schema(description = "收货人姓名", example = "张三")
    private String receiverName;
    /**
     * 收货人电话
     */
    @Schema(description = "收货人电话", example = "13800000001")
    private String receiverPhone;
    /**
     * 收货地址
     */
    @Schema(description = "完整收货地址", example = "北京市海淀区中关村大街1号")
    private String receiverAddress;
    /**
     * 支付时间
     */
    @Schema(description = "支付时间", example = "2023-08-01T10:20:00")
    private LocalDateTime paymentTime;
    /**
     * 发货时间
     */
    @Schema(description = "发货时间", example = "2023-08-02T09:30:00")
    private LocalDateTime deliveryTime;
    /**
     * 收货时间
     */
    @Schema(description = "确认收货时间", example = "2023-08-05T15:00:00")
    private LocalDateTime receiveTime;
    /**
     * 支付方式：1-支付宝 2-微信 3-银联
     */
    @Schema(description = "支付方式：1-支付宝 2-微信 3-银联", example = "1")
    private Integer paymentMethod;
    /**
     * 物流公司
     */
    @Schema(description = "物流公司名称", example = "顺丰速运")
    private String logisticsCompany;
    /**
     * 运单号
     */
    @Schema(description = "物流运单号", example = "SF123456789")
    private String trackingNumber;
    /**
     * 评价状态：0未评价 1已评价
     */
    @Schema(description = "评价状态：0-未评 1-已评", example = "0")
    private Integer commentStatus;
    /**
     * 创建时间（带时区）
     */
    @Schema(description = "创建时间", example = "2023-08-01T10:15:30")
    private LocalDateTime createTime;
    /**
     * 更新时间（带时区）
     */
    @Schema(description = "更新时间", example = "2023-08-01T10:15:30")
    private LocalDateTime updateTime;
    /**
     * 时区信息
     */
    @Schema(description = "时区偏移", example = "+08:00")
    private String timezone;
    @TableField(exist = false)
    @Schema(description = "订单商品明细列表")
    private List<OrderItem> items;

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
}