package com.example.constants;

import java.math.BigDecimal;

/**
 * 推荐系统相关常量
 */
public class RecommendConstants {
    
    /**
     * 默认相似用户数量限制
     */
    public static final Integer DEFAULT_SIMILAR_USERS_LIMIT = 10;
    
    /**
     * 默认推荐用户数量限制
     */
    public static final Integer DEFAULT_RECOMMEND_USERS_LIMIT = 5;
    
    /**
     * 默认相似度阈值
     */
    public static final BigDecimal DEFAULT_SIMILARITY_THRESHOLD = new BigDecimal("0.5");
    
    /**
     * 默认相似度过期天数
     */
    public static final Integer DEFAULT_SIMILARITY_EXPIRY_DAYS = 30;
    
    /**
     * 默认矩阵构建批处理大小
     */
    public static final Integer DEFAULT_MATRIX_BUILD_BATCH_SIZE = 100;
    
    /**
     * 最大购买历史记录大小
     */
    public static final Integer MAX_PURCHASE_HISTORY_SIZE = 50;
    
    /**
     * 默认活跃天数
     */
    public static final Integer DEFAULT_ACTIVE_DAYS = 90;
} 