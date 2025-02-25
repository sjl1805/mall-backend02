package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品相似度实体类
 */
@Data
@TableName("product_similarity")
public class ProductSimilarity {
    
    /**
     * 商品A ID
     */
    private Long productIdA;
    
    /**
     * 商品B ID
     */
    private Long productIdB;
    
    /**
     * 相似度分数（0.0-1.0，基于共同购买/浏览行为计算）
     */
    private BigDecimal similarity;
    
    /**
     * 最后计算时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
} 