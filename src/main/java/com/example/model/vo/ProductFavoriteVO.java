package com.example.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductFavoriteVO {
    private Long id;
    private Long userId;
    private Long productId;
    private Long folderId; // 收藏夹ID（可为空）
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    // 关联商品信息
    private String productName;
    private String productImage;
} 