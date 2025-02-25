package com.example.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 推荐用户视图对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendUserVO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 用户头像
     */
    private String avatar;
    
    /**
     * 相似度
     */
    private BigDecimal similarity;
    
    /**
     * 共同兴趣数量
     */
    private Integer commonInterests;
    
    /**
     * 推荐理由
     */
    private String recommendReason;
} 