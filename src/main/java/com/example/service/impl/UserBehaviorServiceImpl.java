package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.service.UserBehaviorService;
import com.example.model.entity.UserBehavior;
import com.example.mapper.UserBehaviorMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import java.util.List;
import java.util.Map;
import com.example.model.dto.users.UserBehaviorDTO;
import com.example.exception.BusinessException;
import com.example.common.ResultCode;
/**
* @author 31815
* @description 针对表【user_behavior(用户行为记录表)】的数据库操作Service实现
* @createDate 2025-02-18 23:43:52
*/
@Service
@CacheConfig(cacheNames = "userBehaviorService")
public class UserBehaviorServiceImpl extends ServiceImpl<UserBehaviorMapper, UserBehavior>
    implements UserBehaviorService {

    @Override
    @Transactional
    @CacheEvict(key = "'user:' + #userId")
    public boolean recordBehavior(UserBehaviorDTO behaviorDTO) {
        // 校验重复行为
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

    @Override
    @Cacheable(key = "'user:' + #userId + ':recent'")
    public List<UserBehavior> getRecentBehaviors(Long userId, Integer limit) {
        return baseMapper.selectRecentBehaviors(userId, limit);
    }

    @Override
    @Cacheable(key = "'user:' + #userId + ':weight'")
    public Map<String, Object> getUserWeight(Long userId) {
        return baseMapper.calculateUserWeight(userId);
    }

    @Override
    @Cacheable(key = "'distribution:' + #days")
    public List<Map<String, Object>> getBehaviorDistribution(Integer days) {
        return baseMapper.countBehaviorDistribution(days);
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public boolean batchRecordBehaviors(List<UserBehaviorDTO> behaviors) {
        return baseMapper.batchInsert(behaviors) > 0;
    }

    // @Override
    // @Scheduled(cron = "0 0 4 * * ?") // 每天凌晨4点执行
    // @Transactional
    // @CacheEvict(allEntries = true)
    // public void analyzeBehaviors() {
    //     Date end = new Date();
    //     Date start = DateUtils.addDays(end, -7);
    //     List<Map<String, Object>> patterns = baseMapper.analyzeBehaviorPattern(
    //         start, end, 30
    //     );
    //     // 将分析结果存储到数据库或发送到消息队列
    // }
}




