package com.example.constants;

/**
 * 用户标签常量类
 */
public class UserTagConstants {

    /**
     * 标签类型：兴趣
     */
    public static final int TAG_TYPE_INTEREST = 1;

    /**
     * 标签类型：行为
     */
    public static final int TAG_TYPE_BEHAVIOR = 2;

    /**
     * 标签类型：人口特征
     */
    public static final int TAG_TYPE_DEMOGRAPHIC = 3;

    /**
     * 标签类型：其他
     */
    public static final int TAG_TYPE_OTHER = 4;

    /**
     * 标签类型描述
     */
    public static final String[] TAG_TYPE_DESC = {
            "未知", "兴趣", "行为", "人口特征", "其他"
    };
    
    /**
     * 默认常用标签数量
     */
    public static final int DEFAULT_POPULAR_TAG_LIMIT = 20;
    
    /**
     * 默认搜索结果数量
     */
    public static final int DEFAULT_SEARCH_LIMIT = 10;
    
    /**
     * 默认推荐标签数量
     */
    public static final int DEFAULT_RECOMMENDED_TAG_LIMIT = 15;
    
    /**
     * 默认标签使用统计数量
     */
    public static final int DEFAULT_USAGE_STAT_LIMIT = 50;
    
    /**
     * 标签权重最小值
     */
    public static final int MIN_TAG_WEIGHT = 1;
    
    /**
     * 标签权重最大值
     */
    public static final int MAX_TAG_WEIGHT = 100;
    
    /**
     * 标签权重默认值
     */
    public static final int DEFAULT_TAG_WEIGHT = 50;
} 