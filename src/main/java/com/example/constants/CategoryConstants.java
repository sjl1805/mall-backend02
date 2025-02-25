package com.example.constants;

/**
 * 商品分类常量类
 */
public class CategoryConstants {

    /**
     * 分类状态：禁用
     */
    public static final int STATUS_DISABLED = 0;

    /**
     * 分类状态：启用
     */
    public static final int STATUS_ENABLED = 1;

    /**
     * 分类层级：一级
     */
    public static final int LEVEL_FIRST = 1;

    /**
     * 分类层级：二级
     */
    public static final int LEVEL_SECOND = 2;

    /**
     * 分类层级：三级
     */
    public static final int LEVEL_THIRD = 3;

    /**
     * 分类名称最大长度
     */
    public static final int MAX_NAME_LENGTH = 50;

    /**
     * 默认热门分类数量限制
     */
    public static final int DEFAULT_HOT_CATEGORIES_LIMIT = 10;
    
    /**
     * 默认分类树深度
     */
    public static final int DEFAULT_TREE_DEPTH = 3;
    
    /**
     * 默认父级ID（根分类）
     */
    public static final long DEFAULT_PARENT_ID = 0L;
} 