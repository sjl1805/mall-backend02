package com.example.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.math.BigDecimal;

@Data
public class CartVO {
    private Long id;
    private Long userId;
    private Long productId;
    private Integer quantity;
    private String checked; // 选中状态描述
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    // 关联商品信息
    private String productName;
    private String productImage;
    private BigDecimal productPrice;

    // 状态转换
    public String getChecked() {
        return checked.equals("1") ? "已选中" : "未选中";
    }
} 