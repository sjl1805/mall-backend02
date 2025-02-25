package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.UserBehaviorMapper;
import com.example.model.entity.Products;
import com.example.model.entity.UserBehavior;
import com.example.model.entity.Users;
import com.example.service.UserBehaviorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户行为服务实现类
 * <p>
 * 该类实现了用户行为相关的业务逻辑，包括记录、查询、更新和删除用户行为数据。
 * 用户行为数据用于分析用户偏好、推荐商品和个性化营销等功能。
 * 使用了Spring缓存机制对用户行为数据进行缓存，提高查询效率。
 * 使用MyBatis-Plus的ServiceImpl简化数据访问操作。
 *
 * @author 31815
 * @description 针对表【user_behavior(用户行为记录表)】的数据库操作Service实现
 * @createDate 2025-02-24 12:03:47
 */
@Service
@CacheConfig(cacheNames = "userBehaviors") // 指定该服务类的缓存名称为"userBehaviors"
public class UserBehaviorServiceImpl extends ServiceImpl<UserBehaviorMapper, UserBehavior>
        implements UserBehaviorService {

    @Autowired
    private UserBehaviorMapper userBehaviorMapper;

    /**
     * 根据用户ID查询用户行为列表
     * <p>
     * 该方法从缓存或数据库获取指定用户的所有行为数据，
     * 可用于分析用户兴趣偏好和行为习惯
     *
     * @param userId 用户ID
     * @return 用户行为列表
     */
    @Override
    @Cacheable(value = "userBehaviors", key = "#userId") // 缓存用户行为数据，提高查询效率
    public List<UserBehavior> selectByUserId(Long userId) {
        return userBehaviorMapper.selectByUserId(userId);
    }

    /**
     * 分页查询用户行为数据
     * <p>
     * 该方法用于后台管理系统分页查看用户行为数据
     *
     * @param page 分页参数
     * @return 用户行为分页数据
     */
    @Override
    public IPage<UserBehavior> selectPage(IPage<UserBehavior> page) {
        return userBehaviorMapper.selectPage(page);
    }

    /**
     * 根据ID查询用户行为记录
     *
     * @param id 用户行为ID
     * @return 用户行为实体
     */
    @Override
    public UserBehavior selectById(Long id) {
        return userBehaviorMapper.selectById(id);
    }

    /**
     * 添加用户行为记录
     * <p>
     * 该方法用于记录用户的行为数据，如浏览商品、收藏商品、购买商品等，
     * 并清除相关用户的行为缓存，确保数据一致性
     *
     * @param userBehavior 用户行为实体
     * @return 添加成功返回true，失败返回false
     */
    @Override
    @Transactional
    @CacheEvict(value = "userBehaviors", key = "#userBehavior.userId") // 清除用户行为缓存
    public boolean insertUserBehavior(UserBehavior userBehavior) {
        return userBehaviorMapper.insert(userBehavior) > 0;
    }

    /**
     * 更新用户行为记录
     * <p>
     * 该方法用于更新用户行为数据，如修改行为权重、停留时长等，
     * 并清除相关用户的行为缓存，确保数据一致性
     *
     * @param userBehavior 用户行为实体
     * @return 更新成功返回true，失败返回false
     */
    @Override
    @Transactional
    @CacheEvict(value = "userBehaviors", key = "#userBehavior.userId") // 清除用户行为缓存
    public boolean updateUserBehavior(UserBehavior userBehavior) {
        return userBehaviorMapper.updateById(userBehavior) > 0;
    }

    /**
     * 删除用户行为记录
     * <p>
     * 该方法用于删除用户行为数据，
     * 并清除相关缓存，确保数据一致性
     *
     * @param id 用户行为ID
     * @return 删除成功返回true，失败返回false
     */
    @Override
    @Transactional
    @CacheEvict(value = "userBehaviors", key = "#id") // 清除被删除行为的缓存
    public boolean deleteUserBehavior(Long id) {
        return userBehaviorMapper.deleteById(id) > 0;
    }

    /**
     * 根据行为类型查询用户行为
     * <p>
     * 该方法根据指定的行为类型查询用户行为数据，
     * 可用于分析特定行为的总体趋势和分布
     *
     * @param behaviorType 行为类型
     * @return 用户行为列表
     */
    @Override
    @Cacheable(value = "userBehaviors", key = "'type_' + #behaviorType")
    public List<UserBehavior> selectByBehaviorType(Integer behaviorType) {
        return userBehaviorMapper.selectByBehaviorType(behaviorType);
    }

    /**
     * 根据用户ID和行为类型查询
     * <p>
     * 该方法根据用户ID和行为类型查询用户行为数据，
     * 用于分析用户的特定行为模式
     *
     * @param userId       用户ID
     * @param behaviorType 行为类型
     * @return 用户行为列表
     */
    @Override
    @Cacheable(value = "userBehaviors", key = "#userId + '_' + #behaviorType")
    public List<UserBehavior> selectByUserIdAndType(Long userId, Integer behaviorType) {
        return userBehaviorMapper.selectByUserIdAndType(userId, behaviorType);
    }

    /**
     * 根据时间范围查询用户行为
     * <p>
     * 该方法查询指定时间范围内的用户行为数据，
     * 用于分析特定时期的用户行为特点
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 用户行为列表
     */
    @Override
    @Cacheable(value = "userBehaviors", key = "'time_' + #startTime + '_' + #endTime")
    public List<UserBehavior> selectByTimeRange(Date startTime, Date endTime) {
        return userBehaviorMapper.selectByTimeRange(startTime, endTime);
    }

    /**
     * 查询热门商品
     * <p>
     * 该方法基于用户行为数据分析热门商品，
     * 用于首页推荐、热销榜单等功能
     *
     * @param behaviorType 行为类型
     * @param startTime    开始时间
     * @param endTime      结束时间
     * @param limit        限制数量
     * @return 热门商品列表
     */
    @Override
    @Cacheable(value = "userBehaviors", key = "'popular_' + #behaviorType + '_' + #startTime + '_' + #endTime + '_' + #limit")
    public List<Products> selectPopularProducts(
            Integer behaviorType, Date startTime, Date endTime, Integer limit) {
        return userBehaviorMapper.selectPopularProducts(behaviorType, startTime, endTime, limit);
    }

    /**
     * 获取用户兴趣分类
     * <p>
     * 该方法分析用户对不同分类的兴趣程度，
     * 用于个性化推荐和兴趣标签生成
     *
     * @param userId    用户ID
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 用户兴趣分类
     */
    @Override
    @Cacheable(value = "userBehaviors", key = "'interests_' + #userId + '_' + #startTime + '_' + #endTime")
    public List<Users> selectUserInterests(Long userId, Date startTime, Date endTime) {
        return userBehaviorMapper.selectUserInterests(userId, startTime, endTime);
    }

    /**
     * 批量插入用户行为
     * <p>
     * 该方法批量记录用户行为数据，
     * 适用于日志同步、批量导入等场景
     *
     * @param behaviors 用户行为列表
     * @return 插入结果
     */
    @Override
    @Transactional
    @CacheEvict(value = "userBehaviors", allEntries = true)
    public boolean batchInsertBehaviors(List<UserBehavior> behaviors) {
        if (behaviors == null || behaviors.isEmpty()) {
            return false;
        }
        return userBehaviorMapper.batchInsertBehaviors(behaviors) > 0;
    }

    /**
     * 高级条件查询用户行为
     * <p>
     * 该方法支持多条件组合查询用户行为数据，
     * 适用于复杂的数据分析和筛选场景
     *
     * @param userId       用户ID（可选）
     * @param productId    商品ID（可选）
     * @param behaviorType 行为类型（可选）
     * @param startTime    开始时间（可选）
     * @param endTime      结束时间（可选）
     * @param page         分页参数
     * @return 分页用户行为
     */
    @Override
    public IPage<UserBehavior> selectBehaviorsByCondition(
            Long userId, Long productId, Integer behaviorType,
            Date startTime, Date endTime, Page<UserBehavior> page) {
        return userBehaviorMapper.selectBehaviorsByCondition(
                userId, productId, behaviorType, startTime, endTime, page);
    }

    /**
     * 统计用户行为数量
     * <p>
     * 该方法统计用户特定类型行为的数量，
     * 用于用户活跃度分析和行为频率监控
     *
     * @param userId       用户ID
     * @param behaviorType 行为类型
     * @return 行为数量
     */
    @Override
    @Cacheable(value = "userBehaviors", key = "'count_' + #userId + '_' + #behaviorType")
    public int countUserBehaviors(Long userId, Integer behaviorType) {
        return userBehaviorMapper.countUserBehaviors(userId, behaviorType);
    }

    /**
     * 分析用户行为时间分布
     * <p>
     * 该方法分析用户行为在一天中的时间分布，
     * 用于了解用户的活跃时段，优化营销时间
     *
     * @param userId       用户ID
     * @param behaviorType 行为类型
     * @return 行为时间分布（小时 -> 次数）
     */
    @Override
    @Cacheable(value = "userBehaviors", key = "'time_dist_' + #userId + '_' + #behaviorType")
    public Map<Integer, Integer> analyzeUserBehaviorTimeDistribution(Long userId, Integer behaviorType) {
        // 获取用户行为数据
        List<UserBehavior> behaviors = selectByUserIdAndType(userId, behaviorType);

        // 统计每小时的行为次数
        Map<Integer, Integer> hourDistribution = new HashMap<>(24);
        for (int i = 0; i < 24; i++) {
            hourDistribution.put(i, 0);
        }

        for (UserBehavior behavior : behaviors) {
            int hour = behavior.getBehaviorTime().getHour();
            hourDistribution.put(hour, hourDistribution.get(hour) + 1);
        }

        return hourDistribution;
    }

    /**
     * 分析用户行为路径
     * <p>
     * 该方法跟踪用户从浏览到购买的行为路径，
     * 用于了解用户决策过程和优化转化漏斗
     *
     * @param userId    用户ID
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 用户行为路径
     */
    @Override
    @Cacheable(value = "userBehaviors", key = "'path_' + #userId + '_' + #startTime + '_' + #endTime")
    public List<Map<String, Object>> analyzeUserBehaviorPath(Long userId, Date startTime, Date endTime) {
        // 此处需要实际实现，可能需要更复杂的SQL查询
        // 简化实现，按时间顺序返回用户行为
        List<UserBehavior> behaviors = userBehaviorMapper.selectByUserIdAndTimeRange(
                userId, startTime, endTime);

        List<Map<String, Object>> path = new ArrayList<>();
        for (UserBehavior behavior : behaviors) {
            Map<String, Object> node = new HashMap<>();
            node.put("behaviorId", behavior.getId());
            node.put("behaviorType", behavior.getBehaviorType());
            node.put("productId", behavior.getProductId());
            node.put("behaviorTime", behavior.getBehaviorTime());
            node.put("duration", behavior.getDuration());
            path.add(node);
        }

        return path;
    }

    /**
     * 计算用户活跃度
     * <p>
     * 该方法基于用户行为频率、类型多样性、时间跨度等因素，
     * 计算用户的活跃度得分，用于用户分层和精准营销
     *
     * @param userId 用户ID
     * @param days   天数，计算最近几天的活跃度
     * @return 活跃度得分
     */
    @Override
    @Cacheable(value = "userBehaviors", key = "'activity_' + #userId + '_' + #days")
    public BigDecimal calculateUserActivityScore(Long userId, Integer days) {
        // 获取最近N天的行为数据
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -days);
        Date startTime = calendar.getTime();
        Date endTime = new Date();

        List<UserBehavior> behaviors = userBehaviorMapper.selectByUserIdAndTimeRange(
                userId, startTime, endTime);

        if (behaviors.isEmpty()) {
            return BigDecimal.ZERO;
        }

        // 计算行为频率
        BigDecimal frequency = new BigDecimal(behaviors.size()).divide(
                new BigDecimal(days), 2, RoundingMode.HALF_UP);

        // 计算行为多样性（不同类型的行为数）
        Set<Integer> behaviorTypes = behaviors.stream()
                .map(UserBehavior::getBehaviorType)
                .collect(Collectors.toSet());
        BigDecimal diversity = new BigDecimal(behaviorTypes.size());

        // 计算加权得分
        BigDecimal weightedSum = BigDecimal.ZERO;
        for (UserBehavior behavior : behaviors) {
            weightedSum = weightedSum.add(behavior.getWeight());
        }

        // 综合得分：频率*30% + 多样性*20% + 加权和*50%
        BigDecimal activityScore = frequency.multiply(new BigDecimal("0.3"))
                .add(diversity.multiply(new BigDecimal("0.2")))
                .add(weightedSum.multiply(new BigDecimal("0.5")));

        return activityScore;
    }

    /**
     * 识别用户异常行为
     * 检测与用户正常行为模式偏离较大的行为
     *
     * @param userId 用户ID
     * @param days   天数，检测最近几天的行为
     * @return 异常行为列表
     */
    @Override
    @Cacheable(value = "userBehaviors", key = "'abnormal_' + #userId + '_' + #days")
    public List<UserBehavior> detectAbnormalBehaviors(Long userId, Integer days) {
        // 获取用户最近n天的行为
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -days);
        Date startTime = calendar.getTime();
        Date endTime = new Date();

        List<UserBehavior> behaviors = userBehaviorMapper.selectByUserIdAndTimeRange(
                userId, startTime, endTime);

        // 简单实现：筛选权重特别高或特别低的行为作为异常行为
        return behaviors.stream()
                .filter(b -> b.getWeight().compareTo(new BigDecimal("0.2")) < 0
                        || b.getWeight().compareTo(new BigDecimal("0.8")) > 0)
                .collect(Collectors.toList());
    }

    /**
     * 分析用户行为转化
     * 计算从一种行为到另一种行为的转化率
     *
     * @param sourceType 源行为类型
     * @param targetType 目标行为类型
     * @param days       天数
     * @return 转化率
     */
    @Override
    @Cacheable(value = "userBehaviors", key = "'conversion_' + #sourceType + '_' + #targetType + '_' + #days")
    public BigDecimal analyzeBehaviorConversion(Integer sourceType, Integer targetType, Integer days) {
        Map<String, Object> conversionData = userBehaviorMapper.analyzeBehaviorConversion(
                sourceType, targetType, days);

        if (conversionData == null || !conversionData.containsKey("conversion_rate")) {
            return BigDecimal.ZERO;
        }

        return new BigDecimal(conversionData.get("conversion_rate").toString());
    }

    /**
     * 预测用户下一步可能的行为
     * 基于用户历史行为模式预测未来可能的行为
     *
     * @param userId 用户ID
     * @return 可能的行为类型及概率
     */
    @Override
    @Cacheable(value = "userBehaviors", key = "'predict_' + #userId")
    public Map<Integer, BigDecimal> predictNextBehavior(Long userId) {
        // 获取用户最近的行为数据
        List<UserBehavior> recentBehaviors = userBehaviorMapper.selectByUserId(userId);

        if (recentBehaviors == null || recentBehaviors.isEmpty()) {
            return new HashMap<>();
        }

        // 简单实现：根据用户历史行为的频率来预测下一步行为的概率
        Map<Integer, BigDecimal> behaviorCounts = new HashMap<>();
        Map<Integer, BigDecimal> behaviorProbabilities = new HashMap<>();

        // 统计各行为类型的次数
        for (UserBehavior behavior : recentBehaviors) {
            Integer type = behavior.getBehaviorType();
            behaviorCounts.put(type, behaviorCounts.getOrDefault(type, BigDecimal.ZERO).add(BigDecimal.ONE));
        }

        // 计算各行为类型的概率
        BigDecimal total = new BigDecimal(recentBehaviors.size());
        for (Map.Entry<Integer, BigDecimal> entry : behaviorCounts.entrySet()) {
            behaviorProbabilities.put(
                    entry.getKey(),
                    entry.getValue().divide(total, 4, RoundingMode.HALF_UP)
            );
        }

        return behaviorProbabilities;
    }
}




