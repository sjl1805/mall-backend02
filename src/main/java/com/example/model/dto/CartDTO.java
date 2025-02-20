package com.example.model.dto.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import com.example.model.entity.Cart;

@Data
@Schema(description = "购物车商品数据传输对象")
public class CartDTO {
    @NotNull(message = "商品ID不能为空")
    @Schema(description = "商品ID", example = "1001", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long productId;

    @NotNull(message = "数量不能为空")
    @Min(value = 1, message = "数量最小为1")
    @Schema(description = "商品数量", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer quantity;

    @Min(0)
    @Max(1)
    @Schema(description = "选中状态：0-未选中 1-选中", defaultValue = "1", example = "1")
    private Integer checked = 1;

    public static CartDTO fromEntity(Cart cart) {
        CartDTO dto = new CartDTO();
        dto.setProductId(cart.getProductId());
        dto.setQuantity(cart.getQuantity());
        dto.setChecked(cart.getChecked());
        return dto;
    }

    public Cart toEntity() {
        Cart cart = new Cart();
        cart.setProductId(this.productId);
        cart.setQuantity(this.quantity);
        cart.setChecked(this.checked);
        return cart;
    }
} 