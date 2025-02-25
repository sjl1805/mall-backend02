package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.model.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 商品数据访问层接口
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {
    
    /**
     * 根据分类ID查询商品
     *
     * @param categoryId 分类ID
     * @param page 分页参数
     * @return 分页商品列表
     */
    @Select("SELECT * FROM products WHERE category_id = #{categoryId} AND status = 1 ORDER BY id DESC")
    IPage<Product> findByCategoryId(Page<Product> page, @Param("categoryId") Long categoryId);
    
    /**
     * 查询热门商品列表
     *
     * @param limit 限制数量
     * @return 热门商品列表
     */
    @Select("SELECT p.* FROM products p " +
            "LEFT JOIN user_behavior ub ON p.id = ub.product_id " +
            "WHERE p.status = 1 " +
            "GROUP BY p.id " +
            "ORDER BY COUNT(ub.id) DESC " +
            "LIMIT #{limit}")
    List<Product> findHotProducts(@Param("limit") Integer limit);
    
    /**
     * 模糊搜索商品
     *
     * @param keyword 关键词
     * @param page 分页参数
     * @return 分页商品列表
     */
    @Select("SELECT * FROM products WHERE status = 1 AND (name LIKE CONCAT('%', #{keyword}, '%') " +
            "OR description LIKE CONCAT('%', #{keyword}, '%')) " +
            "ORDER BY id DESC")
    IPage<Product> searchProducts(Page<Product> page, @Param("keyword") String keyword);
} 