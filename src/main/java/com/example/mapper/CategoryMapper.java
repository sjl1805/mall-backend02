package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.entity.Category;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author 31815
 * @description 针对表【category(商品分类表)】的数据库操作Mapper
 * @createDate 2025-02-24 12:04:22
 * @Entity model.entity.Category
 */
public interface CategoryMapper extends BaseMapper<Category> {

    /**
     * 递归查询子分类
     * @param categoryId 起始分类ID
     * @return 所有子分类列表
     */
    List<Category> selectChildrenRecursive(@Param("categoryId") Long categoryId);

    /**
     * 同级分类排序调整
     * @param parentId 父分类ID
     * @param sortedIds 排序后的ID顺序列表
     * @return 影响行数
     */
    int updateSortByParent(@Param("parentId") Long parentId, @Param("sortedIds") List<Long> sortedIds);

    /**
     * 批量修改分类状态
     * @param ids 分类ID列表
     * @param status 新状态
     * @return 影响行数
     */
    int updateStatusBatch(@Param("ids") List<Long> ids, @Param("status") Integer status);

    /**
     * 获取带层级的分类树
     * @return 分类树结构
     */
    List<Category> selectCategoryTree();

    /**
     * 获取分类商品数量统计
     * @return 包含分类ID、名称和商品数量的Map列表
     */
    List<Map<String, Object>> selectCategoryWithProductCount();

}




