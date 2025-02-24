package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.ProductSpecMapper;
import com.example.model.entity.ProductSpec;
import com.example.service.ProductSpecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 31815
 * @description 针对表【product_spec(商品规格表)】的数据库操作Service实现
 * @createDate 2025-02-24 12:03:58
 */
@Service
@CacheConfig(cacheNames = "productSpecs")
public class ProductSpecServiceImpl extends ServiceImpl<ProductSpecMapper, ProductSpec>
        implements ProductSpecService {

    @Autowired
    private ProductSpecMapper productSpecMapper;

    @Override
    @Cacheable(value = "productSpecs", key = "#productId")
    public List<ProductSpec> selectByProductId(Long productId) {
        return productSpecMapper.selectByProductId(productId);
    }

    @Override
    public IPage<ProductSpec> selectPage(IPage<ProductSpec> page) {
        return productSpecMapper.selectPage(page);
    }

    @Override
    @Cacheable(value = "productSpecs", key = "#id")
    public ProductSpec selectById(Long id) {
        return productSpecMapper.selectById(id);
    }

    @Override
    @CacheEvict(value = "productSpecs", key = "#productSpec.id")
    public boolean insertProductSpec(ProductSpec productSpec) {
        return productSpecMapper.insert(productSpec) > 0;
    }

    @Override
    @CacheEvict(value = "productSpecs", key = "#productSpec.id")
    public boolean updateProductSpec(ProductSpec productSpec) {
        return productSpecMapper.updateById(productSpec) > 0;
    }

    @Override
    @CacheEvict(value = "productSpecs", key = "#id")
    public boolean deleteProductSpec(Long id) {
        return productSpecMapper.deleteById(id) > 0;
    }
}




