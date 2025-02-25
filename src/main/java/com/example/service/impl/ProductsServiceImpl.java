package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.ProductsMapper;
import com.example.model.entity.Products;
import com.example.service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 商品服务实现类
 * <p>
 * 该类实现了商品相关的业务逻辑，包括商品的添加、修改、删除和查询等核心功能。
 * 商品是电商系统的核心资源，此服务提供对商品全生命周期的管理。
 * 使用了Spring缓存机制对商品信息进行缓存，提高查询效率，减轻数据库压力。
 * 使用MyBatis-Plus的ServiceImpl简化数据访问操作。
 *
 * @author 31815
 * @description 针对表【products(商品表)】的数据库操作Service实现
 * @createDate 2025-02-24 12:03:56
 */
@Service
@CacheConfig(cacheNames = "products") // 指定该服务类的缓存名称为"products"
public class ProductsServiceImpl extends ServiceImpl<ProductsMapper, Products>
        implements ProductsService {

    @Autowired
    private ProductsMapper productsMapper;

    /**
     * 根据商品名称查询商品列表
     * <p>
     * 该方法从缓存或数据库获取包含指定名称的商品列表，
     * 支持模糊查询，用于前台商品搜索和后台商品管理
     *
     * @param name 商品名称关键词
     * @return 符合条件的商品列表
     */
    @Override
    @Cacheable(value = "products", key = "#name") // 缓存商品搜索结果，提高查询效率
    public List<Products> selectByName(String name) {
        return productsMapper.selectByName(name);
    }

    /**
     * 分页查询商品数据
     * <p>
     * 该方法用于前台商品列表展示和后台商品管理，
     * 支持各种复杂的分页、排序和筛选条件
     *
     * @param page 分页参数
     * @return 商品分页数据
     */
    @Override
    public IPage<Products> selectPage(IPage<Products> page) {
        return productsMapper.selectPage(page);
    }

    /**
     * 根据ID查询商品详细信息
     * <p>
     * 该方法从缓存或数据库获取指定ID的商品详情，
     * 用于商品详情页展示和后台商品编辑
     *
     * @param id 商品ID
     * @return 商品实体对象
     */
    @Override
    @Cacheable(value = "products", key = "#id") // 缓存商品详情，提高查询效率
    public Products selectById(Long id) {
        return productsMapper.selectById(id);
    }

    /**
     * 添加商品
     * <p>
     * 该方法用于后台管理系统添加新商品，
     * 包括商品基本信息、价格、库存、图片等数据，
     * 并清除相关缓存，确保数据一致性
     *
     * @param product 商品实体对象
     * @return 添加成功返回true，失败返回false
     */
    @Override
    @Transactional
    @CacheEvict(value = "products", key = "#product.id") // 清除商品缓存
    public boolean insertProduct(Products product) {
        return productsMapper.insert(product) > 0;
    }

    /**
     * 更新商品信息
     * <p>
     * 该方法用于后台管理系统更新商品信息，
     * 如修改价格、库存、描述、状态等，
     * 并清除相关缓存，确保数据一致性
     *
     * @param product 商品实体对象
     * @return 更新成功返回true，失败返回false
     */
    @Override
    @Transactional
    @CacheEvict(value = "products", key = "#product.id") // 清除商品缓存
    public boolean updateProduct(Products product) {
        return productsMapper.updateById(product) > 0;
    }

    /**
     * 删除商品
     * <p>
     * 该方法用于后台管理系统删除商品，
     * 实际业务中通常建议使用软删除（修改状态）而非物理删除，
     * 并清除相关缓存，确保数据一致性
     *
     * @param id 商品ID
     * @return 删除成功返回true，失败返回false
     */
    @Override
    @Transactional
    @CacheEvict(value = "products", key = "#id") // 清除被删除商品的缓存
    public boolean deleteProduct(Long id) {
        return productsMapper.deleteById(id) > 0;
    }

    /**
     * 根据分类ID查询商品
     * <p>
     * 该方法用于前台按分类浏览商品，
     * 是电商系统中最常用的商品查询方式之一
     *
     * @param categoryId 分类ID
     * @param page       分页参数
     * @return 商品分页列表
     */
    @Override
    @Cacheable(key = "'category_'+#categoryId+'_page_'+#page.current+'_'+#page.size")
    public IPage<Products> selectByCategoryId(Long categoryId, IPage<Products> page) {
        QueryWrapper<Products> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id", categoryId)
                .eq("status", 1)
                .orderByDesc("create_time");
        return page(page, queryWrapper);
    }



    /**
     * 获取热门商品
     * <p>
     * 该方法获取销量最高的商品，
     * 用于首页推荐或热销榜单展示
     *
     * @param limit 限制数量
     * @return 热门商品列表
     */
    @Override
    @Cacheable(key = "'hot_products_'+#limit")
    public List<Products> getHotProducts(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10; // 默认返回10个
        }
        return productsMapper.selectHotProducts(limit);
    }

    /**
     * 获取推荐商品
     * <p>
     * 该方法基于用户行为分析，获取推荐的商品，
     * 提高用户购买意愿和转化率
     *
     * @param limit 限制数量
     * @return 推荐商品列表
     */
    @Override
    @Cacheable(key = "'recommend_products_'+#limit")
    public List<Products> getRecommendProducts(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10; // 默认返回10个
        }
        return productsMapper.selectRecommendProducts(limit);
    }

    /**
     * 获取新品
     * <p>
     * 该方法获取最近添加的商品，
     * 用于首页新品推荐或新品专区
     *
     * @param days  最近天数
     * @param limit 限制数量
     * @return 新品列表
     */
    @Override
    @Cacheable(key = "'new_products_'+#days+'_'+#limit")
    public List<Products> getNewProducts(Integer days, Integer limit) {
        if (days == null || days <= 0) {
            days = 7; // 默认最近7天
        }
        if (limit == null || limit <= 0) {
            limit = 10; // 默认返回10个
        }
        return productsMapper.selectNewProducts(days, limit);
    }



    /**
     * 更新商品库存
     * <p>
     * 该方法用于增加或减少商品库存，
     * 支持正数（增加）和负数（减少）
     *
     * @param id       商品ID
     * @param quantity 变动数量
     * @return 更新结果
     */
    @Override
    @Transactional
    @CacheEvict(key = "#id")
    public boolean updateStock(Long id, Integer quantity) {
        if (quantity == 0) {
            return true; // 无变化，直接返回成功
        }

        if (quantity > 0) {
            // 增加库存
            return productsMapper.increaseStock(id, quantity) > 0;
        } else {
            // 减少库存
            return productsMapper.decreaseStock(id, -quantity) > 0;
        }
    }

    /**
     * 批量更新商品状态
     * <p>
     * 该方法一次性更新多个商品的状态，
     * 如批量上架或下架商品，提高管理效率
     *
     * @param ids    商品ID列表
     * @param status 状态值
     * @return 更新结果
     */
    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public boolean batchUpdateStatus(List<Long> ids, Integer status) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        return productsMapper.batchUpdateStatus(ids, status) > 0;
    }


    /**
     * 统计商品销量排行
     * <p>
     * 该方法统计指定时间段内的商品销量排行，
     * 用于销售分析和商品管理
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @param limit     限制数量
     * @return 销量排行数据
     */
    @Override
    @Cacheable(key = "'sales_ranking_'+#startDate+'_'+#endDate+'_'+#limit")
    public List<Map<String, Object>> getProductSalesRanking(
            Date startDate, Date endDate, Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10; // 默认返回10个
        }
        return productsMapper.getProductSalesRanking(startDate, endDate, limit);
    }

    /**
     * 获取完整商品详情
     * <p>
     * 该方法获取商品的完整信息，包括分类名称、评价等，
     * 用于商品详情页展示
     *
     * @param id 商品ID
     * @return 商品详情
     */
    @Override
    @Cacheable(key = "'detail_'+#id")
    public Products getProductDetail(Long id) {
        return productsMapper.selectProductDetail(id);
    }

    /**
     * 批量导入商品
     * <p>
     * 该方法批量导入商品数据，
     * 用于初始化商品数据或批量上架新商品
     *
     * @param productList 商品列表
     * @return 导入成功数量
     */
    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public int batchImportProducts(List<Products> productList) {
        if (productList == null || productList.isEmpty()) {
            return 0;
        }
        return productsMapper.batchInsertProducts(productList);
    }

    /**
     * 导出商品数据
     * <p>
     * 该方法导出符合条件的商品数据，
     * 用于数据备份或报表生成
     *
     * @param categoryId 分类ID
     * @param keyword    关键词
     * @return 商品列表
     */
    @Override
    public List<Products> exportProducts(Long categoryId, String keyword) {
        QueryWrapper<Products> queryWrapper = new QueryWrapper<>();

        if (categoryId != null && categoryId > 0) {
            queryWrapper.eq("category_id", categoryId);
        }

        if (StringUtils.hasText(keyword)) {
            queryWrapper.like("name", keyword)
                    .or()
                    .like("description", keyword);
        }

        return list(queryWrapper);
    }
}




