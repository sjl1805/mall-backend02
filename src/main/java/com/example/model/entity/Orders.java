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
 * 订单表
 *
 * @TableName orders
 */
@TableName(value = "orders")
@Data
@Schema(description = "订单实体")
public class Orders {
    /**
     * 订单ID
     */
    @TableId(type = IdType.AUTO)
    @Schema(description = "订单ID", example = "10001")
    private Long id;

    /**
     * 订单号
     */
    @NotBlank(message = "订单号不能为空")
    @Size(max = 64, message = "订单号最长64个字符")
    @Schema(description = "订单号", example = "20240318123456")
    private String orderNo;

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    @Schema(description = "用户ID", example = "10001")
    private Long userId;

    /**
     * 订单总金额
     */
    @NotNull(message = "订单总金额不能为空")
    @DecimalMin(value = "0.01", message = "总金额不能小于0.01")
    @Schema(description = "订单总金额", example = "999.00")
    private BigDecimal totalAmount;

    /**
     * 实付金额
     */
    @NotNull(message = "实付金额不能为空")
    @DecimalMin(value = "0.00", message = "实付金额不能为负数")
    @Schema(description = "实付金额", example = "899.00")
    private BigDecimal payAmount;

    /**
     * 订单状态：0-待支付 1-已支付 2-已发货 3-已完成 4-已取消 5-退款中
     */
    @Range(min = 0, max = 5, message = "订单状态参数不合法")
    @Schema(description = "订单状态 0-待支付 1-已支付 2-已发货 3-已完成 4-已取消 5-退款中", example = "0")
    private Integer status;

    /**
     * 收货人姓名
     */
    @NotBlank(message = "收货人姓名不能为空")
    @Size(max = 32, message = "姓名最长32个字符")
    @Schema(description = "收货人姓名", example = "张三")
    private String receiverName;

    /**
     * 收货人电话
     */
    @NotBlank(message = "联系电话不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @Schema(description = "联系电话", example = "13800138000")
    private String receiverPhone;

    /**
     * 收货地址
     */
    @NotBlank(message = "收货地址不能为空")
    @Size(max = 255, message = "地址最长255个字符")
    @Schema(description = "收货地址", example = "广东省深圳市南山区科技园路123号")
    private String receiverAddress;

    /**
     * 支付时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "支付时间", example = "2024-03-18 14:30:00")
    private LocalDateTime paymentTime;

    /**
     * 发货时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "发货时间", example = "2024-03-18 15:00:00")
    private LocalDateTime deliveryTime;

    /**
     * 收货时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "收货时间", example = "2024-03-20 10:00:00")
    private LocalDateTime receiveTime;

    /**
     * 支付方式：1-支付宝 2-微信 3-银联
     */
    @Range(min = 1, max = 3, message = "支付方式参数不合法")
    @Schema(description = "支付方式 1-支付宝 2-微信 3-银联", example = "1")
    private Integer paymentMethod;

    /**
     * 物流公司
     */
    @Size(max = 64, message = "物流公司名称最长64个字符")
    @Schema(description = "物流公司", example = "顺丰速运")
    private String logisticsCompany;

    /**
     * 运单号
     */
    @Size(max = 64, message = "运单号最长64个字符")
    @Schema(description = "运单号", example = "SF123456789")
    private String trackingNumber;

    /**
     * 评价状态：0未评价 1已评价
     */
    @Range(min = 0, max = 1, message = "评价状态参数不合法")
    @Schema(description = "评价状态 0-未评价 1-已评价", example = "0")
    private Integer commentStatus = 0;

    /**
     * 创建时间（带时区）
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间", example = "2024-03-18 14:00:00")
    private LocalDateTime createTime;

    /**
     * 更新时间（带时区）
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间", example = "2024-03-18 14:05:00")
    private LocalDateTime updateTime;

    /**
     * 时区信息
     */
    @Size(max = 50, message = "时区信息最长50个字符")
    @Schema(description = "时区信息", example = "+08:00")
    private String timezone = "+08:00";
}