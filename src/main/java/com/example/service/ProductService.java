package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.Product;

import java.util.List;

/**
 * 商品服务接口
 */
public interface ProductService extends IService<Product> {
    
    /**
     * 根据分类ID分页查询商品
     *
     * @param page 分页参数
     * @param categoryId 分类ID
     * @return 分页商品列表
     */
    IPage<Product> pageProductsByCategory(Page<Product> page, Long categoryId);
    
    /**
     * 搜索商品
     *
     * @param page 分页参数
     * @param keyword 搜索关键词
     * @return 分页商品列表
     */
    IPage<Product> searchProducts(Page<Product> page, String keyword);
    
    /**
     * 获取热门商品列表
     *
     * @param limit 限制数量
     * @return 热门商品列表
     */
    List<Product> getHotProducts(Integer limit);
    
    /**
     * 获取新品商品列表
     *
     * @param limit 限制数量
     * @return 新品商品列表
     */
    List<Product> getNewProducts(Integer limit);
    
    /**
     * 获取推荐商品列表
     *
     * @param userId 用户ID
     * @param limit 限制数量
     * @return 推荐商品列表
     */
    List<Product> getRecommendProducts(Long userId, Integer limit);
    
    /**
     * 添加商品
     *
     * @param product 商品信息
     * @return 添加成功的商品
     */
    Product addProduct(Product product);
    
    /**
     * 更新商品
     *
     * @param product 商品信息
     * @return 是否更新成功
     */
    boolean updateProduct(Product product);
    
    /**
     * 更新商品状态
     *
     * @param productId 商品ID
     * @param status 状态：0-下架 1-上架
     * @return 是否更新成功
     */
    boolean updateProductStatus(Long productId, Integer status);
    
    /**
     * 删除商品
     *
     * @param productId 商品ID
     * @return 是否删除成功
     */
    boolean deleteProduct(Long productId);
} 