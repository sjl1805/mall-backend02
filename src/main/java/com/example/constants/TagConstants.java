package com.example.constants;

/**
 * 标签常量类
 */
public class TagConstants {

    /**
     * 标签类型：风格
     */
    public static final int TAG_TYPE_STYLE = 1;

    /**
     * 标签类型：场景
     */
    public static final int TAG_TYPE_SCENE = 2;

    /**
     * 标签类型：功能
     */
    public static final int TAG_TYPE_FUNCTION = 3;

    /**
     * 标签类型：其他
     */
    public static final int TAG_TYPE_OTHER = 4;

    /**
     * 标签类型描述
     */
    public static final String[] TAG_TYPE_DESC = {
            "未知", "风格", "场景", "功能", "其他"
    };
    
    /**
     * 默认热门标签数量
     */
    public static final int DEFAULT_HOT_TAG_LIMIT = 20;
    
    /**
     * 默认搜索结果数量
     */
    public static final int DEFAULT_SEARCH_LIMIT = 10;
} 