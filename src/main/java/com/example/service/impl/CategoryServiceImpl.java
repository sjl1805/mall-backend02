package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.CategoryMapper;
import com.example.model.entity.Category;
import com.example.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CacheConfig;

import java.util.List;

/**
 * @author 31815
 * @description 针对表【category(商品分类表)】的数据库操作Service实现
 * @createDate 2025-02-24 12:04:22
 */
@Service
@CacheConfig(cacheNames = "categories")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
        implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    @Cacheable(value = "categories", key = "#name")
    public List<Category> selectByName(String name) {
        return categoryMapper.selectByName(name);
    }

    @Override
    public IPage<Category> selectPage(IPage<Category> page) {
        return categoryMapper.selectPage(page);
    }

    @Override
    @Cacheable(value = "categories", key = "#id")
    public Category selectById(Long id) {
        return categoryMapper.selectById(id);
    }

    @Override
    @CacheEvict(value = "categories", key = "#category.id")
    public boolean insertCategory(Category category) {
        return categoryMapper.insert(category) > 0;
    }

    @Override
    @CacheEvict(value = "categories", key = "#category.id")
    public boolean updateCategory(Category category) {
        return categoryMapper.updateById(category) > 0;
    }

    @Override
    @CacheEvict(value = "categories", key = "#id")
    public boolean deleteCategory(Long id) {
        return categoryMapper.deleteById(id) > 0;
    }
}




