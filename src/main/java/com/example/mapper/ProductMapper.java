package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.model.entity.Product;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 商品Mapper接口
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {

    /**
     * 分页查询商品（支持多条件筛选）
     *
     * @param page 分页对象
     * @param params 查询参数
     * @return 分页商品列表
     */
    IPage<Product> selectProductPage(Page<Product> page, @Param("params") Map<String, Object> params);

    /**
     * 查询商品详情（包含分类信息）
     *
     * @param id 商品ID
     * @return 商品详情
     */
    Product selectProductDetail(Long id);

    /**
     * 根据分类ID查询商品
     *
     * @param categoryId 分类ID
     * @return 商品列表
     */
    List<Product> selectByCategoryId(@Param("categoryId") Long categoryId);

    /**
     * 根据标签ID查询商品
     *
     * @param tagId 标签ID
     * @return 商品列表
     */
    List<Product> selectByTagId(@Param("tagId") Long tagId);

    /**
     * 更新商品库存
     *
     * @param productId 商品ID
     * @param count 变更数量（正数增加，负数减少）
     * @return 影响行数
     */
    int updateStock(@Param("productId") Long productId, @Param("count") Integer count);

    /**
     * 批量更新商品状态
     *
     * @param ids 商品ID列表
     * @param status 状态：0-下架 1-上架
     * @return 影响行数
     */
    int batchUpdateStatus(@Param("ids") List<Long> ids, @Param("status") Integer status);

    /**
     * 查询热门商品
     *
     * @param limit 限制数量
     * @return 热门商品列表
     */
    List<Product> selectHotProducts(@Param("limit") Integer limit);

    /**
     * 查询最新商品
     *
     * @param limit 限制数量
     * @return 最新商品列表
     */
    List<Product> selectNewProducts(@Param("limit") Integer limit);

    /**
     * 查询推荐商品（基于分类）
     *
     * @param categoryId 分类ID
     * @param productId 当前商品ID（排除）
     * @param limit 限制数量
     * @return 推荐商品列表
     */
    List<Product> selectRecommendProducts(
            @Param("categoryId") Long categoryId, 
            @Param("productId") Long productId, 
            @Param("limit") Integer limit);

    /**
     * 查询价格区间内的商品
     *
     * @param minPrice 最低价格
     * @param maxPrice 最高价格
     * @return 商品列表
     */
    List<Product> selectByPriceRange(
            @Param("minPrice") BigDecimal minPrice, 
            @Param("maxPrice") BigDecimal maxPrice);

    /**
     * 统计各分类商品数量
     *
     * @return 统计结果
     */
    @MapKey("categoryId")
    List<Map<String, Object>> countProductByCategory();

    /**
     * 统计各状态商品数量
     *
     * @return 统计结果
     */
    @MapKey("status")
    List<Map<String, Object>> countProductByStatus();

    /**
     * 查询库存预警商品（库存低于预警值）
     *
     * @param threshold 预警阈值
     * @return 库存预警商品列表
     */
    List<Product> selectLowStockProducts(@Param("threshold") Integer threshold);
    
    /**
     * 模糊搜索商品
     *
     * @param keyword 关键词
     * @param limit 限制数量
     * @return 商品列表
     */
    List<Product> searchByKeyword(@Param("keyword") String keyword, @Param("limit") Integer limit);
    
    /**
     * 更新商品标签
     *
     * @param productId 商品ID
     * @param tags 标签JSON
     * @return 影响行数
     */
    int updateProductTags(@Param("productId") Long productId, @Param("tags") String tags);
    
    /**
     * 根据多个分类ID查询商品
     *
     * @param categoryIds 分类ID列表
     * @return 商品列表
     */
    List<Product> selectByCategoryIds(@Param("categoryIds") List<Long> categoryIds);
} 