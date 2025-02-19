package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.ResultCode;
import com.example.exception.BusinessException;
import com.example.mapper.CategoryMapper;
import com.example.mapper.ProductsMapper;
import com.example.model.dto.product.ProductsDTO;
import com.example.model.dto.product.ProductsPageDTO;
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
 * @author 31815
 * @description 针对表【products(商品表)】的数据库操作Service实现
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

    @Override
    @Cacheable(key = "'page:' + #queryDTO.hashCode()")
    public IPage<Products> listProductsPage(ProductsPageDTO queryDTO) {
        Page<Products> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        return baseMapper.selectProductPage(page, queryDTO);
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public boolean addProduct(ProductsDTO productsDTO) {
        // 校验分类状态
        validateCategory(productsDTO.getCategoryId());

        // 检查名称唯一性
        if (checkNameExists(productsDTO.getName(), null)) {
            throw new BusinessException(ResultCode.PRODUCT_NAME_EXISTS);
        }

        Products product = new Products();
        BeanUtils.copyProperties(productsDTO, product);
        return save(product);
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public boolean updateProduct(ProductsDTO productsDTO) {
        Products existing = getById(productsDTO.getId());
        if (existing == null) {
            throw new BusinessException(ResultCode.PRODUCT_NOT_FOUND);
        }

        // 检查名称唯一性
        if (checkNameExists(productsDTO.getName(), productsDTO.getId())) {
            throw new BusinessException(ResultCode.PRODUCT_NAME_EXISTS);
        }

        BeanUtils.copyProperties(productsDTO, existing);
        return updateById(existing);
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public boolean adjustStock(Long productId, Integer delta) {
        return baseMapper.adjustStock(productId, delta) > 0;
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public boolean switchStatus(Long productId, Integer status) {
        return baseMapper.updateStatus(productId, status) > 0;
    }

    @Override
    @Cacheable(key = "'newArrivals:' + #categoryId")
    public List<Products> getNewArrivals(Long categoryId, Integer limit) {
        return baseMapper.selectNewArrivals(categoryId, limit);
    }

    private void validateCategory(Long categoryId) {
        Category category = categoryMapper.selectById(categoryId);
        if (category == null || category.getStatus() == 0) {
            throw new BusinessException(ResultCode.PRODUCT_CATEGORY_INVALID);
        }
    }

    private boolean checkNameExists(String name, Long excludeId) {
        return baseMapper.checkNameUnique(name, excludeId) > 0;
    }
}




