package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 商品服务接口
 */
public interface ProductService extends IService<Product> {

    /**
     * 分页查询商品
     *
     * @param page 分页对象
     * @param params 查询参数
     * @return 分页商品列表
     */
    IPage<Product> getProductPage(Page<Product> page, Map<String, Object> params);

    /**
     * 获取商品详情
     *
     * @param id 商品ID
     * @return 商品详情
     */
    Product getProductDetail(Long id);

    /**
     * 创建商品
     *
     * @param product 商品信息
     * @return 创建的商品
     */
    Product createProduct(Product product);

    /**
     * 更新商品
     *
     * @param product 商品信息
     * @return 是否更新成功
     */
    boolean updateProduct(Product product);

    /**
     * 删除商品
     *
     * @param id 商品ID
     * @return 是否删除成功
     */
    boolean deleteProduct(Long id);

    /**
     * 批量删除商品
     *
     * @param ids 商品ID列表
     * @return 是否删除成功
     */
    boolean batchDeleteProducts(List<Long> ids);

    /**
     * 根据分类ID查询商品
     *
     * @param categoryId 分类ID
     * @return 商品列表
     */
    List<Product> getProductsByCategoryId(Long categoryId);

    /**
     * 根据标签ID查询商品
     *
     * @param tagId 标签ID
     * @return 商品列表
     */
    List<Product> getProductsByTagId(Long tagId);

    /**
     * 更新商品库存
     *
     * @param productId 商品ID
     * @param count 变更数量（正数增加，负数减少）
     * @return 是否更新成功
     */
    boolean updateStock(Long productId, Integer count);

    /**
     * 批量更新商品状态
     *
     * @param ids 商品ID列表
     * @param status 状态：0-下架 1-上架
     * @return 是否更新成功
     */
    boolean batchUpdateStatus(List<Long> ids, Integer status);

    /**
     * 获取热门商品
     *
     * @param limit 限制数量
     * @return 热门商品列表
     */
    List<Product> getHotProducts(Integer limit);

    /**
     * 获取最新商品
     *
     * @param limit 限制数量
     * @return 最新商品列表
     */
    List<Product> getNewProducts(Integer limit);

    /**
     * 获取推荐商品
     *
     * @param categoryId 分类ID
     * @param productId 当前商品ID（排除）
     * @param limit 限制数量
     * @return 推荐商品列表
     */
    List<Product> getRecommendProducts(Long categoryId, Long productId, Integer limit);

    /**
     * 根据价格区间查询商品
     *
     * @param minPrice 最低价格
     * @param maxPrice 最高价格
     * @return 商品列表
     */
    List<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);

    /**
     * 统计各分类商品数量
     *
     * @return 统计结果
     */
    List<Map<String, Object>> countProductByCategory();

    /**
     * 统计各状态商品数量
     *
     * @return 统计结果
     */
    List<Map<String, Object>> countProductByStatus();

    /**
     * 获取库存预警商品
     *
     * @param threshold 预警阈值
     * @return 库存预警商品列表
     */
    List<Product> getLowStockProducts(Integer threshold);

    /**
     * 根据关键词搜索商品
     *
     * @param keyword 关键词
     * @param limit 限制数量
     * @return 商品列表
     */
    List<Product> searchProductsByKeyword(String keyword, Integer limit);

    /**
     * 更新商品标签
     *
     * @param productId 商品ID
     * @param tags 标签列表
     * @return 是否更新成功
     */
    boolean updateProductTags(Long productId, List<Map<String, Object>> tags);

    /**
     * 根据多个分类ID查询商品
     *
     * @param categoryIds 分类ID列表
     * @return 商品列表
     */
    List<Product> getProductsByCategoryIds(List<Long> categoryIds);
    
    /**
     * 检查商品库存是否充足
     *
     * @param productId 商品ID
     * @param quantity 需要的数量
     * @return 是否充足
     */
    boolean checkStockSufficient(Long productId, Integer quantity);
    
    /**
     * 根据用户偏好推荐商品
     *
     * @param userId 用户ID
     * @param limit 限制数量
     * @return 推荐商品列表
     */
    List<Product> getPersonalizedRecommendProducts(Long userId, Integer limit);
} 