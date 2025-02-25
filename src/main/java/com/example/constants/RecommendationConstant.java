package com.example.constants;

/**
 * 推荐系统常量
 */
public class RecommendationConstant {
    
    /**
     * 算法类型：基于用户的协同过滤
     */
    public static final int ALGORITHM_USER_BASED_CF = 1;
    
    /**
     * 算法类型：基于商品的协同过滤
     */
    public static final int ALGORITHM_ITEM_BASED_CF = 2;
    
    /**
     * 算法类型：混合协同过滤
     */
    public static final int ALGORITHM_HYBRID_CF = 3;
    
    /**
     * 算法类型：热门推荐
     */
    public static final int ALGORITHM_POPULAR = 4;
    
    /**
     * 算法类型：新品推荐
     */
    public static final int ALGORITHM_NEW_PRODUCT = 5;
    
    /**
     * 默认推荐过期时间（天）
     */
    public static final int DEFAULT_EXPIRE_DAYS = 7;
    
    /**
     * 默认推荐数量
     */
    public static final int DEFAULT_RECOMMEND_LIMIT = 20;
    
    /**
     * 最低推荐分数阈值
     */
    public static final double MIN_RECOMMEND_SCORE = 0.0;
}
