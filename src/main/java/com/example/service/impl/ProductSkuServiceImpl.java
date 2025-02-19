package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.service.ProductSkuService;
import com.example.model.entity.ProductSku;
import com.example.mapper.ProductSkuMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @author 31815
* @description 针对表【product_sku(商品SKU表)】的数据库操作Service实现
* @createDate 2025-02-18 23:44:08
*/
@Service
@CacheConfig(cacheNames = "productSkuService")
public class ProductSkuServiceImpl extends ServiceImpl<ProductSkuMapper, ProductSku>
    implements ProductSkuService {

    //private static final Logger logger = LoggerFactory.getLogger(ProductSkuServiceImpl.class);

    @Override
    @Transactional
    @CacheEvict(key = "'product:' + #productId")
    public boolean batchCreateSkus(Long productId, List<ProductSku> skus) {
        // 校验商品状态
        validateProductStatus(productId);
        
        // 设置商品ID
        skus.forEach(sku -> sku.setProductId(productId));
        
        return baseMapper.batchInsert(skus) > 0;
    }

    @Override
    @Cacheable(key = "'product:' + #productId")
    public List<ProductSku> getSkusByProductId(Long productId) {
        return baseMapper.selectByProductId(productId);
    }

    @Override
    @Transactional
    @CacheEvict(key = "'product:' + #productId")
    public boolean adjustStock(Long productId, Long skuId, Integer quantity) {
        return baseMapper.updateStockSafely(productId, skuId, quantity) > 0;
    }

    @Override
    @Transactional
    @CacheEvict(key = "'product:' + #productId")
    public boolean increaseSales(Long productId, Long skuId, Integer quantity) {
        return baseMapper.increaseSales(skuId, quantity) > 0;
    }

    @Override
    @Cacheable(key = "'stats:' + #productId")
    public Map<Integer, Long> getSkuStatusStats(Long productId) {
        return baseMapper.countSkuStatus(productId).stream()
                .collect(Collectors.toMap(
                        m -> (Integer) m.get("status"),
                        m -> (Long) m.get("count")
                ));
    }

    @Override
    @Transactional
    @CacheEvict(key = "'product:' + #productId")
    public boolean updateMainImage(Long productId, String oldImage, String newImage) {
        return baseMapper.batchUpdateMainImage(productId, oldImage, newImage) > 0;
    }

    private void validateProductStatus(Long productId) {
        // 需要依赖ProductsService校验商品状态
        // 实际开发中需要注入ProductsService
        // if (!productsService.isProductActive(productId)) {
        //     throw new BusinessException(ResultCode.PRODUCT_STATUS_ERROR);
        // }
    }
}




