package com.example.model.dto;

import com.example.model.entity.OrderItem;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "订单项数据传输对象")
public class OrderItemDTO {
    @Schema(description = "订单项ID（更新时必填）", example = "789")
    private Long id;

    @NotNull(message = "订单ID不能为空")
    @Schema(description = "订单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "456")
    private Long orderId;

    @NotNull(message = "商品ID不能为空")
    @Schema(description = "商品ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "123")
    private Long productId;

    @NotBlank(message = "商品名称不能为空")
    @Size(max = 128, message = "商品名称最长128个字符")
    @Schema(description = "商品名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "智能手机")
    private String productName;

    @Size(max = 255, message = "图片URL过长")
    @Schema(description = "商品主图URL", example = "https://example.com/image.jpg")
    private String productImage;

    @NotNull(message = "商品单价不能为空")
    @DecimalMin(value = "0.01", message = "单价必须大于0")
    @Schema(description = "商品单价", requiredMode = Schema.RequiredMode.REQUIRED, example = "1999.99")
    private BigDecimal price;

    @NotNull(message = "购买数量不能为空")
    @Min(value = 1, message = "数量至少为1")
    @Schema(description = "购买数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Integer quantity;

    @NotNull(message = "总金额不能为空")
    @DecimalMin(value = "0.01", message = "总金额必须大于0")
    @Schema(description = "商品总价", requiredMode = Schema.RequiredMode.REQUIRED, example = "3999.98")
    private BigDecimal totalAmount;

    @Min(0)
    @Max(1)
    @Schema(description = "评价状态：0-未评价 1-已评价", defaultValue = "0", example = "1")
    private Integer commentStatus = 0;

    public static OrderItemDTO fromEntity(OrderItem item ) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setId(item.getId());
        dto.setOrderId(item.getOrderId());
        dto.setProductId(item.getProductId());
        dto.setProductName(item.getProductName());
        dto.setProductImage(item.getProductImage());
        dto.setPrice(item.getPrice());
        dto.setQuantity(item.getQuantity());
        dto.setTotalAmount(item.getTotalAmount());
        return dto;
    }

    public OrderItem toEntity() {
        OrderItem item = new OrderItem();
        item.setId(this.id);
        item.setOrderId(this.orderId);
        item.setProductId(this.productId);
        item.setProductName(this.productName);
        item.setProductImage(this.productImage);
        item.setPrice(this.price);
        item.setQuantity(this.quantity);
        item.setTotalAmount(this.totalAmount);
        return item;
    }
} 