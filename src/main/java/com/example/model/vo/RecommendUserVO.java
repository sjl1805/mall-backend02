package com.example.model.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 推荐用户视图对象
 */
@Data
public class RecommendUserVO {
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 昵称
     */
    private String nickname;
    
    /**
     * 头像
     */
    private String avatar;
    
    /**
     * 相似度
     */
    private BigDecimal similarity;
} 