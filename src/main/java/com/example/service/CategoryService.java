package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.Category;

import java.util.List;
import java.util.Map;

/**
 * 商品分类服务接口
 */
public interface CategoryService extends IService<Category> {

    /**
     * 查询所有分类（树形结构）
     *
     * @return 分类树
     */
    List<Category> getCategoryTree();

    /**
     * 根据父ID查询子分类
     *
     * @param parentId 父分类ID
     * @return 子分类列表
     */
    List<Category> getChildrenByParentId(Long parentId);

    /**
     * 根据层级查询分类
     *
     * @param level 层级：1一级 2二级 3三级
     * @return 分类列表
     */
    List<Category> getListByLevel(Integer level);

    /**
     * 分页查询分类
     *
     * @param page 分页对象
     * @param params 查询参数
     * @return 分页分类列表
     */
    IPage<Category> getCategoryPage(Page<Category> page, Map<String, Object> params);

    /**
     * 创建分类
     *
     * @param category 分类信息
     * @return 创建的分类
     */
    Category createCategory(Category category);

    /**
     * 更新分类
     *
     * @param category 分类信息
     * @return 是否更新成功
     */
    boolean updateCategory(Category category);

    /**
     * 删除分类
     *
     * @param id 分类ID
     * @return 是否删除成功
     */
    boolean deleteCategory(Long id);

    /**
     * 递归删除分类（包括所有子分类）
     *
     * @param id 分类ID
     * @return 是否删除成功
     */
    boolean deleteCategoryWithChildren(Long id);

    /**
     * 批量更新分类状态
     *
     * @param ids 分类ID列表
     * @param status 状态：0-禁用 1-启用
     * @return 是否更新成功
     */
    boolean batchUpdateStatus(List<Long> ids, Integer status);

    /**
     * 更新分类的父级
     *
     * @param id 分类ID
     * @param parentId 新父分类ID
     * @return 是否更新成功
     */
    boolean updateCategoryParent(Long id, Long parentId);

    /**
     * 统计各层级分类数量
     *
     * @return 统计结果
     */
    List<Map<String, Object>> countCategoryByLevel();

    /**
     * 获取分类路径（从根到当前分类的完整路径）
     *
     * @param categoryId 分类ID
     * @return 分类路径列表
     */
    List<Category> getCategoryPath(Long categoryId);

    /**
     * 查询所有启用状态的分类
     *
     * @return 分类列表
     */
    List<Category> getAllEnabledCategories();

    /**
     * 查询有商品关联的分类
     *
     * @return 分类列表
     */
    List<Category> getCategoriesWithProducts();

    /**
     * 查询热门分类（根据关联商品数量）
     *
     * @param limit 限制数量
     * @return 热门分类列表
     */
    List<Category> getHotCategories(Integer limit);
    
    /**
     * 检查分类名称是否存在（同一父级下）
     *
     * @param name 分类名称
     * @param parentId 父分类ID
     * @return 是否存在
     */
    boolean checkNameExists(String name, Long parentId);
    
    /**
     * 获取完整分类树（包含子分类的所有信息）
     *
     * @return 完整分类树
     */
    List<Category> getFullCategoryTree();
    
    /**
     * 获取分类的所有子分类ID（包括子分类的子分类）
     *
     * @param categoryId 分类ID
     * @return 所有子分类ID集合
     */
    List<Long> getAllChildCategoryIds(Long categoryId);
} 