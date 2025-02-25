package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.model.entity.Category;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 商品分类表 Mapper 接口
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

    /**
     * 获取所有一级分类
     *
     * @return 一级分类列表
     */
    List<Category> selectTopCategories();

    /**
     * 获取指定父分类的所有子分类
     *
     * @param parentId 父分类ID
     * @return 子分类列表
     */
    List<Category> selectChildCategories(@Param("parentId") Long parentId);

    /**
     * 获取完整的分类树
     *
     * @return 分类树列表（一级分类）
     */
    @MapKey("id")
    List<Map<String, Object>> selectCategoryTree();

    /**
     * 获取分类的完整层级路径
     *
     * @param categoryId 分类ID
     * @return 完整路径（从一级到当前分类）
     */
    List<Category> selectCategoryPath(@Param("categoryId") Long categoryId);

    /**
     * 更新分类状态
     *
     * @param categoryId 分类ID
     * @param status 状态：0-禁用 1-启用
     * @return 影响行数
     */
    int updateStatus(@Param("categoryId") Long categoryId, @Param("status") Integer status);

    /**
     * 分页查询分类列表
     *
     * @param page 分页参数
     * @param params 查询参数
     * @return 分类列表
     */
    IPage<Category> selectCategoryPage(Page<Category> page, @Param("params") Map<String, Object> params);

    /**
     * 批量删除分类及其所有子分类
     *
     * @param categoryIds 分类ID列表
     * @return 影响行数
     */
    int deleteBatchWithChildren(@Param("categoryIds") List<Long> categoryIds);

    /**
     * 查询指定分类下的商品数量
     *
     * @param categoryId 分类ID
     * @return 商品数量
     */
    int selectProductCount(@Param("categoryId") Long categoryId);

    /**
     * 查询分类是否有子分类
     *
     * @param categoryId 分类ID
     * @return 子分类数量
     */
    int selectChildCount(@Param("categoryId") Long categoryId);
    
    /**
     * 根据名称模糊查询分类
     *
     * @param name 分类名称
     * @return 分类列表
     */
    List<Category> selectByNameLike(@Param("name") String name);
    
    /**
     * 查询所有启用状态的分类
     *
     * @return 启用的分类列表
     */
    List<Category> selectEnabledCategories();
} 