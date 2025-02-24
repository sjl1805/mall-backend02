package com.example.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductSkuVO {
    private Long id;
    private Long productId;
    private Object specValues; // 反序列化后的规格组合
    private BigDecimal price;
    private Integer stock;
    private Integer sales;
    private String mainImage;
    private String status;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    // 状态转换
    public String getStatus() {
        return switch (status) {
            case "1" -> "上架";
            case "0" -> "下架";
            default -> "未知状态";
        };
    }

    // JSON转换方法
    public void setSpecValues(String specValues) {
        // 使用JSON工具转换，如：this.specValues = JSONUtil.parseObj(specValues);
    }
} 