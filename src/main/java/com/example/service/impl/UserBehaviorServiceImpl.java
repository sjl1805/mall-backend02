package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.ResultCode;
import com.example.exception.BusinessException;
import com.example.mapper.UserBehaviorMapper;
import com.example.model.dto.users.UserBehaviorDTO;
import com.example.model.entity.UserBehavior;
import com.example.service.UserBehaviorService;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户行为服务实现类
 * 
 * @author 31815
 * @description 实现用户行为核心业务逻辑，包含：
 *              1. 行为重复校验
 *              2. 权重计算优化
 *              3. 缓存策略管理
 * @createDate 2025-02-18 23:43:52
 */
@Service
@CacheConfig(cacheNames = "userBehaviorService")
public class UserBehaviorServiceImpl extends ServiceImpl<UserBehaviorMapper, UserBehavior>
        implements UserBehaviorService {

    /**
     * 记录用户行为（完整校验）
     * @param behaviorDTO 行为信息
     * @return 操作结果
     * @implNote 业务逻辑：
     *           1. 校验重复行为
     *           2. 保存行为记录
     *           3. 清除用户缓存
     */
    @Override
    @Transactional
    @CacheEvict(key = "'user:' + #behaviorDTO.userId")
    public boolean recordBehavior(UserBehaviorDTO behaviorDTO) {
        if (baseMapper.checkBehaviorExists(
                behaviorDTO.getUserId(),
                behaviorDTO.getProductId(),
                behaviorDTO.getBehaviorType()
        ) > 0) {
            throw new BusinessException(ResultCode.BEHAVIOR_DUPLICATE);
        }

        UserBehavior behavior = new UserBehavior();
        BeanUtils.copyProperties(behaviorDTO, behavior);
        return save(behavior);
    }

    /**
     * 获取最近行为（缓存优化）
     * @param userId 用户ID
     * @param limit 最大数量
     * @return 行为列表
     * @implNote 缓存策略：
     *           1. 缓存键：user:{userId}:recent
     *           2. 缓存时间：15分钟
     */
    @Override
    @Cacheable(key = "'user:' + #userId + ':recent'")
    public List<UserBehaviorDTO> getRecentBehaviors(Long userId, Integer limit) {
        return baseMapper.selectRecentBehaviors(userId, limit).stream()
                .map(UserBehaviorDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 计算用户权重（缓存优化）
     * @param userId 用户ID
     * @return 权重结果
     * @implNote 缓存策略：
     *           1. 缓存键：user:{userId}:weight
     *           2. 缓存时间：1小时
     */
    @Override
    @Cacheable(key = "'user:' + #userId + ':weight'")
    public Map<String, Object> getUserWeight(Long userId) {
        return baseMapper.calculateUserWeight(userId);
    }

    /**
     * 获取行为分布（缓存优化）
     * @param days 统计天数
     * @return 分布数据
     * @implNote 缓存策略：
     *           1. 缓存键：distribution:{days}
     *           2. 缓存时间：2小时
     */
    @Override
    @Cacheable(key = "'distribution:' + #days")
    public List<Map<String, Object>> getBehaviorDistribution(Integer days) {
        return baseMapper.countBehaviorDistribution(days);
    }

    /**
     * 批量记录行为（事务操作）
     * @param behaviors 行为列表
     * @return 操作结果
     * @implNote 执行逻辑：
     *           1. 批量校验重复
     *           2. 批量插入数据
     *           3. 清除全量缓存
     */
    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public boolean batchRecordBehaviors(List<UserBehaviorDTO> behaviors) {
        behaviors.forEach(behavior -> {
            if (baseMapper.checkBehaviorExists(
                    behavior.getUserId(),
                    behavior.getProductId(),
                    behavior.getBehaviorType()
            ) > 0) {
                throw new BusinessException(ResultCode.BEHAVIOR_DUPLICATE);
            }
        });
        return baseMapper.batchInsert(behaviors) > 0;
    }

}




