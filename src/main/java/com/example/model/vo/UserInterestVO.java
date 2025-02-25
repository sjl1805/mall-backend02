package com.example.model.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 用户兴趣VO类
 */
@Data
public class UserInterestVO {
    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 兴趣得分
     */
    private BigDecimal interestScore;

    /**
     * 行为数量
     */
    private Integer behaviorCount;
} 