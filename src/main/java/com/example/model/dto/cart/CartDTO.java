package com.example.model.dto.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.*;

@Data
@Schema(description = "购物车数据传输对象")
public class CartDTO {
    @Schema(description = "购物车项ID（更新时必填）", example = "789")
    private Long id;

    @NotNull(message = "用户ID不能为空")
    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "123")
    private Long userId;

    @NotNull(message = "商品ID不能为空")
    @Schema(description = "商品ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "456")
    private Long productId;

    @NotNull(message = "商品数量不能为空")
    @Min(value = 1, message = "数量至少为1")
    @Schema(description = "商品数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Integer quantity;

    @Min(0) @Max(1)
    @Schema(description = "选中状态：0-未选中 1-已选中", defaultValue = "1", example = "1")
    private Integer checked = 1;
} 