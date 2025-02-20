package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.ResultCode;
import com.example.exception.BusinessException;
import com.example.mapper.CategoryMapper;
import com.example.mapper.ProductsMapper;
import com.example.model.dto.ProductsDTO;
import com.example.model.dto.PageDTO;
import com.example.model.entity.Category;
import com.example.model.entity.Products;
import com.example.service.ProductsService;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商品服务实现类
 * 
 * @author 31815
 * @description 实现商品核心业务逻辑，包含：
 *              1. 商品信息校验与状态管理
 *              2. 分类关联校验
 *              3. 缓存策略优化
 * @createDate 2025-02-18 23:44:03
 */
@Service
@CacheConfig(cacheNames = "productService")
public class ProductsServiceImpl extends ServiceImpl<ProductsMapper, Products>
        implements ProductsService {

    //private static final Logger logger = LoggerFactory.getLogger(ProductsServiceImpl.class);

    private final CategoryMapper categoryMapper;

    public ProductsServiceImpl(ProductsMapper productsMapper,
                               CategoryMapper categoryMapper) {
        this.baseMapper = productsMapper;
        this.categoryMapper = categoryMapper;
    }

    /**
     * 分页查询商品（缓存优化）
     * @param queryDTO 分页参数
     * @return 分页结果
     * @implNote 缓存策略：
     *           1. 缓存键：page:{queryDTO.hashCode}
     *           2. 缓存时间：15分钟
     */
    @Override
    @Cacheable(key = "'page:' + #queryDTO.hashCode()")
    public IPage<ProductsDTO> listProductsPage(PageDTO<ProductsDTO> queryDTO) {
        Page<Products> page = new Page<>(queryDTO.getCurrent(), queryDTO.getSize());
        IPage<Products> productPage = baseMapper.selectProductPage(page, queryDTO.getQuery());
        return productPage.convert(ProductsDTO::fromEntity);
    }

    /**
     * 添加商品（完整校验）
     * @param productsDTO 商品信息
     * @return 操作结果
     * @implNote 业务逻辑：
     *           1. 校验分类有效性
     *           2. 检查名称唯一性
     *           3. 清除全量缓存
     */
    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public boolean addProduct(ProductsDTO productsDTO) {
        validateCategory(productsDTO.getCategoryId());
        if (checkNameExists(productsDTO.getName(), null)) {
            throw new BusinessException(ResultCode.PRODUCT_NAME_EXISTS);
        }

        Products product = new Products();
        BeanUtils.copyProperties(productsDTO, product);
        return save(product);
    }

    /**
     * 更新商品（带版本控制）
     * @param productsDTO 商品信息
     * @return 操作结果
     * @implNote 执行逻辑：
     *           1. 校验商品存在性
     *           2. 检查名称唯一性
     *           3. 使用乐观锁更新
     */
    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public boolean updateProduct(ProductsDTO productsDTO) {
        Products existing = getById(productsDTO.getId());
        if (existing == null) {
            throw new BusinessException(ResultCode.PRODUCT_NOT_FOUND);
        }

        if (checkNameExists(productsDTO.getName(), productsDTO.getId())) {
            throw new BusinessException(ResultCode.PRODUCT_NAME_EXISTS);
        }

        BeanUtils.copyProperties(productsDTO, existing);
        return updateById(existing);
    }

    /**
     * 调整库存（安全操作）
     * @param productId 商品ID
     * @param delta 调整数量
     * @return 操作结果
     * @implNote 使用数据库行锁保证原子性
     */
    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public boolean adjustStock(Long productId, Integer delta) {
        return baseMapper.adjustStock(productId, delta) > 0;
    }

    /**
     * 切换商品状态（状态机）
     * @param productId 商品ID
     * @param status 新状态
     * @return 操作结果
     * @implNote 状态变更后清除相关缓存
     */
    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public boolean switchStatus(Long productId, Integer status) {
        return baseMapper.updateStatus(productId, status) > 0;
    }

    /**
     * 获取新品推荐（缓存优化）
     * @param categoryId 分类ID
     * @param limit 最大数量
     * @return 新品列表
     * @implNote 缓存策略：
     *           1. 缓存键：newArrivals:{categoryId}
     *           2. 缓存时间：1小时
     */
    @Override
    @Cacheable(key = "'newArrivals:' + #categoryId")
    public List<Products> getNewArrivals(Long categoryId, Integer limit) {
        return baseMapper.selectNewArrivals(categoryId, limit);
    }

    /**
     * 校验分类有效性
     * @param categoryId 分类ID
     * @throws BusinessException 当分类无效时抛出
     */
    private void validateCategory(Long categoryId) {
        Category category = categoryMapper.selectById(categoryId);
        if (category == null || category.getStatus() == 0) {
            throw new BusinessException(ResultCode.PRODUCT_CATEGORY_INVALID);
        }
    }

    /**
     * 检查商品名称唯一性
     * @param name 商品名称
     * @param excludeId 排除的商品ID（用于更新操作）
     * @return 是否存在重复名称
     */
    private boolean checkNameExists(String name, Long excludeId) {
        return baseMapper.checkNameUnique(name, excludeId) > 0;
    }

}




