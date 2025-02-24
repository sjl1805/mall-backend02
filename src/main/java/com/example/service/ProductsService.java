package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.Products;

import java.util.List;

/**
 * @author 31815
 * @description 针对表【products(商品表)】的数据库操作Service
 * @createDate 2025-02-24 12:03:56
 */
public interface ProductsService extends IService<Products> {

    /**
     * 根据商品名称查询商品
     *
     * @param name 商品名称
     * @return 商品列表
     */
    List<Products> selectByName(String name);

    /**
     * 分页查询商品
     *
     * @param page 分页信息
     * @return 商品列表
     */
    IPage<Products> selectPage(IPage<Products> page);

    /**
     * 根据ID查询商品
     *
     * @param id 商品ID
     * @return 商品信息
     */
    Products selectById(Long id);

    /**
     * 新增商品
     *
     * @param product 商品信息
     * @return 插入结果
     */
    boolean insertProduct(Products product);

    /**
     * 更新商品信息
     *
     * @param product 商品信息
     * @return 更新结果
     */
    boolean updateProduct(Products product);

    /**
     * 根据ID删除商品
     *
     * @param id 商品ID
     * @return 删除结果
     */
    boolean deleteProduct(Long id);
}
