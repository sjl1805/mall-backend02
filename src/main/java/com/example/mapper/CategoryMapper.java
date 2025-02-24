package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.model.entity.Category;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * @author 31815
 * @description 针对表【category(商品分类表)】的数据库操作Mapper
 * @createDate 2025-02-24 12:04:22
 * @Entity model.entity.Category
 */
public interface CategoryMapper extends BaseMapper<Category> {

    /**
     * 根据分类名称模糊查询
     * @param name 分类名称（模糊匹配）
     * @return 分类列表
     */
    List<Category> selectByNameLike(@Param("name") String name);

    /**
     * 分页查询分类（支持排序）
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<Category> selectPage(IPage<Category> page);

    /**
     * 根据父分类ID查询子分类
     * @param parentId 父分类ID
     * @return 子分类列表
     */
    List<Category> selectByParentId(@Param("parentId") Long parentId);

    /**
     * 根据层级查询分类
     * @param level 分类层级（1-一级 2-二级 3-三级）
     * @return 分类列表
     */
    List<Category> selectByLevel(@Param("level") Integer level);
}




