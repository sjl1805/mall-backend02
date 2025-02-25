package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.exception.BusinessException;
import com.example.mapper.ProductMapper;
import com.example.mapper.RecommendationResultMapper;
import com.example.model.entity.Product;
import com.example.model.entity.RecommendationResult;
import com.example.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品服务实现类
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    private RecommendationResultMapper recommendationResultMapper;

    @Override
    public IPage<Product> pageProductsByCategory(Page<Product> page, Long categoryId) {
        return baseMapper.findByCategoryId(page, categoryId);
    }

    @Override
    public IPage<Product> searchProducts(Page<Product> page, String keyword) {
        if (!StringUtils.hasText(keyword)) {
            LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Product::getStatus, 1)
                    .orderByDesc(Product::getCreateTime);
            return page(page, queryWrapper);
        }
        return baseMapper.searchProducts(page, keyword);
    }

    @Override
    public List<Product> getHotProducts(Integer limit) {
        return baseMapper.findHotProducts(limit);
    }

    @Override
    public List<Product> getNewProducts(Integer limit) {
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Product::getStatus, 1)
                .orderByDesc(Product::getCreateTime)
                .last("LIMIT " + limit);
        return list(queryWrapper);
    }

    @Override
    public List<Product> getRecommendProducts(Long userId, Integer limit) {
        // 获取用户的推荐商品
        List<RecommendationResult> recommendations = recommendationResultMapper.findByUserId(userId, limit);
        
        if (recommendations.isEmpty()) {
            // 如果没有推荐，返回热门商品
            return getHotProducts(limit);
        }
        
        // 提取推荐商品ID
        List<Long> productIds = recommendations.stream()
                .map(RecommendationResult::getProductId)
                .collect(Collectors.toList());
        
        // 查询商品详情
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Product::getId, productIds)
                .eq(Product::getStatus, 1);
        List<Product> products = list(queryWrapper);
        
        // 按照推荐顺序排序
        List<Product> orderedProducts = new ArrayList<>(products.size());
        for (Long productId : productIds) {
            products.stream()
                    .filter(p -> p.getId().equals(productId))
                    .findFirst()
                    .ifPresent(orderedProducts::add);
        }
        
        return orderedProducts;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Product addProduct(Product product) {
        // 设置默认值
        product.setStatus(product.getStatus() == null ? 1 : product.getStatus());
        product.setCreateTime(LocalDateTime.now());
        product.setUpdateTime(LocalDateTime.now());
        
        // 保存商品
        save(product);
        
        return product;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateProduct(Product product) {
        if (product == null || product.getId() == null) {
            throw new BusinessException("商品ID不能为空");
        }
        
        // 不允许修改的字段设为null
        product.setCreateTime(null);
        
        product.setUpdateTime(LocalDateTime.now());
        
        return updateById(product);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateProductStatus(Long productId, Integer status) {
        if (status != 0 && status != 1) {
            throw new BusinessException("状态参数错误");
        }
        
        LambdaUpdateWrapper<Product> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Product::getId, productId)
                .set(Product::getStatus, status)
                .set(Product::getUpdateTime, LocalDateTime.now());
        
        return update(updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteProduct(Long productId) {
        // 后续可以添加商品关联检查逻辑，防止删除已关联的商品
        return removeById(productId);
    }
} 