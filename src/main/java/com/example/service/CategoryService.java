package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.Category;

import java.util.List;
import java.util.Map;

/**
 * 商品分类服务接口
 * 
 * 定义了商品分类模块的所有业务功能，包括基础的CRUD操作和高级查询功能
 * 该接口继承了MyBatis-Plus的IService接口，获取通用的服务方法
 *
 * @author 31815
 * @description 针对表【category(商品分类表)】的数据库操作Service
 * @createDate 2025-02-24 12:04:22
 */
public interface CategoryService extends IService<Category> {

    /**
     * 根据分类名称查询分类
     *
     * @param name 分类名称
     * @return 分类列表
     */
    List<Category> selectByName(String name);

    /**
     * 分页查询分类
     *
     * @param page 分页信息
     * @return 分类列表
     */
    IPage<Category> selectPage(IPage<Category> page);

    /**
     * 根据ID查询分类
     *
     * @param id 分类ID
     * @return 分类信息
     */
    Category selectById(Long id);

    /**
     * 新增分类
     *
     * @param category 分类信息
     * @return 插入结果
     */
    boolean insertCategory(Category category);

    /**
     * 更新分类信息
     *
     * @param category 分类信息
     * @return 更新结果
     */
    boolean updateCategory(Category category);

    /**
     * 根据ID删除分类
     *
     * @param id 分类ID
     * @return 删除结果
     */
    boolean deleteCategory(Long id);
    
    /**
     * 根据父ID查询子分类
     *
     * @param parentId 父分类ID
     * @return 子分类列表
     */
    List<Category> selectByParentId(Long parentId);
    
    /**
     * 根据分类层级查询分类
     *
     * @param level 分类层级（1-一级 2-二级 3-三级）
     * @return 分类列表
     */
    List<Category> selectByLevel(Integer level);
    
    /**
     * 获取分类树结构
     *
     * @return 分类树结构
     */
    List<Map<String, Object>> selectCategoryTree();
    
    /**
     * 获取子分类及其商品数量
     *
     * @param parentId 父分类ID
     * @return 子分类及商品数量列表
     */
    List<Map<String, Object>> selectChildrenWithProductCount(Long parentId);
    
    /**
     * 更新分类排序
     *
     * @param id 分类ID
     * @param sort 排序值
     * @return 更新结果
     */
    boolean updateSort(Long id, Integer sort);
    
    /**
     * 更新分类状态
     *
     * @param id 分类ID
     * @param status 状态值
     * @return 更新结果
     */
    boolean updateStatus(Long id, Integer status);
    
    /**
     * 获取分类路径（面包屑导航）
     *
     * @param categoryId 分类ID
     * @return 分类路径
     */
    List<Category> selectCategoryPath(Long categoryId);
    
    /**
     * 批量删除分类
     *
     * @param ids 分类ID列表
     * @return 删除结果
     */
    boolean batchDeleteCategories(List<Long> ids);
    
    /**
     * 批量更新分类状态
     *
     * @param ids 分类ID列表
     * @param status 状态值
     * @return 更新结果
     */
    boolean batchUpdateStatus(List<Long> ids, Integer status);
    
    /**
     * 检查分类是否可以安全删除
     * 
     * @param categoryId 分类ID
     * @return 如果可以安全删除返回true，否则返回false
     */
    boolean checkCanDelete(Long categoryId);
    
    /**
     * 批量导入分类数据
     *
     * @param categories 分类数据列表
     * @return 导入成功的数量
     */
    int batchImport(List<Category> categories);
    
    /**
     * 导出分类数据
     *
     * @param level 分类层级（可选）
     * @return 分类数据列表
     */
    List<Category> exportCategories(Integer level);
    
    /**
     * 查询热门分类
     *
     * @param limit 查询数量限制
     * @return 热门分类列表
     */
    List<Map<String, Object>> getHotCategories(Integer limit);
    
    /**
     * 统计每个层级的分类数量
     *
     * @return 层级-数量映射
     */
    Map<Integer, Integer> countCategoriesByLevel();
    
    /**
     * 根据关键词搜索分类
     *
     * @param keyword 搜索关键词
     * @return 匹配的分类列表
     */
    List<Category> searchCategories(String keyword);
    
    /**
     * 获取完整的分类树（包含所有层级和商品数量）
     *
     * @return 完整分类树结构
     */
    List<Category> getFullCategoryTree();
}
