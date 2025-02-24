package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.Category;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.List;
/**
 * @author 31815
 * @description 针对表【category(商品分类表)】的数据库操作Service
 * @createDate 2025-02-24 12:04:22
 */
public interface CategoryService extends IService<Category> {

    /**
     * 根据分类名称查询分类
     * @param name 分类名称
     * @return 分类列表
     */
    List<Category> selectByName(String name);

    /**
     * 分页查询分类
     * @param page 分页信息
     * @return 分类列表
     */
    IPage<Category> selectPage(IPage<Category> page);

    /**
     * 根据ID查询分类
     * @param id 分类ID
     * @return 分类信息
     */
    Category selectById(Long id);

    /**
     * 新增分类
     * @param category 分类信息
     * @return 插入结果
     */
    boolean insertCategory(Category category);

    /**
     * 更新分类信息
     * @param category 分类信息
     * @return 更新结果
     */
    boolean updateCategory(Category category);

    /**
     * 根据ID删除分类
     * @param id 分类ID
     * @return 删除结果
     */
    boolean deleteCategory(Long id);
}
