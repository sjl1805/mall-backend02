package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.ProductSku;

import java.util.List;

/**
 * @author 31815
 * @description 针对表【product_sku(商品SKU表)】的数据库操作Service
 * @createDate 2025-02-24 12:04:01
 */
public interface ProductSkuService extends IService<ProductSku> {

    /**
     * 根据商品ID查询SKU
     *
     * @param productId 商品ID
     * @return SKU列表
     */
    List<ProductSku> selectByProductId(Long productId);

    /**
     * 分页查询SKU
     *
     * @param page 分页信息
     * @return SKU列表
     */
    IPage<ProductSku> selectPage(IPage<ProductSku> page);

    /**
     * 根据ID查询SKU
     *
     * @param id SKU ID
     * @return SKU信息
     */
    ProductSku selectById(Long id);

    /**
     * 新增SKU
     *
     * @param productSku SKU信息
     * @return 插入结果
     */
    boolean insertProductSku(ProductSku productSku);

    /**
     * 更新SKU信息
     *
     * @param productSku SKU信息
     * @return 更新结果
     */
    boolean updateProductSku(ProductSku productSku);

    /**
     * 根据ID删除SKU
     *
     * @param id SKU ID
     * @return 删除结果
     */
    boolean deleteProductSku(Long id);
}
