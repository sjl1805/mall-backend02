package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.ProductSku;

import java.util.List;

/**
 * 商品SKU服务接口
 */
public interface ProductSkuService extends IService<ProductSku> {
    
    /**
     * 根据商品ID获取SKU列表
     *
     * @param productId 商品ID
     * @return SKU列表
     */
    List<ProductSku> getSkusByProductId(Long productId);
    
    /**
     * 根据SKU编码获取SKU
     *
     * @param skuCode SKU编码
     * @return SKU对象
     */
    ProductSku getSkuByCode(String skuCode);
    
    /**
     * 添加SKU
     *
     * @param sku SKU信息
     * @return 添加成功的SKU
     */
    ProductSku addSku(ProductSku sku);
    
    /**
     * 批量添加SKU
     *
     * @param skus SKU列表
     * @return 是否添加成功
     */
    boolean batchAddSkus(List<ProductSku> skus);
    
    /**
     * 更新SKU
     *
     * @param sku SKU信息
     * @return 是否更新成功
     */
    boolean updateSku(ProductSku sku);
    
    /**
     * 更新SKU库存
     *
     * @param skuId SKU ID
     * @param stock 库存数量
     * @return 是否更新成功
     */
    boolean updateSkuStock(Long skuId, Integer stock);
    
    /**
     * 减少SKU库存
     *
     * @param skuId SKU ID
     * @param quantity 减少数量
     * @return 是否更新成功
     */
    boolean decreaseStock(Long skuId, Integer quantity);
    
    /**
     * 增加SKU库存
     *
     * @param skuId SKU ID
     * @param quantity 增加数量
     * @return 是否更新成功
     */
    boolean increaseStock(Long skuId, Integer quantity);
    
    /**
     * 删除SKU
     *
     * @param skuId SKU ID
     * @return 是否删除成功
     */
    boolean deleteSku(Long skuId);
} 