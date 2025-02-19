package com.example.service;

import com.example.model.entity.ProductSku;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
* @author 31815
* @description 针对表【product_sku(商品SKU表)】的数据库操作Service
* @createDate 2025-02-18 23:44:08
*/
public interface ProductSkuService extends IService<ProductSku> {
    boolean batchCreateSkus(Long productId, List<ProductSku> skus);
    List<ProductSku> getSkusByProductId(Long productId);
    boolean adjustStock(Long productId, Long skuId, Integer quantity);
    boolean increaseSales(Long productId, Long skuId, Integer quantity);
    Map<Integer, Long> getSkuStatusStats(Long productId);
    boolean updateMainImage(Long productId, String oldImage, String newImage);
}
