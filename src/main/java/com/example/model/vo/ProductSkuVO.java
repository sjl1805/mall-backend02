package com.example.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductSkuVO {
    private Long id;

    @Schema(description = "商品ID", example = "1")
    private Long productId;

    @Schema(description = "规格值组合（JSON）", example = "{\"color\":\"red\",\"size\":\"M\"}")
    private String specValues;

    @Schema(description = "商品单价", example = "99.99")
    private BigDecimal price;

    @Schema(description = "商品库存", example = "100")
    private Integer stock;

    @Schema(description = "SKU主图URL", example = "http://example.com/image.jpg")
    private String mainImage;

    @Schema(description = "SKU状态：0-下架 1-上架", example = "1")
    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    // 状态转换
    public String getStatus() {
        return switch (status) {
            case 1 -> "上架";
            case 0 -> "下架";
            default -> "未知状态";
        };
    }

    // JSON转换方法
    public void setSpecValues(String specValues) {
        // 使用JSON工具转换，如：this.specValues = JSONUtil.parseObj(specValues);
    }
} 