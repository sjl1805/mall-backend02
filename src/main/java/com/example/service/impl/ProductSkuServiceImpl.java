package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.exception.BusinessException;
import com.example.mapper.ProductSkuMapper;
import com.example.model.entity.ProductSku;
import com.example.service.ProductSkuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品SKU服务实现类
 */
@Service
public class ProductSkuServiceImpl extends ServiceImpl<ProductSkuMapper, ProductSku> implements ProductSkuService {

    @Override
    public List<ProductSku> getSkusByProductId(Long productId) {
        return baseMapper.findByProductId(productId);
    }

    @Override
    public ProductSku getSkuByCode(String skuCode) {
        return baseMapper.findBySkuCode(skuCode);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductSku addSku(ProductSku sku) {
        // 设置默认值
        sku.setStatus(sku.getStatus() == null ? 1 : sku.getStatus());
        sku.setCreateTime(LocalDateTime.now());
        sku.setUpdateTime(LocalDateTime.now());
        
        // 保存SKU
        save(sku);
        
        return sku;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchAddSkus(List<ProductSku> skus) {
        if (skus == null || skus.isEmpty()) {
            return false;
        }
        
        LocalDateTime now = LocalDateTime.now();
        skus.forEach(sku -> {
            sku.setStatus(sku.getStatus() == null ? 1 : sku.getStatus());
            sku.setCreateTime(now);
            sku.setUpdateTime(now);
        });
        
        return saveBatch(skus);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSku(ProductSku sku) {
        if (sku == null || sku.getId() == null) {
            throw new BusinessException("SKU ID不能为空");
        }
        
        // 不允许修改的字段设为null
        sku.setProductId(null);
        sku.setCreateTime(null);
        
        sku.setUpdateTime(LocalDateTime.now());
        
        return updateById(sku);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSkuStock(Long skuId, Integer stock) {
        if (stock < 0) {
            throw new BusinessException("库存不能为负数");
        }
        
        LambdaUpdateWrapper<ProductSku> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ProductSku::getId, skuId)
                .set(ProductSku::getStock, stock)
                .set(ProductSku::getUpdateTime, LocalDateTime.now());
        
        return update(updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean decreaseStock(Long skuId, Integer quantity) {
        if (quantity <= 0) {
            throw new BusinessException("减少数量必须大于0");
        }
        
        int rows = baseMapper.decreaseStock(skuId, quantity);
        return rows > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean increaseStock(Long skuId, Integer quantity) {
        if (quantity <= 0) {
            throw new BusinessException("增加数量必须大于0");
        }
        
        LambdaUpdateWrapper<ProductSku> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ProductSku::getId, skuId)
                .setSql("stock = stock + " + quantity)
                .set(ProductSku::getUpdateTime, LocalDateTime.now());
        
        return update(updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteSku(Long skuId) {
        // 后续可以添加SKU使用检查逻辑，防止删除已使用的SKU
        return removeById(skuId);
    }
} 