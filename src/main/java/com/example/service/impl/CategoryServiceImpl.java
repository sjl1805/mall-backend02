package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.exception.BusinessException;
import com.example.mapper.CategoryMapper;
import com.example.model.entity.Category;
import com.example.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商品分类服务实现类
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Override
    public List<Category> getCategoryTree() {
        // 获取所有启用的分类
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getStatus, 1)
                .orderByAsc(Category::getLevel)
                .orderByAsc(Category::getId);
        
        List<Category> allCategories = list(queryWrapper);
        
        // 按parentId分组
        Map<Long, List<Category>> parentIdMap = allCategories.stream()
                .collect(Collectors.groupingBy(Category::getParentId));
        
        // 获取一级分类
        List<Category> rootCategories = parentIdMap.getOrDefault(0L, new ArrayList<>());
        
        // 递归设置子分类
        rootCategories.forEach(root -> setChildCategories(root, parentIdMap));
        
        return rootCategories;
    }
    
    /**
     * 递归设置子分类
     */
    private void setChildCategories(Category parent, Map<Long, List<Category>> parentIdMap) {
        List<Category> children = parentIdMap.getOrDefault(parent.getId(), new ArrayList<>());
        parent.setChildren(children);
        children.forEach(child -> setChildCategories(child, parentIdMap));
    }

    @Override
    public List<Category> getChildCategories(Long parentId) {
        return baseMapper.findByParentId(parentId);
    }

    @Override
    public List<Category> getCategoriesByLevel(Integer level) {
        return baseMapper.findByLevel(level);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Category addCategory(Category category) {
        // 检查父分类是否存在
        if (category.getParentId() != 0) {
            Category parent = getById(category.getParentId());
            if (parent == null) {
                throw new BusinessException("父分类不存在");
            }
            
            // 设置分类层级
            category.setLevel(parent.getLevel() + 1);
        } else {
            // 一级分类
            category.setLevel(1);
        }
        
        // 设置默认值
        category.setStatus(category.getStatus() == null ? 1 : category.getStatus());
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        
        // 保存分类
        save(category);
        
        return category;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateCategory(Category category) {
        if (category == null || category.getId() == null) {
            throw new BusinessException("分类ID不能为空");
        }
        
        // 不允许修改的字段设为null
        category.setParentId(null);
        category.setLevel(null);
        category.setCreateTime(null);
        
        category.setUpdateTime(LocalDateTime.now());
        
        return updateById(category);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateCategoryStatus(Long categoryId, Integer status) {
        if (status != 0 && status != 1) {
            throw new BusinessException("状态参数错误");
        }
        
        LambdaUpdateWrapper<Category> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Category::getId, categoryId)
                .set(Category::getStatus, status)
                .set(Category::getUpdateTime, LocalDateTime.now());
        
        return update(updateWrapper);
    }
} 