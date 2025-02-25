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
 * 商品分类Mapper接口
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

    /**
     * 查询所有分类（树形结构）
     *
     * @return 分类树
     */
    List<Category> selectCategoryTree();

    /**
     * 根据父ID查询子分类
     *
     * @param parentId 父分类ID
     * @return 子分类列表
     */
    List<Category> selectByParentId(@Param("parentId") Long parentId);

    /**
     * 根据层级查询分类
     *
     * @param level 层级：1一级 2二级 3三级
     * @return 分类列表
     */
    List<Category> selectByLevel(@Param("level") Integer level);

    /**
     * 分页查询分类（支持多条件筛选）
     *
     * @param page 分页对象
     * @param params 查询参数
     * @return 分页分类列表
     */
    IPage<Category> selectCategoryPage(Page<Category> page, @Param("params") Map<String, Object> params);

    /**
     * 批量更新分类状态
     *
     * @param ids 分类ID列表
     * @param status 状态：0-禁用 1-启用
     * @return 影响行数
     */
    int batchUpdateStatus(@Param("ids") List<Long> ids, @Param("status") Integer status);

    /**
     * 更新分类的父级
     *
     * @param id 分类ID
     * @param parentId 新父分类ID
     * @param level 新层级
     * @return 影响行数
     */
    int updateParent(@Param("id") Long id, @Param("parentId") Long parentId, @Param("level") Integer level);

    /**
     * 统计各层级分类数量
     *
     * @return 统计结果
     */
    @MapKey("level")
    List<Map<String, Object>> countCategoryByLevel();

    /**
     * 获取分类路径（从根到当前分类的完整路径）
     *
     * @param categoryId 分类ID
     * @return 分类路径列表
     */
    List<Category> selectCategoryPath(@Param("categoryId") Long categoryId);

    /**
     * 查询所有启用状态的分类
     *
     * @return 分类列表
     */
    List<Category> selectAllEnabled();

    /**
     * 查询有商品关联的分类
     *
     * @return 分类列表
     */
    List<Category> selectCategoriesWithProducts();

    /**
     * 查询热门分类（根据关联商品数量）
     *
     * @param limit 限制数量
     * @return 热门分类
     */
    List<Category> selectHotCategories(@Param("limit") Integer limit);

    /**
     * 递归删除分类（包括所有子分类）
     *
     * @param categoryId 分类ID
     * @return 影响行数
     */
    int deleteWithChildren(@Param("categoryId") Long categoryId);
    
    /**
     * 检查分类名称是否存在（同一父级下）
     *
     * @param name 分类名称
     * @param parentId 父分类ID
     * @return 存在数量
     */
    int existsByNameAndParent(@Param("name") String name, @Param("parentId") Long parentId);
} 