package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.RecommendationResultMapper;
import com.example.model.entity.RecommendationResult;
import com.example.model.vo.ProductRecommendVO;
import com.example.model.vo.RecommendUserVO;
import com.example.service.RecommendationResultService;
import com.example.service.UserBehaviorService;
import com.example.service.UserSimilarityService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;


/**
 * 推荐结果服务实现类
 */
@Slf4j
@Service
public class RecommendationResultServiceImpl extends ServiceImpl<RecommendationResultMapper, RecommendationResult> implements RecommendationResultService {

    @Autowired
    private RecommendationResultMapper recommendationResultMapper;
    
    @Autowired
    private UserBehaviorService userBehaviorService;
    
    @Autowired
    private UserSimilarityService userSimilarityService;

    @Override
    public List<RecommendationResult> getUserRecommendations(Long userId, Integer limit) {
        return recommendationResultMapper.selectByUserId(userId, limit);
    }

    @Override
    public List<RecommendationResult> getRecommendationsByAlgorithmType(Long userId, Integer algorithmType, Integer limit) {
        return recommendationResultMapper.selectByUserIdAndAlgorithmType(userId, algorithmType, limit);
    }

    @Override
    public List<RecommendationResult> getValidRecommendations(Long userId, Integer limit) {
        return recommendationResultMapper.selectValidRecommendations(userId, LocalDateTime.now(), limit);
    }

    @Override
    public RecommendationResult checkProductRecommended(Long userId, Long productId) {
        return recommendationResultMapper.selectByUserIdAndProductId(userId, productId);
    }

    @Override
    public boolean saveOrUpdateRecommendation(Long userId, Long productId, BigDecimal score, Integer algorithmType, LocalDateTime expireTime) {
        int result = recommendationResultMapper.insertOrUpdate(userId, productId, score, algorithmType, expireTime);
        return result > 0;
    }

    @Override
    @Transactional
    public boolean batchSaveRecommendations(List<RecommendationResult> recommendationList) {
        if (recommendationList == null || recommendationList.isEmpty()) {
            return false;
        }
        
        // 设置创建时间
        for (RecommendationResult recommendation : recommendationList) {
            if (recommendation.getCreateTime() == null) {
                recommendation.setCreateTime(LocalDateTime.now());
            }
        }
        
        int result = recommendationResultMapper.batchInsert(recommendationList);
        return result > 0;
    }

    @Override
    public int cleanExpiredRecommendations() {
        return recommendationResultMapper.deleteExpiredRecommendations(LocalDateTime.now());
    }

    @Override
    public boolean deleteUserRecommendations(Long userId) {
        int result = recommendationResultMapper.deleteByUserId(userId);
        return result > 0;
    }

    @Override
    public boolean deleteRecommendationsByAlgorithmType(Integer algorithmType) {
        int result = recommendationResultMapper.deleteByAlgorithmType(algorithmType);
        return result > 0;
    }

    @Override
    public List<RecommendationResult> getHighScoreRecommendations(Long userId, BigDecimal minScore, Integer limit) {
        return recommendationResultMapper.selectHighScoreRecommendations(userId, minScore, limit);
    }

    @Override
    public List<Map<String, Object>> getSimilarProductRecommendations(List<Long> productIds, Integer limit) {
        if (productIds == null || productIds.isEmpty()) {
            return new ArrayList<>();
        }
        
        Map<Long, Map<String, Object>> resultMap = recommendationResultMapper.selectSimilarProductRecommendations(productIds, limit);
        if (resultMap == null || resultMap.isEmpty()) {
            return new ArrayList<>();
        }
        
        return new ArrayList<>(resultMap.values());
    }

    @Override
    public IPage<RecommendationResult> getUserRecommendationsPage(Page<RecommendationResult> page, Long userId) {
        return recommendationResultMapper.selectPageByUserId(page, userId);
    }

    @Override
    public List<Map<String, Object>> getAlgorithmTypeStats() {
        return recommendationResultMapper.selectAlgorithmTypeStats();
    }

    @Override
    @Transactional
    public boolean generatePersonalizedRecommendations(Long userId, Integer limit) {
        try {
            // 1. 清除该用户之前的个性化推荐结果
            LambdaQueryWrapper<RecommendationResult> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(RecommendationResult::getUserId, userId)
                    .in(RecommendationResult::getAlgorithmType, Arrays.asList(1, 2, 3)); // 个性化推荐算法类型
            remove(queryWrapper);
            
            // 2. 生成基于用户的协同过滤推荐
            boolean userBasedResult = generateUserBasedRecommendations(userId, limit);
            
            // 3. 生成基于商品的协同过滤推荐
            boolean itemBasedResult = generateItemBasedRecommendations(userId, limit);
            
            // 4. 如果推荐数量不足，补充热门商品推荐
            List<RecommendationResult> currentRecommendations = getUserRecommendations(userId, limit);
            if (currentRecommendations.size() < limit) {
                //int needMore = limit - currentRecommendations.size();
                // 这里应该调用热门商品推荐的逻辑，为简化实现，暂不实现
            }
            
            return userBasedResult || itemBasedResult;
        } catch (Exception e) {
            log.error("为用户{}生成个性化推荐失败", userId, e);
            return false;
        }
    }

    @Override
    @Transactional
    public boolean generateUserBasedRecommendations(Long userId, Integer limit) {
        try {
            // 1. 获取与当前用户相似度最高的用户列表
            List<RecommendUserVO> similarUsers = userSimilarityService.getRecommendedUsers(userId, 10);
            if (similarUsers.isEmpty()) {
                return false;
            }
            
            // 2. 获取相似用户的行为数据
            Set<Long> recommendProductIds = new HashSet<>();
            List<RecommendationResult> recommendations = new ArrayList<>();
            
            // 设置推荐过期时间，默认7天后过期
            LocalDateTime expireTime = LocalDateTime.now().plusDays(7);
            
            // 3. 为每个相似用户，获取其喜欢的商品
            for (RecommendUserVO similarUser : similarUsers) {
                // 获取该用户最近浏览、收藏、购买的商品
                List<Long> viewedProducts = userBehaviorService.getRecentViewedProducts(similarUser.getUserId(), 20);
                List<Long> favoriteProducts = userBehaviorService.getFavoriteProducts(similarUser.getUserId(), 10);
                
                // 合并商品ID列表，并排除当前用户已经交互过的商品
                Set<Long> candidateProducts = new HashSet<>();
                candidateProducts.addAll(viewedProducts);
                candidateProducts.addAll(favoriteProducts);
                
                // 获取当前用户已交互的商品，用于排除
                List<Long> userInteractedProducts = userBehaviorService.getRecentViewedProducts(userId, 100);
                candidateProducts.removeAll(userInteractedProducts);
                
                // 4. 为每个候选商品计算推荐分数并创建推荐结果
                for (Long productId : candidateProducts) {
                    // 避免重复推荐
                    if (recommendProductIds.contains(productId)) {
                        continue;
                    }
                    
                    // 计算推荐分数：相似度 * 权重因子
                    BigDecimal score = similarUser.getSimilarity().multiply(new BigDecimal("0.8"));
                    
                    // 创建推荐结果对象
                    RecommendationResult recommendation = RecommendationResult.builder()
                            .userId(userId)
                            .productId(productId)
                            .score(score)
                            .algorithmType(1) // 基于用户的协同过滤
                            .expireTime(expireTime)
                            .createTime(LocalDateTime.now())
                            .build();
                    
                    recommendations.add(recommendation);
                    recommendProductIds.add(productId);
                    
                    // 达到推荐数量限制时停止
                    if (recommendations.size() >= limit) {
                        break;
                    }
                }
                
                // 达到推荐数量限制时停止
                if (recommendations.size() >= limit) {
                    break;
                }
            }
            
            // 5. 批量保存推荐结果
            if (!recommendations.isEmpty()) {
                return batchSaveRecommendations(recommendations);
            }
            
            return false;
        } catch (Exception e) {
            log.error("为用户{}生成基于用户的推荐失败", userId, e);
            return false;
        }
    }

    @Override
    @Transactional
    public boolean generateItemBasedRecommendations(Long userId, Integer limit) {
        try {
            // 1. 获取用户最近交互的商品
            List<Long> recentProducts = userBehaviorService.getRecentViewedProducts(userId, 10);
            if (recentProducts.isEmpty()) {
                return false;
            }
            
            // 2. 获取相似商品推荐
            List<Map<String, Object>> similarProducts = getSimilarProductRecommendations(recentProducts, limit * 2);
            if (similarProducts.isEmpty()) {
                return false;
            }
            
            // 3. 获取用户已交互的商品，用于排除
            List<Long> userInteractedProducts = userBehaviorService.getRecentViewedProducts(userId, 100);
            Set<Long> interactedProductSet = new HashSet<>(userInteractedProducts);
            
            // 4. 创建推荐结果列表
            List<RecommendationResult> recommendations = new ArrayList<>();
            Set<Long> recommendProductIds = new HashSet<>();
            
            // 设置推荐过期时间，默认7天后过期
            LocalDateTime expireTime = LocalDateTime.now().plusDays(7);
            
            // 5. 处理相似商品数据
            for (Map<String, Object> product : similarProducts) {
                Long productId = Long.valueOf(product.get("product_id").toString());
                
                // 排除用户已交互的商品和已添加到推荐列表的商品
                if (interactedProductSet.contains(productId) || recommendProductIds.contains(productId)) {
                    continue;
                }
                
                // 获取推荐分数
                BigDecimal score;
                if (product.containsKey("avg_score")) {
                    Object scoreObj = product.get("avg_score");
                    if (scoreObj instanceof BigDecimal) {
                        score = (BigDecimal) scoreObj;
                    } else {
                        score = new BigDecimal(scoreObj.toString());
                    }
                } else {
                    // 默认分数
                    score = new BigDecimal("0.5");
                }
                
                // 创建推荐结果对象
                RecommendationResult recommendation = RecommendationResult.builder()
                        .userId(userId)
                        .productId(productId)
                        .score(score)
                        .algorithmType(2) // 基于商品的协同过滤
                        .expireTime(expireTime)
                        .createTime(LocalDateTime.now())
                        .build();
                
                recommendations.add(recommendation);
                recommendProductIds.add(productId);
                
                // 达到推荐数量限制时停止
                if (recommendations.size() >= limit) {
                    break;
                }
            }
            
            // 6. 批量保存推荐结果
            if (!recommendations.isEmpty()) {
                return batchSaveRecommendations(recommendations);
            }
            
            return false;
        } catch (Exception e) {
            log.error("为用户{}生成基于商品的推荐失败", userId, e);
            return false;
        }
    }

    @Override
    @Transactional
    public boolean generatePopularRecommendations(Integer limit) {
        try {
            // 这里应该调用商品服务获取热门商品
            // 为简化实现，暂时返回false
            return false;
        } catch (Exception e) {
            log.error("生成热门商品推荐失败", e);
            return false;
        }
    }

    @Override
    @Transactional
    public boolean generateNewProductRecommendations(Integer days, Integer limit) {
        try {
            // 这里应该调用商品服务获取新品
            // 为简化实现，暂时返回false
            return false;
        } catch (Exception e) {
            log.error("生成新品推荐失败", e);
            return false;
        }
    }

    @Override
    public List<ProductRecommendVO> getUserRecommendProductDetails(Long userId, Integer limit) {
        // 1. 获取用户的有效推荐结果
        List<RecommendationResult> recommendations = getValidRecommendations(userId, limit);
        if (recommendations.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 2. 获取推荐商品的详细信息
        // 实际项目中应该调用商品服务获取商品详情
        // 这里为了简化，直接构造返回数据
        
        List<ProductRecommendVO> result = new ArrayList<>();
        for (RecommendationResult recommendation : recommendations) {
            ProductRecommendVO vo = ProductRecommendVO.builder()
                    .id(recommendation.getId())
                    .productId(recommendation.getProductId())
                    .score(recommendation.getScore())
                    .algorithmType(recommendation.getAlgorithmType())
                    .algorithmTypeName(getAlgorithmTypeName(recommendation.getAlgorithmType()))
                    .recommendReason(generateRecommendReason(recommendation.getAlgorithmType()))
                    .expireTime(recommendation.getExpireTime())
                    .createTime(recommendation.getCreateTime())
                    .build();
            
            // 这里应该调用商品服务填充商品详情
            // 为简化实现，使用模拟数据
            vo.setProductName("商品" + recommendation.getProductId());
            vo.setProductImage("https://example.com/images/" + recommendation.getProductId() + ".jpg");
            vo.setProductPrice(new BigDecimal("99.99"));
            vo.setProductDescription("这是一个推荐商品的描述");
            
            result.add(vo);
        }
        
        return result;
    }

    @Override
    public boolean updateRecommendationExpireTime(Long id, LocalDateTime expireTime) {
        RecommendationResult recommendation = getById(id);
        if (recommendation == null) {
            return false;
        }
        
        recommendation.setExpireTime(expireTime);
        return updateById(recommendation);
    }

    @Override
    public Map<String, Object> getRecommendationExpiryStats() {
        Map<String, Object> result = new HashMap<>();
        
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        
        // 查询已过期的推荐数量
        LambdaQueryWrapper<RecommendationResult> expiredQuery = new LambdaQueryWrapper<>();
        expiredQuery.lt(RecommendationResult::getExpireTime, now);
        long expiredCount = count(expiredQuery);
        
        // 查询即将过期的推荐数量（24小时内）
        LambdaQueryWrapper<RecommendationResult> soonExpireQuery = new LambdaQueryWrapper<>();
        soonExpireQuery.ge(RecommendationResult::getExpireTime, now)
                .lt(RecommendationResult::getExpireTime, now.plusHours(24));
        long soonExpireCount = count(soonExpireQuery);
        
        // 查询有效的推荐数量
        LambdaQueryWrapper<RecommendationResult> validQuery = new LambdaQueryWrapper<>();
        validQuery.ge(RecommendationResult::getExpireTime, now);
        long validCount = count(validQuery);
        
        // 查询总推荐数量
        long totalCount = count();
        
        result.put("expired_count", expiredCount);
        result.put("soon_expire_count", soonExpireCount);
        result.put("valid_count", validCount);
        result.put("total_count", totalCount);
        result.put("expired_ratio", totalCount > 0 ? (double) expiredCount / totalCount : 0);
        
        return result;
    }
    
    /**
     * 获取算法类型名称
     * @param algorithmType 算法类型
     * @return 算法类型名称
     */
    private String getAlgorithmTypeName(Integer algorithmType) {
        switch (algorithmType) {
            case 1:
                return "基于用户的协同过滤";
            case 2:
                return "基于商品的协同过滤";
            case 3:
                return "混合协同过滤";
            case 4:
                return "热门推荐";
            case 5:
                return "新品推荐";
            default:
                return "未知算法";
        }
    }
    
    /**
     * 生成推荐理由
     * @param algorithmType 算法类型
     * @return 推荐理由
     */
    private String generateRecommendReason(Integer algorithmType) {
        switch (algorithmType) {
            case 1:
                return "与您兴趣相似的用户也喜欢这个商品";
            case 2:
                return "根据您浏览的商品推荐";
            case 3:
                return "根据您的购物偏好推荐";
            case 4:
                return "热门商品推荐";
            case 5:
                return "新品上架，为您优先推荐";
            default:
                return "猜您喜欢";
        }
    }
} 