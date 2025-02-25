package com.example.constants;

/**
 * 商品常量类
 */
public class ProductConstants {

    /**
     * 商品状态：下架
     */
    public static final int STATUS_OFF_SHELF = 0;

    /**
     * 商品状态：上架
     */
    public static final int STATUS_ON_SHELF = 1;

    /**
     * 商品状态描述
     */
    public static final String[] STATUS_DESC = {
            "下架", "上架"
    };

    /**
     * 默认热门商品数量限制
     */
    public static final int DEFAULT_HOT_PRODUCTS_LIMIT = 10;

    /**
     * 默认最新商品数量限制
     */
    public static final int DEFAULT_NEW_PRODUCTS_LIMIT = 10;

    /**
     * 默认推荐商品数量限制
     */
    public static final int DEFAULT_RECOMMEND_PRODUCTS_LIMIT = 6;

    /**
     * 默认库存预警阈值
     */
    public static final int DEFAULT_STOCK_WARNING_THRESHOLD = 10;
    
    /**
     * 默认搜索结果数量限制
     */
    public static final int DEFAULT_SEARCH_LIMIT = 20;
    
    /**
     * 默认个性化推荐商品数量限制
     */
    public static final int DEFAULT_PERSONALIZED_RECOMMEND_LIMIT = 10;
    
    /**
     * 商品名称最大长度
     */
    public static final int MAX_NAME_LENGTH = 100;
    
    /**
     * 商品描述最大长度
     */
    public static final int MAX_DESCRIPTION_LENGTH = 2000;
    
    /**
     * 商品图片最大数量
     */
    public static final int MAX_IMAGES_COUNT = 10;
    
    /**
     * 商品标签最大数量
     */
    public static final int MAX_TAGS_COUNT = 10;
} 