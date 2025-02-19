package com.example.model.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.*;
import java.util.List;

@Data
@Schema(description = "订单创建数据传输对象")
public class OrderCreateDTO {
    @NotNull(message = "用户ID不能为空")
    @Schema(description = "用户ID", example = "123", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long userId;

    @NotNull(message = "地址ID不能为空")
    @Schema(description = "收货地址ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long addressId;

    @NotNull(message = "支付方式不能为空")
    @Min(1) @Max(3)
    @Schema(description = "支付方式：1-支付宝 2-微信 3-银行卡", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer paymentMethod;

    @NotEmpty(message = "订单项不能为空")
    @Schema(description = "订单项列表", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<OrderItemDTO> items;
} 