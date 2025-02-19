package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.ProductSku;

import java.util.List;
import java.util.Map;

/**
 * 商品SKU服务接口
 * 
 * @author 31815
 * @description 提供商品SKU管理功能，包含：
 *              1. SKU的批量操作
 *              2. 库存与销量管理
 *              3. 状态统计与图片更新
 * @createDate 2025-02-18 23:44:08
 */
public interface ProductSkuService extends IService<ProductSku> {

    /**
     * 批量创建SKU（事务操作）
     * @param productId 商品ID
     * @param skus SKU列表（至少一项）
     * @return 操作是否成功
     * @throws com.example.exception.BusinessException 当商品状态不可编辑时抛出
     */
    boolean batchCreateSkus(Long productId, List<ProductSku> skus);

    /**
     * 获取商品SKU列表（带缓存）
     * @param productId 商品ID
     * @return SKU列表（按创建时间排序）
     * @implNote 结果缓存优化，有效期30分钟
     */
    List<ProductSku> getSkusByProductId(Long productId);

    /**
     * 调整库存（原子操作）
     * @param productId 商品ID
     * @param skuId SKU ID
     * @param quantity 调整数量（正数增加，负数减少）
     * @return 操作是否成功
     * @throws com.example.exception.BusinessException 当库存不足时抛出
     */
    boolean adjustStock(Long productId, Long skuId, Integer quantity);

    /**
     * 增加销量（原子操作）
     * @param productId 商品ID
     * @param skuId SKU ID
     * @param quantity 增加数量（必须为正数）
     * @return 操作是否成功
     */
    boolean increaseSales(Long productId, Long skuId, Integer quantity);

    /**
     * 获取SKU状态统计
     * @param productId 商品ID
     * @return 状态统计结果（key为状态值，value为对应数量）
     * @implNote 结果缓存优化，有效期1小时
     */
    Map<Integer, Long> getSkuStatusStats(Long productId);

    /**
     * 批量更新主图（事务操作）
     * @param productId 商品ID
     * @param oldImage 原图片地址
     * @param newImage 新图片地址
     * @return 更新影响的行数
     */
    boolean updateMainImage(Long productId, String oldImage, String newImage);
}
