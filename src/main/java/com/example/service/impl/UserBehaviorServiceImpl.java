package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.UserBehaviorMapper;
import com.example.model.entity.UserBehavior;
import com.example.service.UserBehaviorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.*;


/**
 * 用户行为服务实现类
 */
@Slf4j
@Service
public class UserBehaviorServiceImpl extends ServiceImpl<UserBehaviorMapper, UserBehavior> implements UserBehaviorService {

    @Autowired
    private UserBehaviorMapper userBehaviorMapper;

    @Override
    public boolean recordViewBehavior(Long userId, Long productId, Long categoryId, Integer stayTime) {
        UserBehavior behavior = UserBehavior.builder()
                .userId(userId)
                .productId(productId)
                .categoryId(categoryId)
                .behaviorType(1) // 浏览行为
                .stayTime(stayTime)
                .viewCount(1)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        return save(behavior);
    }

    @Override
    public boolean recordClickBehavior(Long userId, Long productId, Long categoryId) {
        UserBehavior behavior = UserBehavior.builder()
                .userId(userId)
                .productId(productId)
                .categoryId(categoryId)
                .behaviorType(2) // 点击行为
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        return save(behavior);
    }

    @Override
    public boolean recordCartBehavior(Long userId, Long productId, Long categoryId) {
        UserBehavior behavior = UserBehavior.builder()
                .userId(userId)
                .productId(productId)
                .categoryId(categoryId)
                .behaviorType(3) // 加入购物车行为
                .cartCount(1)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        return save(behavior);
    }

    @Override
    public boolean recordFavoriteBehavior(Long userId, Long productId, Long categoryId) {
        UserBehavior behavior = UserBehavior.builder()
                .userId(userId)
                .productId(productId)
                .categoryId(categoryId)
                .behaviorType(4) // 收藏行为
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        return save(behavior);
    }

    @Override
    public boolean recordSearchBehavior(Long userId, String keyword) {
        UserBehavior behavior = UserBehavior.builder()
                .userId(userId)
                .behaviorType(5) // 搜索行为
                .searchKeyword(keyword)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        return save(behavior);
    }

    @Override
    public boolean recordRatingBehavior(Long userId, Long productId, Long categoryId, BigDecimal rating) {
        UserBehavior behavior = UserBehavior.builder()
                .userId(userId)
                .productId(productId)
                .categoryId(categoryId)
                .behaviorType(6) // 评分行为
                .rating(rating)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        return save(behavior);
    }

    @Override
    public boolean recordReviewBehavior(Long userId, Long productId, Long categoryId, BigDecimal rating, String reviewContent) {
        UserBehavior behavior = UserBehavior.builder()
                .userId(userId)
                .productId(productId)
                .categoryId(categoryId)
                .behaviorType(7) // 评价行为
                .rating(rating)
                .reviewContent(reviewContent)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        return save(behavior);
    }

    @Override
    public boolean recordPurchaseBehavior(Long userId, Long productId, Long categoryId) {
        UserBehavior behavior = UserBehavior.builder()
                .userId(userId)
                .productId(productId)
                .categoryId(categoryId)
                .behaviorType(8) // 购买行为
                .buyCount(1)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        return save(behavior);
    }

    @Override
    public List<UserBehavior> getUserBehaviorHistory(Long userId, Integer limit) {
        return userBehaviorMapper.selectByUserId(userId, limit);
    }

    @Override
    public List<Long> getRecentViewedProducts(Long userId, Integer limit) {
        return userBehaviorMapper.selectRecentViewedProducts(userId, limit);
    }

    @Override
    public List<Long> getFavoriteProducts(Long userId, Integer limit) {
        return userBehaviorMapper.selectFavoriteProducts(userId, limit);
    }

    @Override
    public List<String> getUserSearchHistory(Long userId, Integer limit) {
        return userBehaviorMapper.selectUserSearchHistory(userId, limit);
    }

    @Override
    public List<Map<String, Object>> getHotSearchKeywords(Integer limit) {
        return userBehaviorMapper.selectHotSearchKeywords(limit);
    }

    @Override
    public BigDecimal getProductAvgRating(Long productId) {
        return userBehaviorMapper.selectAvgRatingByProductId(productId);
    }

    @Override
    public Integer getProductReviewCount(Long productId) {
        return userBehaviorMapper.countReviewsByProductId(productId);
    }

    @Override
    public IPage<UserBehavior> getProductReviews(Page<UserBehavior> page, Long productId) {
        return userBehaviorMapper.selectReviewsByProductIdPage(page, productId);
    }

    @Override
    public List<Map<String, Object>> getHotProductsByCategory(Long categoryId, Integer limit) {
        return userBehaviorMapper.selectHotProductsByCategory(categoryId, limit);
    }

    @Override
    public Map<Integer, Map<String, Object>> getBehaviorStatsByDateRange(LocalDateTime startTime, LocalDateTime endTime) {
        return userBehaviorMapper.selectBehaviorStatsByDateRange(startTime, endTime);
    }

    @Override
    public Integer getProductFavoriteCount(Long productId) {
        return userBehaviorMapper.countFavoriteUsers(productId);
    }

    @Override
    public List<Map<String, Object>> analyzeUserInterests(Long userId, Integer limit) {
        // 获取用户所有行为
        List<UserBehavior> behaviors = userBehaviorMapper.selectByUserId(userId, 1000);
        
        // 按分类统计行为
        Map<Long, Integer> categoryScores = new HashMap<>();
        
        for (UserBehavior behavior : behaviors) {
            Long categoryId = behavior.getCategoryId();
            if (categoryId == null) continue;
            
            // 根据行为类型赋予不同权重
            int score = 0;
            switch (behavior.getBehaviorType()) {
                case 1: // 浏览
                    score = 1;
                    break;
                case 2: // 点击
                    score = 2;
                    break;
                case 3: // 加入购物车
                    score = 5;
                    break;
                case 4: // 收藏
                    score = 8;
                    break;
                case 6: // 评分
                case 7: // 评价
                    score = 10;
                    break;
                case 8: // 购买
                    score = 15;
                    break;
                default:
                    score = 0;
            }
            
            // 累加分数
            categoryScores.put(categoryId, categoryScores.getOrDefault(categoryId, 0) + score);
        }
        
        // 转换为结果列表并排序
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry : categoryScores.entrySet()) {
            Map<String, Object> item = new HashMap<>();
            item.put("categoryId", entry.getKey());
            item.put("interestLevel", entry.getValue());
            result.add(item);
        }
        
        // 按兴趣程度降序排序
        result.sort((a, b) -> ((Integer) b.get("interestLevel")).compareTo((Integer) a.get("interestLevel")));
        
        // 限制返回数量
        if (result.size() > limit) {
            result = result.subList(0, limit);
        }
        
        return result;
    }

    @Override
    public List<Map<String, Object>> getUserBehaviorHeatmap(Long userId, Integer days) {
        // 获取指定天数内的用户行为
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.minusDays(days);
        
        LambdaQueryWrapper<UserBehavior> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserBehavior::getUserId, userId)
                .ge(UserBehavior::getCreateTime, startTime)
                .le(UserBehavior::getCreateTime, endTime);
        
        List<UserBehavior> behaviors = list(queryWrapper);
        
        // 按日期分组统计行为数量
        Map<LocalDate, Integer> dailyCounts = new HashMap<>();
        
        for (UserBehavior behavior : behaviors) {
            LocalDate date = behavior.getCreateTime().toLocalDate();
            dailyCounts.put(date, dailyCounts.getOrDefault(date, 0) + 1);
        }
        
        // 确保所有日期都有数据
        LocalDate currentDate = startTime.toLocalDate();
        List<Map<String, Object>> result = new ArrayList<>();
        
        while (!currentDate.isAfter(endTime.toLocalDate())) {
            Map<String, Object> dayData = new HashMap<>();
            dayData.put("date", currentDate.toString());
            dayData.put("behaviorCount", dailyCounts.getOrDefault(currentDate, 0));
            result.add(dayData);
            
            currentDate = currentDate.plusDays(1);
        }
        
        return result;
    }

    @Override
    public Integer calculateUserActivityScore(Long userId, Integer days) {
        // 获取用户热力图数据
        List<Map<String, Object>> heatmapData = getUserBehaviorHeatmap(userId, days);
        
        // 计算总行为数
        int totalBehaviors = heatmapData.stream()
                .mapToInt(data -> (Integer) data.get("behaviorCount"))
                .sum();
        
        // 计算活跃天数
        long activeDays = heatmapData.stream()
                .filter(data -> (Integer) data.get("behaviorCount") > 0)
                .count();
        
        // 计算活跃度评分 (0-100)
        // 考虑因素：总行为数、活跃天数比例
        double behaviorScore = Math.min(totalBehaviors / 2.0, 50); // 最高50分
        double activeRatio = (double) activeDays / days;
        double activeScore = activeRatio * 50; // 最高50分
        
        int finalScore = (int) Math.round(behaviorScore + activeScore);
        return Math.min(finalScore, 100); // 确保不超过100
    }

    @Override
    @Transactional
    public Integer cleanHistoricalData(LocalDateTime beforeDate) {
        LambdaQueryWrapper<UserBehavior> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.lt(UserBehavior::getCreateTime, beforeDate);
        
        long count = count(queryWrapper);
        if (count > 0) {
            remove(queryWrapper);
        }
        
        return (int) count;
    }

    @Override
    @Transactional
    public Integer mergeUserBehaviors(Long sourceUserId, Long targetUserId) {
        // 获取源用户的所有行为
        LambdaQueryWrapper<UserBehavior> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserBehavior::getUserId, sourceUserId);
        List<UserBehavior> sourceBehaviors = list(queryWrapper);
        
        int count = 0;
        if (!sourceBehaviors.isEmpty()) {
            // 更新用户ID
            for (UserBehavior behavior : sourceBehaviors) {
                behavior.setUserId(targetUserId);
                behavior.setUpdateTime(LocalDateTime.now());
                count++;
            }
            
            // 批量更新
            updateBatchById(sourceBehaviors);
        }
        
        return count;
    }
} 