package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.model.entity.Products;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 31815
 * @description 针对表【products(商品表)】的数据库操作Mapper
 * @createDate 2025-02-24 12:03:56
 * @Entity model.entity.Products
 */
@Mapper
public interface ProductsMapper extends BaseMapper<Products> {

    /**
     * 根据商品名称模糊查询商品
     *
     * @param name 商品名称
     * @return 商品列表
     */
    List<Products> selectByNameLike(@Param("name") String name);

    /**
     * 根据商品名称查询商品
     *
     * @param name 商品名称
     * @return 商品列表
     */
    List<Products> selectByName(@Param("name") String name);

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
    Products selectById(@Param("id") Long id);

    /**
     * 插入新商品
     *
     * @param product 商品信息
     * @return 插入结果
     */
    int insertProduct(Products product);

    /**
     * 更新商品信息
     *
     * @param product 商品信息
     * @return 更新结果
     */
    int updateProduct(Products product);

    /**
     * 根据ID删除商品
     *
     * @param id 商品ID
     * @return 删除结果
     */
    int deleteProduct(@Param("id") Long id);

    /**
     * 根据分类ID查询商品
     *
     * @param categoryId 分类ID
     * @return 商品列表
     */
    List<Products> selectByCategoryId(@Param("categoryId") Long categoryId);

    /**
     * 根据价格范围查询商品
     *
     * @param minPrice 最低价格
     * @param maxPrice 最高价格
     * @return 商品列表
     */
    List<Products> selectByPriceRange(
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice);

    /**
     * 根据状态查询商品
     *
     * @param status 商品状态
     * @return 商品列表
     */
    List<Products> selectByStatus(@Param("status") Integer status);

    /**
     * 查询热门商品
     *
     * @param limit 限制数量
     * @return 热门商品列表
     */
    List<Products> selectHotProducts(@Param("limit") Integer limit);


    /**
     * 批量插入商品
     *
     * @param productList 商品列表
     * @return 插入结果
     */
    int batchInsertProducts(@Param("productList") List<Products> productList);

    /**
     * 减少商品库存
     *
     * @param id       商品ID
     * @param quantity 减少数量
     * @return 更新结果
     */
    int decreaseStock(@Param("id") Long id, @Param("quantity") Integer quantity);

    /**
     * 查询推荐商品
     *
     * @param limit 限制数量
     * @return 推荐商品列表
     */
    List<Products> selectRecommendProducts(@Param("limit") Integer limit);

    /**
     * 查询新品
     *
     * @param days  最近天数
     * @param limit 限制数量
     * @return 新品列表
     */
    List<Products> selectNewProducts(
            @Param("days") Integer days,
            @Param("limit") Integer limit);


    /**
     * 全文搜索商品
     *
     * @param keyword 关键词
     * @param page    分页参数
     * @return 商品分页数据
     */
    IPage<Products> fullTextSearch(
            @Param("keyword") String keyword,
            IPage<Products> page);

    /**
     * 增加商品库存
     *
     * @param id       商品ID
     * @param quantity 增加数量
     * @return 更新结果
     */
    int increaseStock(@Param("id") Long id, @Param("quantity") Integer quantity);

    /**
     * 批量更新商品状态
     *
     * @param ids    商品ID列表
     * @param status 状态值
     * @return 更新结果
     */
    int batchUpdateStatus(
            @Param("ids") List<Long> ids,
            @Param("status") Integer status);

    /**
     * 统计商品销量排行
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @param limit     限制数量
     * @return 销量排行数据
     */
    @MapKey("product_id")
    List<Map<String, Object>> getProductSalesRanking(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("limit") Integer limit);

    /**
     * 获取完整商品详情（包含分类名称、评价等）
     *
     * @param id 商品ID
     * @return 商品详情
     */
    Products selectProductDetail(@Param("id") Long id);
}




