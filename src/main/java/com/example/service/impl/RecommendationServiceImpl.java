package com.example.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.ProductSimilarityMapper;
import com.example.mapper.RecommendationResultMapper;
import com.example.mapper.UserSimilarityMapper;
import com.example.model.entity.*;
import com.example.service.ProductService;
import com.example.service.RecommendationService;
import com.example.service.UserBehaviorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 推荐服务实现类
 */
@Service
public class RecommendationServiceImpl extends ServiceImpl<RecommendationResultMapper, RecommendationResult> implements RecommendationService {

    @Autowired
    private ProductService productService;
    
    @Autowired
    private UserBehaviorService userBehaviorService;
    
    @Autowired
    private UserSimilarityMapper userSimilarityMapper;
    
    @Autowired
    private ProductSimilarityMapper productSimilarityMapper;

    @Override
    public List<Product> getRecommendedProducts(Long userId, Integer limit) {
        // 从推荐结果表获取推荐商品
        List<RecommendationResult> recommendations = baseMapper.findByUserId(userId, limit);
        
        if (recommendations.isEmpty()) {
            // 如果没有推荐结果，返回热门商品
            return productService.getHotProducts(limit);
        }
        
        // 提取商品ID并查询商品详情
        List<Long> productIds = recommendations.stream()
                .map(RecommendationResult::getProductId)
                .collect(Collectors.toList());
        
        // 查询商品详情并保持推荐顺序
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Product::getId, productIds)
                .eq(Product::getStatus, 1);
        List<Product> products = productService.list(queryWrapper);
        
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
    public List<Product> getUserBasedRecommendations(Long userId, Integer limit) {
        // 获取与用户相似的用户
        List<UserSimilarity> similarUsers = userSimilarityMapper.findSimilarUsers(userId, 10);
        
        if (similarUsers.isEmpty()) {
            // 如果没有相似用户，返回热门商品
            return productService.getHotProducts(limit);
        }
        
        // 获取相似用户的购买行为
        List<Long> similarUserIds = similarUsers.stream()
                .map(us -> us.getUserIdA().equals(userId) ? us.getUserIdB() : us.getUserIdA())
                .collect(Collectors.toList());
        
        List<UserBehavior> behaviors = new ArrayList<>();
        for (Long similarUserId : similarUserIds) {
            // 获取购买行为
            behaviors.addAll(userBehaviorService.getUserBehaviors(similarUserId, 4));
        }
        
        // 获取当前用户已购买的商品，过滤掉已购买的商品
        List<UserBehavior> userBehaviors = userBehaviorService.getUserBehaviors(userId, 4);
        List<Long> purchasedProductIds = userBehaviors.stream()
                .map(UserBehavior::getProductId)
                .collect(Collectors.toList());
        
        // 过滤出未购买的商品并按照出现频率排序
        List<Long> recommendProductIds = behaviors.stream()
                .map(UserBehavior::getProductId)
                .filter(pid -> !purchasedProductIds.contains(pid))
                .distinct()
                .limit(limit)
                .collect(Collectors.toList());
        
        if (recommendProductIds.isEmpty()) {
            // 如果没有推荐结果，返回热门商品
            return productService.getHotProducts(limit);
        }
        
        // 查询商品详情
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Product::getId, recommendProductIds)
                .eq(Product::getStatus, 1);
        return productService.list(queryWrapper);
    }

    @Override
    public List<Product> getItemBasedRecommendations(Long userId, Integer limit) {
        // 获取用户购买过的商品
        List<UserBehavior> purchases = userBehaviorService.getUserBehaviors(userId, 4);
        
        if (purchases.isEmpty()) {
            // 如果用户没有购买记录，返回热门商品
            return productService.getHotProducts(limit);
        }
        
        // 获取购买过的商品的相似商品
        List<Long> purchasedProductIds = purchases.stream()
                .map(UserBehavior::getProductId)
                .collect(Collectors.toList());
        
        List<Long> recommendProductIds = new ArrayList<>();
        for (Long productId : purchasedProductIds) {
            // 获取相似商品
            List<ProductSimilarity> similarProducts = productSimilarityMapper.findSimilarProducts(productId, 5);
            
            // 添加相似商品ID
            for (ProductSimilarity ps : similarProducts) {
                Long similarProductId = ps.getProductIdA().equals(productId) ? ps.getProductIdB() : ps.getProductIdA();
                if (!purchasedProductIds.contains(similarProductId) && !recommendProductIds.contains(similarProductId)) {
                    recommendProductIds.add(similarProductId);
                    
                    // 当推荐商品数量达到限制时结束
                    if (recommendProductIds.size() >= limit) {
                        break;
                    }
                }
            }
            
            // 当推荐商品数量达到限制时结束
            if (recommendProductIds.size() >= limit) {
                break;
            }
        }
        
        if (recommendProductIds.isEmpty()) {
            // 如果没有推荐结果，返回热门商品
            return productService.getHotProducts(limit);
        }
        
        // 查询商品详情
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Product::getId, recommendProductIds)
                .eq(Product::getStatus, 1);
        return productService.list(queryWrapper);
    }

    @Override
    public List<Product> getContentBasedRecommendations(Long userId, Integer limit) {
        // 内容推荐通常基于用户历史行为和商品特征
        // 这里简化实现，基于用户浏览和购买行为的分类偏好推荐商品
        
        // 获取用户浏览和购买行为
        List<UserBehavior> viewBehaviors = userBehaviorService.getUserBehaviors(userId, 1);
        List<UserBehavior> purchaseBehaviors = userBehaviorService.getUserBehaviors(userId, 4);
        
        if (viewBehaviors.isEmpty() && purchaseBehaviors.isEmpty()) {
            // 如果没有行为记录，返回热门商品
            return productService.getHotProducts(limit);
        }
        
        // 合并行为并获取商品ID
        List<Long> behaviorProductIds = new ArrayList<>();
        behaviorProductIds.addAll(viewBehaviors.stream().map(UserBehavior::getProductId).collect(Collectors.toList()));
        behaviorProductIds.addAll(purchaseBehaviors.stream().map(UserBehavior::getProductId).collect(Collectors.toList()));
        
        // 查询这些商品的分类
        LambdaQueryWrapper<Product> productQueryWrapper = new LambdaQueryWrapper<>();
        productQueryWrapper.in(Product::getId, behaviorProductIds);
        List<Product> behaviorProducts = productService.list(productQueryWrapper);
        
        // 获取用户可能感兴趣的分类ID
        List<Long> categoryIds = behaviorProducts.stream()
                .map(Product::getCategoryId)
                .distinct()
                .collect(Collectors.toList());
        
        if (categoryIds.isEmpty()) {
            // 如果没有分类信息，返回热门商品
            return productService.getHotProducts(limit);
        }
        
        // 查询这些分类下的商品，但排除用户已查看或购买的商品
        LambdaQueryWrapper<Product> recommendQueryWrapper = new LambdaQueryWrapper<>();
        recommendQueryWrapper.in(Product::getCategoryId, categoryIds)
                .eq(Product::getStatus, 1)
                .notIn(behaviorProductIds.size() > 0, Product::getId, behaviorProductIds)
                .orderByDesc(Product::getCreateTime)
                .last("LIMIT " + limit);
        
        List<Product> recommendProducts = productService.list(recommendQueryWrapper);
        
        if (recommendProducts.size() < limit) {
            // 如果推荐结果不足，补充热门商品
            int needMore = limit - recommendProducts.size();
            List<Product> hotProducts = productService.getHotProducts(needMore);
            
            // 过滤掉已推荐和已浏览/购买的商品
            List<Long> recommendedIds = recommendProducts.stream()
                    .map(Product::getId)
                    .collect(Collectors.toList());
            
            hotProducts = hotProducts.stream()
                    .filter(p -> !recommendedIds.contains(p.getId()) && !behaviorProductIds.contains(p.getId()))
                    .collect(Collectors.toList());
            
            recommendProducts.addAll(hotProducts);
        }
        
        return recommendProducts;
    }

    @Override
    public List<UserSimilarity> getSimilarUsers(Long userId, Integer limit) {
        return userSimilarityMapper.findSimilarUsers(userId, limit);
    }

    @Override
    public List<Product> getSimilarProducts(Long productId, Integer limit) {
        // 获取相似商品
        List<ProductSimilarity> similarities = productSimilarityMapper.findSimilarProducts(productId, limit);
        
        // 提取相似商品ID
        List<Long> similarProductIds = similarities.stream()
                .map(ps -> ps.getProductIdA().equals(productId) ? ps.getProductIdB() : ps.getProductIdA())
                .collect(Collectors.toList());
        
        if (similarProductIds.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 查询商品详情
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Product::getId, similarProductIds)
                .eq(Product::getStatus, 1);
        List<Product> products = productService.list(queryWrapper);
        
        // 按相似度排序
        List<Product> orderedProducts = new ArrayList<>(products.size());
        for (Long pid : similarProductIds) {
            products.stream()
                    .filter(p -> p.getId().equals(pid))
                    .findFirst()
                    .ifPresent(orderedProducts::add);
        }
        
        return orderedProducts;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RecommendationResult saveRecommendation(Long userId, Long productId, Integer algorithmType, Double score) {
        // 检查是否已存在推荐结果
        LambdaQueryWrapper<RecommendationResult> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RecommendationResult::getUserId, userId)
                .eq(RecommendationResult::getProductId, productId)
                .eq(RecommendationResult::getAlgorithmType, algorithmType);
        
        RecommendationResult existingResult = getOne(queryWrapper);
        
        if (existingResult != null) {
            // 更新已有推荐结果
            existingResult.setScore(BigDecimal.valueOf(score));
            existingResult.setCreateTime(LocalDateTime.now());
            // 默认推荐结果有效期为7天
            existingResult.setExpireTime(LocalDateTime.now().plusDays(7));
            updateById(existingResult);
            return existingResult;
        } else {
            // 创建新的推荐结果
            RecommendationResult newResult = new RecommendationResult();
            newResult.setUserId(userId);
            newResult.setProductId(productId);
            newResult.setAlgorithmType(algorithmType);
            newResult.setScore(BigDecimal.valueOf(score));
            newResult.setCreateTime(LocalDateTime.now());
            newResult.setExpireTime(LocalDateTime.now().plusDays(7));
            save(newResult);
            return newResult;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateUserSimilarities() {
        // 用户相似度计算是一个复杂的过程，需要基于用户行为数据计算用户间的相似度
        // 这里只提供一个简化的框架，实际实现需要更复杂的算法
        
        // 简化实现，基于用户的购买行为计算相似度
        // 实际应用中可能需要使用专业的机器学习框架
        
        return 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateProductSimilarities() {
        // 商品相似度计算也是一个复杂过程，需要基于商品特征和用户行为计算商品间的相似度
        // 这里只提供一个简化的框架，实际实现需要更复杂的算法
        
        // 简化实现，基于用户的共同购买行为计算相似度
        // 实际应用中可能需要使用专业的机器学习框架
        
        return 0;
    }
}