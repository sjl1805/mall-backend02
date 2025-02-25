package com.example.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 相似度用户对视图对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimilarityPairVO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 用户A的ID
     */
    private Long userIdA;
    
    /**
     * 用户A的名称
     */
    private String usernameA;
    
    /**
     * 用户B的ID
     */
    private Long userIdB;
    
    /**
     * 用户B的名称
     */
    private String usernameB;
    
    /**
     * 相似度
     */
    private BigDecimal similarity;
    
    /**
     * 共同行为数量
     */
    private Integer commonBehaviors;
    
    /**
     * 计算时间
     */
    private LocalDateTime calculateTime;
} 