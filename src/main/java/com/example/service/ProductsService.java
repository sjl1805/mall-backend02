package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.dto.ProductsDTO;
import com.example.model.dto.PageDTO;
import com.example.model.entity.Products;

import java.util.List;

/**
 * 商品服务接口
 * 
 * @author 31815
 * @description 提供商品全生命周期管理功能，包含：
 *              1. 商品信息管理
 *              2. 库存与状态管理
 *              3. 新品推荐与分页查询
 * @createDate 2025-02-18 23:44:03
 */
public interface ProductsService extends IService<Products> {

    /**
     * 分页查询商品（带缓存）
     * @param queryDTO 分页参数：
     *                 - keyword: 商品名称搜索
     *                 - categoryId: 分类ID过滤
     *                 - status: 状态过滤
     * @return 分页结果（包含商品基本信息）
     * @implNote 结果缓存优化，有效期15分钟
     */
    IPage<ProductsDTO> listProductsPage(PageDTO<ProductsDTO> queryDTO);

    /**
     * 添加商品（事务操作）
     * @param productsDTO 商品信息，包含：
     *                    - name: 商品名称（必须）
     *                    - categoryId: 分类ID（必须）
     *                    - price: 价格（必须）
     * @return 操作是否成功
     * @throws com.example.exception.BusinessException 当分类无效或名称重复时抛出
     */
    boolean addProduct(ProductsDTO productsDTO);

    /**
     * 更新商品信息（带校验）
     * @param productsDTO 商品信息（必须包含ID）
     * @return 操作是否成功
     * @throws com.example.exception.BusinessException 当商品不存在或名称重复时抛出
     */
    boolean updateProduct(ProductsDTO productsDTO);

    /**
     * 调整库存（原子操作）
     * @param productId 商品ID
     * @param delta 调整数量（正数增加，负数减少）
     * @return 操作是否成功
     * @throws com.example.exception.BusinessException 当库存不足时抛出
     */
    boolean adjustStock(Long productId, Integer delta);

    /**
     * 切换商品状态（上架/下架）
     * @param productId 商品ID
     * @param status 新状态（1-上架，0-下架）
     * @return 操作是否成功
     * @throws com.example.exception.BusinessException 当商品不存在时抛出
     */
    boolean switchStatus(Long productId, Integer status);

    /**
     * 获取新品推荐（带缓存）
     * @param categoryId 分类ID（0表示全站）
     * @param limit 最大数量
     * @return 新品列表（按创建时间倒序）
     * @implNote 结果缓存优化，有效期1小时
     */
    List<Products> getNewArrivals(Long categoryId, Integer limit);
}
