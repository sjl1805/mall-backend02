package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.dto.CategoryDTO;
import com.example.model.dto.PageDTO;
import com.example.model.entity.Category;

import java.util.List;

/**
 * 商品分类服务接口
 * 
 * @author 31815
 * @description 提供商品分类的增删改查、状态管理及树形结构查询等核心业务功能
 * @createDate 2025-02-18 23:44:29
 */
public interface CategoryService extends IService<Category> {
    
    /**
     * 分页查询分类（支持树形结构展示）
     * @param queryDTO 分页查询参数，包含页码、页大小及过滤条件
     * @return 分页结果对象，包含当前页数据和分页信息
     */
    IPage<CategoryDTO> listCategoryPage(PageDTO<CategoryDTO> queryDTO);

    /**
     * 新增商品分类
     * @param categoryDTO 分类数据传输对象，包含分类基本信息
     * @return 操作是否成功
     * @throws com.example.exception.BusinessException 当父分类无效或名称重复时抛出
     */
    boolean addCategory(CategoryDTO categoryDTO);

    /**
     * 更新商品分类信息
     * @param categoryDTO 分类数据传输对象，需包含分类ID
     * @return 操作是否成功
     * @throws com.example.exception.BusinessException 当分类不存在、层级变更或名称重复时抛出
     */
    boolean updateCategory(CategoryDTO categoryDTO);

    /**
     * 删除分类及其子分类（级联删除）
     * @param id 要删除的分类ID
     * @return 操作是否成功
     */
    boolean deleteCategory(Long id);

    /**
     * 获取全部分类树形结构
     * @return 嵌套结构的分类列表，适合前端树形控件展示
     */
    List<CategoryDTO> getCategoryTree(Long parentId);

    /**
     * 切换分类状态（启用/禁用）
     * @param id 目标分类ID
     * @param status 新状态（1-启用，0-禁用）
     * @return 操作是否成功
     * @throws com.example.exception.BusinessException 当分类不存在时抛出
     */
    boolean switchStatus(Long id, Integer status);
}
