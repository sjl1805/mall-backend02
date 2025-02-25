package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.ProductSkuMapper;
import com.example.model.entity.ProductSku;
import com.example.service.ProductSkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商品SKU服务实现类
 * <p>
 * 该类实现了商品SKU(Stock Keeping Unit，库存单位)相关的业务逻辑，包括SKU的添加、修改、删除和查询等功能。
 * 商品SKU是电商系统中最小的库存单位，代表了特定规格组合的商品实体，管理着价格、库存等核心商品属性。
 * 使用了Spring缓存机制对SKU信息进行缓存，提高查询效率，减少数据库压力。
 * 使用MyBatis-Plus的ServiceImpl简化数据访问操作。
 *
 * @author 31815
 * @description 针对表【product_sku(商品SKU表)】的数据库操作Service实现
 * @createDate 2025-02-24 12:04:01
 */
@Service
@CacheConfig(cacheNames = "productSkus") // 指定该服务类的缓存名称为"productSkus"
public class ProductSkuServiceImpl extends ServiceImpl<ProductSkuMapper, ProductSku>
        implements ProductSkuService {

    @Autowired
    private ProductSkuMapper productSkuMapper;

    /**
     * 根据商品ID查询SKU列表
     * <p>
     * 该方法从缓存或数据库获取指定商品的所有SKU信息，
     * 用于商品详情页展示不同规格组合的价格、库存等信息
     *
     * @param productId 商品ID
     * @return 商品SKU列表
     */
    @Override
    @Cacheable(value = "productSkus", key = "#productId") // 缓存商品SKU信息，提高查询效率
    public List<ProductSku> selectByProductId(Long productId) {
        return productSkuMapper.selectByProductId(productId);
    }

    /**
     * 分页查询商品SKU数据
     * <p>
     * 该方法用于后台管理系统分页查看商品SKU数据，
     * 便于管理员批量管理和查看SKU信息
     *
     * @param page 分页参数
     * @return 商品SKU分页数据
     */
    @Override
    public IPage<ProductSku> selectPage(IPage<ProductSku> page) {
        return productSkuMapper.selectPage(page);
    }

    /**
     * 根据ID查询商品SKU
     * <p>
     * 该方法从缓存或数据库获取指定ID的SKU详情，
     * 用于购物车、订单创建等场景获取准确的价格和库存信息
     *
     * @param id SKU ID
     * @return 商品SKU实体
     */
    @Override
    @Cacheable(value = "productSkus", key = "#id") // 缓存SKU详情，提高查询效率
    public ProductSku selectById(Long id) {
        return productSkuMapper.selectById(id);
    }

    /**
     * 添加商品SKU
     * <p>
     * 该方法用于后台管理系统添加新的商品SKU，
     * 通常在商品创建或编辑过程中批量创建多个SKU，
     * 提供不同规格组合的价格和库存，并清除相关缓存
     *
     * @param productSku 商品SKU实体
     * @return 添加成功返回true，失败返回false
     */
    @Override
    @Transactional
    @CacheEvict(value = "productSkus", key = "#productSku.id") // 清除SKU缓存
    public boolean insertProductSku(ProductSku productSku) {
        return productSkuMapper.insert(productSku) > 0;
    }

    /**
     * 更新商品SKU
     * <p>
     * 该方法用于后台管理系统更新商品SKU信息，
     * 如修改价格、库存、状态等关键属性，
     * 是商品管理的核心操作之一，并清除相关缓存
     *
     * @param productSku 商品SKU实体
     * @return 更新成功返回true，失败返回false
     */
    @Override
    @Transactional
    @CacheEvict(value = "productSkus", key = "#productSku.id") // 清除SKU缓存
    public boolean updateProductSku(ProductSku productSku) {
        return productSkuMapper.updateById(productSku) > 0;
    }

    /**
     * 删除商品SKU
     * <p>
     * 该方法用于后台管理系统删除商品SKU，
     * 需要注意的是，删除SKU前应确保没有相关的订单或购物车引用，
     * 通常建议使用软删除而非物理删除，并清除相关缓存
     *
     * @param id SKU ID
     * @return 删除成功返回true，失败返回false
     */
    @Override
    @Transactional
    @CacheEvict(value = "productSkus", key = "#id") // 清除被删除SKU的缓存
    public boolean deleteProductSku(Long id) {
        return productSkuMapper.deleteById(id) > 0;
    }
}




