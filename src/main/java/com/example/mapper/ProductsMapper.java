package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.model.entity.Products;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 31815
 * @description 针对表【products(商品表)】的数据库操作Mapper
 * @createDate 2025-02-24 12:03:56
 * @Entity model.entity.Products
 */
public interface ProductsMapper extends BaseMapper<Products> {

    /**
     * 根据商品名称模糊查询商品
     * @param name 商品名称
     * @return 商品列表
     */
    List<Products> selectByNameLike(@Param("name") String name);

    /**
     * 分页查询商品
     * @param page 分页信息
     * @return 商品列表
     */
    IPage<Products> selectPage(IPage<Products> page);

    /**
     * 根据ID查询商品
     * @param id 商品ID
     * @return 商品信息
     */
    Products selectById(@Param("id") Long id);

    /**
     * 插入新商品
     * @param product 商品信息
     * @return 插入结果
     */
    int insertProduct(Products product);

    /**
     * 更新商品信息
     * @param product 商品信息
     * @return 更新结果
     */
    int updateProduct(Products product);

    /**
     * 根据ID删除商品
     * @param id 商品ID
     * @return 删除结果
     */
    int deleteProduct(@Param("id") Long id);
}




