package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.model.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 商品分类Mapper接口
 * 
 * 该接口提供对商品分类表(category)的数据库操作方法
 * 继承MyBatis-Plus的BaseMapper获取基础的CRUD功能
 * 同时扩展了特定业务需求的查询方法
 *
 * @author 31815
 * @description 针对表【category(商品分类表)】的数据库操作Mapper
 * @createDate 2025-02-24 12:04:22
 * @Entity model.entity.Category
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

    /**
     * 根据分类名称模糊查询
     *
     * @param name 分类名称（模糊匹配）
     * @return 分类列表
     */
    List<Category> selectByNameLike(@Param("name") String name);

    /**
     * 根据分类名称查询分类
     *
     * @param name 分类名称
     * @return 分类列表
     */
    List<Category> selectByName(@Param("name") String name);

    /**
     * 分页查询分类（支持排序）
     *
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<Category> selectPage(IPage<Category> page);

    /**
     * 根据父分类ID查询子分类
     *
     * @param parentId 父分类ID
     * @return 子分类列表
     */
    List<Category> selectByParentId(@Param("parentId") Long parentId);

    /**
     * 根据层级查询分类
     *
     * @param level 分类层级（1-一级 2-二级 3-三级）
     * @return 分类列表
     */
    List<Category> selectByLevel(@Param("level") Integer level);

    /**
     * 插入分类
     *
     * @param category 分类信息
     * @return 插入结果
     */
    int insertCategory(Category category);

    /**
     * 更新分类
     *
     * @param category 分类信息
     * @return 更新结果
     */
    int updateCategory(Category category);

    /**
     * 删除分类
     *
     * @param id 分类ID
     * @return 删除结果
     */
    int deleteCategory(@Param("id") Long id);

    /**
     * 查询所有分类并按层级组织
     *
     * @return 分类树结构
     */
    List<Map<String, Object>> selectCategoryTree();

    /**
     * 根据父ID查询子分类（包含子分类的商品数量）
     *
     * @param parentId 父分类ID
     * @return 子分类列表
     */
    List<Map<String, Object>> selectChildrenWithProductCount(@Param("parentId") Long parentId);

    /**
     * 更新分类排序
     *
     * @param id 分类ID
     * @param sort 排序值
     * @return 更新结果
     */
    int updateSort(@Param("id") Long id, @Param("sort") Integer sort);

    /**
     * 更新分类状态
     *
     * @param id 分类ID
     * @param status 状态值
     * @return 更新结果
     */
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 获取分类路径（面包屑导航）
     *
     * @param categoryId 分类ID
     * @return 分类路径
     */
    List<Category> selectCategoryPath(@Param("categoryId") Long categoryId);

    /**
     * 批量删除分类
     *
     * @param ids 分类ID列表
     * @return 删除结果
     */
    int batchDeleteCategories(@Param("ids") List<Long> ids);

    /**
     * 批量更新分类状态
     *
     * @param ids 分类ID列表
     * @param status 状态值
     * @return 更新结果
     */
    int batchUpdateStatus(
            @Param("ids") List<Long> ids,
            @Param("status") Integer status);

    /**
     * 查询指定分类下的商品数量
     *
     * @param categoryId 分类ID
     * @return 商品数量
     */
    Integer countProductsByCategoryId(@Param("categoryId") Long categoryId);
    
    /**
     * 查询指定分类是否有子分类
     *
     * @param categoryId 分类ID
     * @return 子分类数量
     */
    Integer countChildCategories(@Param("categoryId") Long categoryId);
    
    /**
     * 递归查询所有子分类ID（包括自身）
     *
     * @param categoryId 分类ID
     * @return 所有子分类ID列表
     */
    List<Long> selectAllChildCategoryIds(@Param("categoryId") Long categoryId);
    
    /**
     * 根据关键词搜索分类
     *
     * @param keyword 关键词
     * @return 匹配的分类列表
     */
    List<Category> searchCategories(@Param("keyword") String keyword);
    
    /**
     * 查询热门分类（包含商品数量最多的分类）
     *
     * @param limit 查询数量
     * @return 热门分类列表
     */
    List<Map<String, Object>> selectHotCategories(@Param("limit") Integer limit);
}




