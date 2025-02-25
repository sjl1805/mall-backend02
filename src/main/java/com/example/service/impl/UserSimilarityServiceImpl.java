package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.ResultCode;
import com.example.constants.RecommendConstants;
import com.example.exception.BusinessException;
import com.example.mapper.OrderItemMapper;
import com.example.mapper.UserMapper;
import com.example.mapper.UserSimilarityMapper;
import com.example.model.entity.OrderItem;
import com.example.model.entity.User;
import com.example.model.entity.UserSimilarity;
import com.example.model.vo.RecommendUserVO;
import com.example.model.vo.SimilarityPairVO;
import com.example.service.UserSimilarityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户相似度服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserSimilarityServiceImpl extends ServiceImpl<UserSimilarityMapper, UserSimilarity> implements UserSimilarityService {

    private final UserSimilarityMapper userSimilarityMapper;
    private final UserMapper userMapper;
    private final OrderItemMapper orderItemMapper;

    @Override
    public List<UserSimilarity> getMostSimilarUsers(Long userId, Integer limit) {
        if (userId == null) {
            return Collections.emptyList();
        }
        
        // 设置默认限制数量
        if (limit == null || limit <= 0) {
            limit = RecommendConstants.DEFAULT_SIMILAR_USERS_LIMIT;
        }
        
        return userSimilarityMapper.selectMostSimilarUsers(userId, limit);
    }

    @Override
    public UserSimilarity getSimilarityBetweenUsers(Long userIdA, Long userIdB) {
        if (userIdA == null || userIdB == null) {
            return null;
        }
        
        // 确保userIdA小于userIdB，以保持一致性
        if (userIdA > userIdB) {
            Long temp = userIdA;
            userIdA = userIdB;
            userIdB = temp;
        }
        
        return userSimilarityMapper.selectByUserIds(userIdA, userIdB);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateOrInsertSimilarity(Long userIdA, Long userIdB, BigDecimal similarity) {
        if (userIdA == null || userIdB == null || similarity == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "参数不能为空");
        }
        
        // 确保userIdA小于userIdB，以保持一致性
        if (userIdA > userIdB) {
            Long temp = userIdA;
            userIdA = userIdB;
            userIdB = temp;
        }
        
        // 确保相似度在0-1之间
        if (similarity.compareTo(BigDecimal.ZERO) < 0) {
            similarity = BigDecimal.ZERO;
        } else if (similarity.compareTo(BigDecimal.ONE) > 0) {
            similarity = BigDecimal.ONE;
        }
        
        // 保留四位小数
        similarity = similarity.setScale(4, RoundingMode.HALF_UP);
        
        LocalDateTime now = LocalDateTime.now();
        int rows = userSimilarityMapper.insertOrUpdate(userIdA, userIdB, similarity, now);
        return rows > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchInsertSimilarities(List<UserSimilarity> similarityList) {
        if (CollectionUtils.isEmpty(similarityList)) {
            return false;
        }
        
        // 确保每个相似度记录的userIdA小于userIdB，并设置更新时间
        LocalDateTime now = LocalDateTime.now();
        for (UserSimilarity similarity : similarityList) {
            if (similarity.getUserIdA() > similarity.getUserIdB()) {
                Long temp = similarity.getUserIdA();
                similarity.setUserIdA(similarity.getUserIdB());
                similarity.setUserIdB(temp);
            }
            
            // 确保相似度在0-1之间
            BigDecimal similarityValue = similarity.getSimilarity();
            if (similarityValue.compareTo(BigDecimal.ZERO) < 0) {
                similarity.setSimilarity(BigDecimal.ZERO);
            } else if (similarityValue.compareTo(BigDecimal.ONE) > 0) {
                similarity.setSimilarity(BigDecimal.ONE);
            }
            
            // 保留四位小数
            similarity.setSimilarity(similarityValue.setScale(4, RoundingMode.HALF_UP));
            
            similarity.setUpdateTime(now);
        }
        
        int rows = userSimilarityMapper.batchInsert(similarityList);
        return rows > 0;
    }

    @Override
    public List<UserSimilarity> getSimilarUsersByThreshold(BigDecimal threshold) {
        if (threshold == null) {
            threshold = RecommendConstants.DEFAULT_SIMILARITY_THRESHOLD;
        }
        
        return userSimilarityMapper.selectByThreshold(threshold);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteOldSimilarities(LocalDateTime beforeTime) {
        if (beforeTime == null) {
            beforeTime = LocalDateTime.now().minusDays(RecommendConstants.DEFAULT_SIMILARITY_EXPIRY_DAYS);
        }
        
        return userSimilarityMapper.deleteBeforeTime(beforeTime);
    }

    @Override
    public List<UserSimilarity> getAllSimilaritiesByUserId(Long userId) {
        if (userId == null) {
            return Collections.emptyList();
        }
        
        return userSimilarityMapper.selectAllByUserId(userId);
    }

    @Override
    public BigDecimal calculateUserSimilarity(Long userIdA, Long userIdB) {
        if (userIdA == null || userIdB == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "用户ID不能为空");
        }
        
        if (userIdA.equals(userIdB)) {
            return BigDecimal.ONE; // 相同用户相似度为1
        }
        
        // 获取用户A的购买记录
        List<OrderItem> userAItems = orderItemMapper.selectRecentPurchasesByUserId(userIdA, RecommendConstants.MAX_PURCHASE_HISTORY_SIZE);
        // 获取用户B的购买记录
        List<OrderItem> userBItems = orderItemMapper.selectRecentPurchasesByUserId(userIdB, RecommendConstants.MAX_PURCHASE_HISTORY_SIZE);
        
        if (CollectionUtils.isEmpty(userAItems) || CollectionUtils.isEmpty(userBItems)) {
            return BigDecimal.ZERO; // 如果任一用户没有购买记录，相似度为0
        }
        
        // 构建用户A的商品ID到购买数量的映射
        Map<Long, Integer> userAProductMap = userAItems.stream()
                .collect(Collectors.toMap(
                        OrderItem::getProductId,
                        OrderItem::getQuantity,
                        Integer::sum)); // 合并相同商品ID的数量
        
        // 构建用户B的商品ID到购买数量的映射
        Map<Long, Integer> userBProductMap = userBItems.stream()
                .collect(Collectors.toMap(
                        OrderItem::getProductId,
                        OrderItem::getQuantity,
                        Integer::sum)); // 合并相同商品ID的数量
        
        // 计算余弦相似度
        // 公式: cos(A,B) = (A·B) / (|A|·|B|)
        // 其中A·B是向量点积，|A|和|B|分别是向量A和B的模
        
        // 计算向量点积(A·B)
        double dotProduct = 0.0;
        for (Map.Entry<Long, Integer> entry : userAProductMap.entrySet()) {
            Long productId = entry.getKey();
            Integer quantityA = entry.getValue();
            
            if (userBProductMap.containsKey(productId)) {
                Integer quantityB = userBProductMap.get(productId);
                dotProduct += quantityA * quantityB;
            }
        }
        
        // 计算向量A的模的平方
        double normASquared = userAProductMap.values().stream()
                .mapToDouble(quantity -> quantity * quantity)
                .sum();
        
        // 计算向量B的模的平方
        double normBSquared = userBProductMap.values().stream()
                .mapToDouble(quantity -> quantity * quantity)
                .sum();
        
        // 计算相似度
        double similarity = 0.0;
        if (normASquared > 0 && normBSquared > 0) {
            similarity = dotProduct / (Math.sqrt(normASquared) * Math.sqrt(normBSquared));
        }
        
        // 确保相似度在0-1之间
        similarity = Math.max(0.0, Math.min(1.0, similarity));
        
        return new BigDecimal(similarity).setScale(4, RoundingMode.HALF_UP);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int buildUserSimilarityMatrix(Integer batchSize) {
        if (batchSize == null || batchSize <= 0) {
            batchSize = RecommendConstants.DEFAULT_MATRIX_BUILD_BATCH_SIZE;
        }
        
        // 获取所有活跃用户ID
        List<Long> userIds = userMapper.selectActiveUserIds(RecommendConstants.DEFAULT_ACTIVE_DAYS);
        if (CollectionUtils.isEmpty(userIds)) {
            log.info("没有活跃用户，跳过构建相似度矩阵");
            return 0;
        }
        
        int totalPairs = 0;
        List<UserSimilarity> batchSimilarities = new ArrayList<>(batchSize);
        
        // 遍历所有用户对，计算相似度
        for (int i = 0; i < userIds.size(); i++) {
            for (int j = i + 1; j < userIds.size(); j++) {
                Long userIdA = userIds.get(i);
                Long userIdB = userIds.get(j);
                
                try {
                    // 计算相似度
                    BigDecimal similarity = calculateUserSimilarity(userIdA, userIdB);
                    
                    // 创建相似度记录
                    UserSimilarity similarityRecord = UserSimilarity.builder()
                            .userIdA(userIdA)
                            .userIdB(userIdB)
                            .similarity(similarity)
                            .updateTime(LocalDateTime.now())
                            .build();
                    
                    // 添加到批量处理列表
                    batchSimilarities.add(similarityRecord);
                    totalPairs++;
                    
                    // 达到批处理大小时，执行批量插入
                    if (batchSimilarities.size() >= batchSize) {
                        userSimilarityMapper.batchInsert(batchSimilarities);
                        batchSimilarities.clear();
                        log.info("已处理 {} 对用户相似度", totalPairs);
                    }
                } catch (Exception e) {
                    log.error("计算用户 {} 和 {} 的相似度时出错", userIdA, userIdB, e);
                }
            }
        }
        
        // 处理剩余的记录
        if (!batchSimilarities.isEmpty()) {
            userSimilarityMapper.batchInsert(batchSimilarities);
            log.info("最终处理 {} 对用户相似度", totalPairs);
        }
        
        return totalPairs;
    }

    @Override
    public List<RecommendUserVO> getRecommendedUsers(Long userId, Integer limit) {
        if (userId == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "用户ID不能为空");
        }
        
        if (limit == null || limit <= 0) {
            limit = RecommendConstants.DEFAULT_RECOMMEND_USERS_LIMIT;
        }
        
        // 获取与当前用户相似度最高的用户
        List<UserSimilarity> similarUsers = getMostSimilarUsers(userId, limit * 2); // 获取更多，以便后续过滤
        if (CollectionUtils.isEmpty(similarUsers)) {
            log.info("用户 {} 没有相似用户", userId);
            return Collections.emptyList();
        }
        
        // 收集相似用户ID
        List<Long> similarUserIds = similarUsers.stream()
                .map(similarity -> similarity.getUserIdA().equals(userId) ? 
                        similarity.getUserIdB() : similarity.getUserIdA())
                .collect(Collectors.toList());
        
        // 获取这些用户的基本信息
        List<User> userInfoList = userMapper.selectBatchIds(similarUserIds);
        Map<Long, User> userInfoMap = userInfoList.stream()
                .collect(Collectors.toMap(User::getId, user -> user));
        
        // 构建推荐用户VO列表
        List<RecommendUserVO> recommendUsers = new ArrayList<>();
        for (UserSimilarity similarity : similarUsers) {
            // 确定相似用户ID
            Long similarUserId = similarity.getUserIdA().equals(userId) ? 
                    similarity.getUserIdB() : similarity.getUserIdA();
            
            // 获取相似用户信息
            User similarUser = userInfoMap.get(similarUserId);
            if (similarUser != null) {
                RecommendUserVO recommendUser = new RecommendUserVO();
                recommendUser.setUserId(similarUserId);
                recommendUser.setUsername(similarUser.getUsername());
                recommendUser.setNickname(similarUser.getNickname());
                recommendUser.setAvatar(similarUser.getAvatar());
                recommendUser.setSimilarity(similarity.getSimilarity());
                
                // 可以添加更多信息，如共同兴趣等
                
                recommendUsers.add(recommendUser);
            }
        }
        
        // 按相似度降序排序并限制数量
        recommendUsers.sort((a, b) -> b.getSimilarity().compareTo(a.getSimilarity()));
        if (recommendUsers.size() > limit) {
            recommendUsers = recommendUsers.subList(0, limit);
        }
        
        return recommendUsers;
    }

    @Override
    public List<SimilarityPairVO>  getHighSimilarityUserPairs(BigDecimal threshold) {
        if (threshold == null) {
            threshold = RecommendConstants.DEFAULT_SIMILARITY_THRESHOLD;
        }
        
        // 获取高相似度用户对
        List<UserSimilarity> highSimilarities = getSimilarUsersByThreshold(threshold);
        if (CollectionUtils.isEmpty(highSimilarities)) {
            return Collections.emptyList();
        }
        
        // 收集所有涉及的用户ID
        List<Long> allUserIds = new ArrayList<>();
        for (UserSimilarity similarity : highSimilarities) {
            allUserIds.add(similarity.getUserIdA());
            allUserIds.add(similarity.getUserIdB());
        }
        
        // 获取这些用户的基本信息
        List<User> userInfoList = userMapper.selectBatchIds(allUserIds);
        Map<Long, User> userInfoMap = userInfoList.stream()
                .collect(Collectors.toMap(User::getId, user -> user));
        
        // 构建相似度对VO列表
        List<SimilarityPairVO> similarityPairs = new ArrayList<>();
        
        for (UserSimilarity similarity : highSimilarities) {
            Long userIdA = similarity.getUserIdA();
            Long userIdB = similarity.getUserIdB();
            
            User userA = userInfoMap.get(userIdA);
            User userB = userInfoMap.get(userIdB);
            
            if (userA != null && userB != null) {
                SimilarityPairVO pairVO = new SimilarityPairVO();
                pairVO.setUserIdA(userIdA);
                pairVO.setUserIdB(userIdB);
                pairVO.setSimilarity(similarity.getSimilarity());
                
                similarityPairs.add(pairVO);
            }
        }

        return similarityPairs;
    }

    @Override
    public Map<String, Integer> getSimilarityDistribution(Integer days) {
        if (days == null || days <= 0) {
            days = RecommendConstants.DEFAULT_SIMILARITY_EXPIRY_DAYS;
        }
        
        LocalDateTime beforeTime = LocalDateTime.now().minusDays(days);
        List<UserSimilarity> similarities = userSimilarityMapper.selectList(
                new LambdaQueryWrapper<UserSimilarity>()
                        .lt(UserSimilarity::getUpdateTime, beforeTime));
                        
        Map<String, Integer> distribution = new HashMap<>();
        for (UserSimilarity similarity : similarities) {
            String key = similarity.getUserIdA() + "_" + similarity.getUserIdB();
            distribution.put(key, distribution.getOrDefault(key, 0) + 1);
        }
        
        return distribution;
    }
}

    
