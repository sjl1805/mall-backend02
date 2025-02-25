package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.Products;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 商品服务接口
 * <p>
 * 定义了商品模块的业务功能，包括基础的CRUD操作和高级业务处理
 * 该接口继承了MyBatis-Plus的IService接口，获取通用的服务方法
 * 是电商系统核心模块之一，为前台展示和后台管理提供数据支持
 *
 * @author 31815
 * @description 针对表【products(商品表)】的数据库操作Service
 * @createDate 2025-02-24 12:03:56
 */
public interface ProductsService extends IService<Products> {

    /**
     * 根据商品名称查询商品
     *
     * @param name 商品名称
     * @return 商品列表
     */
    List<Products> selectByName(String name);

    /**
     * 分页查询商品
     *
     * @param page 分页信息
     * @return 商品列表
     */
    IPage<Products> selectPage(IPage<Products> page);

    /**
     * 根据ID查询商品
     *
     * @param id 商品ID
     * @return 商品信息
     */
    Products selectById(Long id);

    /**
     * 新增商品
     *
     * @param product 商品信息
     * @return 插入结果
     */
    boolean insertProduct(Products product);

    /**
     * 更新商品信息
     *
     * @param product 商品信息
     * @return 更新结果
     */
    boolean updateProduct(Products product);

    /**
     * 根据ID删除商品
     *
     * @param id 商品ID
     * @return 删除结果
     */
    boolean deleteProduct(Long id);

    /**
     * 根据分类ID查询商品
     *
     * @param categoryId 分类ID
     * @param page       分页参数
     * @return 商品分页列表
     */
    IPage<Products> selectByCategoryId(Long categoryId, IPage<Products> page);



    /**
     * 获取热门商品
     *
     * @param limit 限制数量
     * @return 热门商品列表
     */
    List<Products> getHotProducts(Integer limit);

    /**
     * 获取推荐商品
     *
     * @param limit 限制数量
     * @return 推荐商品列表
     */
    List<Products> getRecommendProducts(Integer limit);

    /**
     * 获取新品
     *
     * @param days  最近天数
     * @param limit 限制数量
     * @return 新品列表
     */
    List<Products> getNewProducts(Integer days, Integer limit);

    /**
     * 更新商品库存
     *
     * @param id       商品ID
     * @param quantity 变动数量（正数增加，负数减少）
     * @return 更新结果
     */
    boolean updateStock(Long id, Integer quantity);

    /**
     * 批量更新商品状态
     *
     * @param ids    商品ID列表
     * @param status 状态值
     * @return 更新结果
     */
    boolean batchUpdateStatus(List<Long> ids, Integer status);


    /**
     * 统计商品销量排行
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @param limit     限制数量
     * @return 销量排行数据
     */
    List<Map<String, Object>> getProductSalesRanking(
            Date startDate, Date endDate, Integer limit);

    /**
     * 获取完整商品详情
     *
     * @param id 商品ID
     * @return 商品详情
     */
    Products getProductDetail(Long id);

    /**
     * 批量导入商品
     *
     * @param productList 商品列表
     * @return 导入成功数量
     */
    int batchImportProducts(List<Products> productList);

    /**
     * 导出商品数据
     *
     * @param categoryId 分类ID（可选）
     * @param keyword    关键词（可选）
     * @return 商品列表
     */
    List<Products> exportProducts(Long categoryId, String keyword);
}
