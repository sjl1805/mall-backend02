package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.dto.category.CategoryDTO;
import com.example.model.dto.category.CategoryPageDTO;
import com.example.model.entity.Category;

import java.util.List;

/**
 * @author 31815
 * @description 针对表【category(商品分类表)】的数据库操作Service
 * @createDate 2025-02-18 23:44:29
 */
public interface CategoryService extends IService<Category> {
    IPage<Category> listCategoryPage(CategoryPageDTO queryDTO);

    boolean addCategory(CategoryDTO categoryDTO);

    boolean updateCategory(CategoryDTO categoryDTO);

    boolean deleteCategory(Long id);

    List<Category> getCategoryTree();

    boolean switchStatus(Long id, Integer status);
}
