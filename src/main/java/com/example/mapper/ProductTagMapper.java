package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.entity.ProductTag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 商品标签数据访问层接口
 */
@Mapper
public interface ProductTagMapper extends BaseMapper<ProductTag> {
    
    /**
     * 根据标签类型查询标签列表
     *
     * @param type 标签类型
     * @return 标签列表
     */
    @Select("SELECT * FROM product_tag WHERE type = #{type} ORDER BY id ASC")
    List<ProductTag> findByType(@Param("type") Integer type);
} 