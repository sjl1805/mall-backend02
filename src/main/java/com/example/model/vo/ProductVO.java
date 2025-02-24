package com.example.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProductVO {
    private Long id;
    private Long categoryId;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private List<String> images;
    private String status;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    // 关联的SKU列表
    private List<ProductSkuVO> skus;

    // 状态转换
    public String getStatus() {
        return switch (status) {
            case "1" -> "上架";
            case "0" -> "下架";
            default -> "未知状态";
        };
    }

    // 转换图片JSON字符串为列表
    public void setImages(String images) {
        // 使用JSON工具转换，如：this.images = JSONUtil.parseArray(images, String.class);
    }
} 