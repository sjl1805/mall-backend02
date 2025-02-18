package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.model.dto.ProductQuery;
import com.example.model.entity.Products;

import org.apache.ibatis.annotations.Param;

/**
 * 商品管理Mapper接口
 * @author 毕业设计学生
 */
public interface ProductsMapper extends BaseMapper<Products> {

    /**
     * 分页查询商品列表（带条件）
     * @param page 分页参数
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<Products> selectProductPage(IPage<Products> page, @Param("query") ProductQuery query);

    /**
     * 更新商品状态（上架/下架）
     * @param productId 商品ID
     * @param status 新状态
     * @return 影响行数
     */
    int updateStatus(@Param("productId") Long productId, @Param("status") Integer status);

    /**
     * 调整商品库存
     * @param productId 商品ID
     * @param delta 库存变化量（正数增加，负数减少）
     * @return 影响行数
     */
    int adjustStock(@Param("productId") Long productId, @Param("delta") Integer delta);

    /**
     * 检查商品名称唯一性
     * @param name 商品名称
     * @param excludeId 排除的ID
     * @return 存在的记录数
     */
    int checkNameUnique(@Param("name") String name, 
                       @Param("excludeId") Long excludeId);
}




