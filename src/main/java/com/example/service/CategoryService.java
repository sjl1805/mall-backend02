package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.Category;

import java.util.List;

/**
 * 商品分类服务接口
 */
public interface CategoryService extends IService<Category> {
    
    /**
     * 获取分类树结构
     *
     * @return 分类树
     */
    List<Category> getCategoryTree();
    
    /**
     * 获取指定父分类的子分类
     *
     * @param parentId 父分类ID
     * @return 子分类列表
     */
    List<Category> getChildCategories(Long parentId);
    
    /**
     * 获取指定层级的分类列表
     *
     * @param level 层级
     * @return 分类列表
     */
    List<Category> getCategoriesByLevel(Integer level);
    
    /**
     * 添加分类
     *
     * @param category 分类信息
     * @return 添加成功的分类
     */
    Category addCategory(Category category);
    
    /**
     * 更新分类
     *
     * @param category 分类信息
     * @return 是否更新成功
     */
    boolean updateCategory(Category category);
    
    /**
     * 更新分类状态
     *
     * @param categoryId 分类ID
     * @param status 状态：0-禁用 1-启用
     * @return 是否更新成功
     */
    boolean updateCategoryStatus(Long categoryId, Integer status);
} 