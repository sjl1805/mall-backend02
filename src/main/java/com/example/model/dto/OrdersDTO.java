package com.example.model.dto;

import com.example.model.entity.Orders;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Schema(description = "订单数据传输对象")
public class OrdersDTO {
    @Schema(description = "订单ID（更新时必填）", example = "456")
    private Long id;

    @NotBlank(message = "订单号不能为空")
    @Size(max = 32, message = "订单号最长32个字符")
    @Schema(description = "订单号", requiredMode = Schema.RequiredMode.REQUIRED, example = "202308150001")
    private String orderNo;

    @NotNull(message = "用户ID不能为空")
    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "123")
    private Long userId;

    @NotNull(message = "订单金额不能为空")
    @DecimalMin(value = "0.01", message = "金额必须大于0")
    @Schema(description = "订单总金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "299.99")
    private BigDecimal totalAmount;

    @NotNull(message = "支付金额不能为空")
    @DecimalMin(value = "0.00", message = "支付金额不能为负")
    @Schema(description = "实付金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "279.99")
    private BigDecimal payAmount;

    @NotNull(message = "订单状态不能为空")
    @Min(0)
    @Max(4)
    @Schema(description = "订单状态：0-待支付 1-已支付 2-已发货 3-已完成 4-已取消",
            requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @NotBlank(message = "收货人姓名不能为空")
    @Size(max = 32, message = "姓名最长32个字符")
    @Schema(description = "收货人姓名", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    private String receiverName;

    @NotBlank(message = "联系电话不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @Schema(description = "联系电话", requiredMode = Schema.RequiredMode.REQUIRED, example = "13812345678")
    private String receiverPhone;

    @NotBlank(message = "收货地址不能为空")
    @Size(max = 255, message = "地址最长255个字符")
    @Schema(description = "收货地址", requiredMode = Schema.RequiredMode.REQUIRED,
            example = "浙江省杭州市西湖区文三路123号")
    private String receiverAddress;

    @Min(1)
    @Max(3)
    @Schema(description = "支付方式：1-支付宝 2-微信 3-银联", example = "1")
    private Integer paymentMethod;

    @Schema(description = "订单项列表")
    private List<OrderItemDTO> items;

    @Size(max = 64, message = "物流公司名称过长")
    @Schema(description = "物流公司", example = "顺丰速运")
    private String logisticsCompany;

    @Size(max = 64, message = "运单号过长")
    @Schema(description = "运单号", example = "SF123456789")
    private String trackingNumber;

    public static OrdersDTO fromEntity(Orders order) {
        OrdersDTO dto = new OrdersDTO();
        dto.setId(order.getId());
        dto.setOrderNo(order.getOrderNo());
        dto.setUserId(order.getUserId());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setPayAmount(order.getPayAmount());
        dto.setStatus(order.getStatus());
        dto.setReceiverName(order.getReceiverName());
        dto.setReceiverPhone(order.getReceiverPhone());
        dto.setReceiverAddress(order.getReceiverAddress());
        dto.setPaymentMethod(order.getPaymentMethod());
        dto.setLogisticsCompany(order.getLogisticsCompany());
        dto.setTrackingNumber(order.getTrackingNumber());
        return dto;
    }

    public Orders toEntity() {
        Orders order = new Orders();
        order.setId(this.id);
        order.setOrderNo(this.orderNo);
        order.setUserId(this.userId);
        order.setTotalAmount(this.totalAmount);
        order.setPayAmount(this.payAmount);
        order.setStatus(this.status);
        order.setReceiverName(this.receiverName);
        order.setReceiverPhone(this.receiverPhone);
        order.setReceiverAddress(this.receiverAddress);
        order.setPaymentMethod(this.paymentMethod);
        order.setLogisticsCompany(this.logisticsCompany);
        order.setTrackingNumber(this.trackingNumber);
        return order;
    }
} 