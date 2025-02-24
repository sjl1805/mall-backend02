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

import java.util.List;

/**
 * @author 31815
 * @description 针对表【product_sku(商品SKU表)】的数据库操作Service实现
 * @createDate 2025-02-24 12:04:01
 */
@Service
@CacheConfig(cacheNames = "productSkus")
public class ProductSkuServiceImpl extends ServiceImpl<ProductSkuMapper, ProductSku>
        implements ProductSkuService {

    @Autowired
    private ProductSkuMapper productSkuMapper;

    @Override
    @Cacheable(value = "productSkus", key = "#productId")
    public List<ProductSku> selectByProductId(Long productId) {
        return productSkuMapper.selectByProductId(productId);
    }

    @Override
    public IPage<ProductSku> selectPage(IPage<ProductSku> page) {
        return productSkuMapper.selectPage(page);
    }

    @Override
    @Cacheable(value = "productSkus", key = "#id")
    public ProductSku selectById(Long id) {
        return productSkuMapper.selectById(id);
    }

    @Override
    @CacheEvict(value = "productSkus", key = "#productSku.id")
    public boolean insertProductSku(ProductSku productSku) {
        return productSkuMapper.insert(productSku) > 0;
    }

    @Override
    @CacheEvict(value = "productSkus", key = "#productSku.id")
    public boolean updateProductSku(ProductSku productSku) {
        return productSkuMapper.updateById(productSku) > 0;
    }

    @Override
    @CacheEvict(value = "productSkus", key = "#id")
    public boolean deleteProductSku(Long id) {
        return productSkuMapper.deleteById(id) > 0;
    }
}




