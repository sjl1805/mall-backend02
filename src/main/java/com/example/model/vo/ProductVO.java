package com.example.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProductVO {
    private Long id;

    @Schema(description = "分类ID", example = "1")
    private Long categoryId;

    @Schema(description = "商品名称", example = "苹果")
    private String name;

    @Schema(description = "商品描述", example = "新鲜的苹果")
    private String description;

    @Schema(description = "商品单价", example = "9.99")
    private BigDecimal price;

    @Schema(description = "商品库存", example = "100")
    private Integer stock;

    @Schema(description = "商品图片URL", example = "http://example.com/image.jpg")
    private String images;

    @Schema(description = "商品状态：0-下架 1-上架", example = "1")
    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    // 关联的SKU列表
    private List<ProductSkuVO> skus;

    // 状态转换
    public String getStatus() {
        return switch (status) {
            case 1 -> "上架";
            case 0 -> "下架";
            default -> "未知状态";
        };
    }

} 