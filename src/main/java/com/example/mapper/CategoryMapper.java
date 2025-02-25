package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 商品分类数据访问层接口
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
    
    /**
     * 获取指定父分类下的所有子分类
     *
     * @param parentId 父分类ID
     * @return 子分类列表
     */
    @Select("SELECT * FROM category WHERE parent_id = #{parentId} AND status = 1 ORDER BY id ASC")
    List<Category> findByParentId(@Param("parentId") Long parentId);
    
    /**
     * 获取指定层级的分类列表
     *
     * @param level 层级
     * @return 分类列表
     */
    @Select("SELECT * FROM category WHERE level = #{level} AND status = 1 ORDER BY id ASC")
    List<Category> findByLevel(@Param("level") Integer level);
} 