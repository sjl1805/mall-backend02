package com.example.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.math.BigDecimal;

@Data
public class CartVO {
    private Long id;

    @Schema(description = "用户ID", example = "1")
    private Long userId;

    @Schema(description = "商品ID", example = "1")
    private Long productId;

    @Schema(description = "商品数量", example = "2")
    private Integer quantity;

    @Schema(description = "选中状态：0-未选中 1-已选中", example = "1")
    private Integer checked;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    // 关联商品信息
    private String productName;
    private String productImage;
    private BigDecimal productPrice;

    // 状态转换
    public String getChecked() {
        return checked.equals(1) ? "已选中" : "未选中";
    }
} 