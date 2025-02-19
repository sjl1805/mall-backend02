package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.model.dto.category.CategoryPageDTO;
import com.example.model.entity.Category;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 商品分类管理Mapper接口
 *
 * @author 毕业设计学生
 */
public interface CategoryMapper extends BaseMapper<Category> {

    /**
     * 分页查询分类列表（带条件）
     *
     * @param page     分页参数
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    IPage<Category> selectCategoryPage(IPage<Category> page, @Param("query") CategoryPageDTO queryDTO);

    /**
     * 查询子分类列表
     *
     * @param parentId 父分类ID
     * @return 子分类列表
     */
    List<Category> selectChildren(@Param("parentId") Long parentId);

    /**
     * 更新分类状态（启用/禁用）
     *
     * @param categoryId 分类ID
     * @param status     新状态
     * @return 影响行数
     */
    int updateStatus(@Param("categoryId") Long categoryId, @Param("status") Integer status);

    /**
     * 检查分类名称唯一性
     *
     * @param name      分类名称
     * @param parentId  父分类ID
     * @param excludeId 排除的ID
     * @return 存在的记录数
     */
    int checkNameUnique(@Param("name") String name,
                        @Param("parentId") Long parentId,
                        @Param("excludeId") Long excludeId);

    /**
     * 查询激活的子分类列表
     *
     * @param parentId 父分类ID
     * @return 激活的子分类列表
     */
    @Select("SELECT * FROM category WHERE parent_id = #{parentId} AND status = 1 ORDER BY sort DESC")
    List<Category> selectActiveChildren(@Param("parentId") Long parentId);

    /**
     * 查询分类树形结构
     *
     * @param parentId 父分类ID
     * @return 树形结构分类列表
     */
    @Select("SELECT * FROM category WHERE parent_id = #{parentId} ORDER BY sort DESC")
    List<Category> selectCategoryTree(@Param("parentId") Long parentId);
}




