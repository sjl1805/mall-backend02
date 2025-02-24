package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.ProductFavoriteMapper;
import com.example.model.entity.ProductFavorite;
import com.example.service.ProductFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 31815
 * @description 针对表【product_favorite(商品收藏表)】的数据库操作Service实现
 * @createDate 2025-02-24 12:04:07
 */
@Service
@CacheConfig(cacheNames = "productFavorites")
public class ProductFavoriteServiceImpl extends ServiceImpl<ProductFavoriteMapper, ProductFavorite>
        implements ProductFavoriteService {

    @Autowired
    private ProductFavoriteMapper productFavoriteMapper;

    @Override
    @Cacheable(value = "productFavorites", key = "#userId")
    public List<ProductFavorite> selectByUserId(Long userId) {
        return productFavoriteMapper.selectByUserId(userId);
    }

    @Override
    public IPage<ProductFavorite> selectPage(IPage<ProductFavorite> page) {
        return productFavoriteMapper.selectPage(page);
    }

    @Override
    @Cacheable(value = "productFavorites", key = "#id")
    public ProductFavorite selectById(Long id) {
        return productFavoriteMapper.selectById(id);
    }

    @Override
    @CacheEvict(value = "productFavorites", key = "#productFavorite.id")
    public boolean insertProductFavorite(ProductFavorite productFavorite) {
        return productFavoriteMapper.insert(productFavorite) > 0;
    }

    @Override
    @CacheEvict(value = "productFavorites", key = "#productFavorite.id")
    public boolean updateProductFavorite(ProductFavorite productFavorite) {
        return productFavoriteMapper.updateById(productFavorite) > 0;
    }

    @Override
    @CacheEvict(value = "productFavorites", key = "#id")
    public boolean deleteProductFavorite(Long id) {
        return productFavoriteMapper.deleteById(id) > 0;
    }
}




