package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.model.entity.Products;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @author 31815
 * @description 针对表【products(商品表)】的数据库操作Mapper
 * @createDate 2025-02-24 12:03:56
 * @Entity model.entity.Products
 */
public interface ProductsMapper extends BaseMapper<Products> {

    /**
     * 更新商品状态
     * @param productId 商品ID
     * @param status 新状态（0-下架，1-上架）
     * @return 影响行数
     */
    int updateProductStatus(@Param("productId") Long productId, @Param("status") Integer status);

    /**
     * 分页查询商品（带分类过滤）
     * @param page 分页参数
     * @param query 查询条件
     * @return 分页结果
     */
    Page<Products> selectProductsPage(Page<Products> page, @Param("query") ProductQuery query);

    /**
     * 更新商品图片
     * @param productId 商品ID
     * @param images JSON格式图片数据
     * @return 影响行数
     */
    int updateProductImages(@Param("productId") Long productId, @Param("images") String images);

    /**
     * 获取价格区间统计
     * @param categoryId 可选分类ID
     * @return 包含minPrice/maxPrice/avgPrice的Map
     */
    Map<String, Object> selectPriceRange(@Param("categoryId") Long categoryId);

    // 查询条件封装类
    class ProductQuery {
        private Long categoryId;
        private Integer status;
        private String keyword;
    }
}




