package com.example.service.impl;

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

import java.util.List;

/**
 * @author 31815
 * @description 针对表【products(商品表)】的数据库操作Service实现
 * @createDate 2025-02-24 12:03:56
 */
@Service
@CacheConfig(cacheNames = "products")
public class ProductsServiceImpl extends ServiceImpl<ProductsMapper, Products>
        implements ProductsService {

    @Autowired
    private ProductsMapper productsMapper;

    @Override
    @Cacheable(value = "products", key = "#name")
    public List<Products> selectByName(String name) {
        return productsMapper.selectByName(name);
    }

    @Override
    public IPage<Products> selectPage(IPage<Products> page) {
        return productsMapper.selectPage(page);
    }

    @Override
    @Cacheable(value = "products", key = "#id")
    public Products selectById(Long id) {
        return productsMapper.selectById(id);
    }

    @Override
    @CacheEvict(value = "products", key = "#product.id")
    public boolean insertProduct(Products product) {
        return productsMapper.insert(product) > 0;
    }

    @Override
    @CacheEvict(value = "products", key = "#product.id")
    public boolean updateProduct(Products product) {
        return productsMapper.updateById(product) > 0;
    }

    @Override
    @CacheEvict(value = "products", key = "#id")
    public boolean deleteProduct(Long id) {
        return productsMapper.deleteById(id) > 0;
    }
}




