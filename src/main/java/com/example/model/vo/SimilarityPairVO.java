package com.example.model.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 相似度对视图对象
 */
@Data
public class SimilarityPairVO {
    
    /**
     * 用户A的ID
     */
    private Long userIdA;
    
    /**
     * 用户B的ID
     */
    private Long userIdB;
    
    /**
     * 相似度
     */
    private BigDecimal similarity;
} 