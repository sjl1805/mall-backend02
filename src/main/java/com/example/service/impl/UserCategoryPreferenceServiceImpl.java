package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.UserCategoryPreferenceMapper;
import com.example.model.entity.UserCategoryPreference;
import com.example.service.UserCategoryPreferenceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用户-分类偏好关联服务实现类
 */
@Service
public class UserCategoryPreferenceServiceImpl extends ServiceImpl<UserCategoryPreferenceMapper, UserCategoryPreference> implements UserCategoryPreferenceService {

    @Override
    public List<UserCategoryPreference> getByUserId(Long userId) {
        return baseMapper.selectByUserId(userId);
    }

    @Override
    public List<UserCategoryPreference> getByCategoryId(Long categoryId) {
        return baseMapper.selectByCategoryId(categoryId);
    }

    @Override
    public List<UserCategoryPreference> getHighPreferenceCategories(Long userId, BigDecimal minPreference) {
        return baseMapper.selectHighPreferenceCategories(userId, minPreference);
    }

    @Override
    public List<UserCategoryPreference> getHighPreferenceUsers(Long categoryId, BigDecimal minPreference) {
        return baseMapper.selectHighPreferenceUsers(categoryId, minPreference);
    }

    @Override
    public boolean updatePreferenceLevel(Long userId, Long categoryId, BigDecimal preferenceLevel) {
        return baseMapper.updatePreferenceLevel(userId, categoryId, preferenceLevel) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchSavePreferences(List<UserCategoryPreference> preferences) {
        if (preferences == null || preferences.isEmpty()) {
            return false;
        }
        return baseMapper.batchInsert(preferences) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByUserId(Long userId) {
        return baseMapper.deleteByUserId(userId) >= 0;
    }

    @Override
    public boolean removePreference(Long userId, Long categoryId) {
        return baseMapper.deleteByUserIdAndCategoryId(userId, categoryId) > 0;
    }

    @Override
    public Map<Long, Map<String, Object>> getCategoryPreferenceStats() {
        return baseMapper.selectCategoryPreferenceStats();
    }

    @Override
    public IPage<UserCategoryPreference> getUsersByCategoryIdPage(Long categoryId, Page<UserCategoryPreference> page) {
        return baseMapper.selectUsersByCategoryIdPage(page, categoryId);
    }

    @Override
    public int countByUserId(Long userId) {
        return baseMapper.countByUserId(userId);
    }

    @Override
    public boolean incrementPreferenceLevel(Long userId, Long categoryId, BigDecimal increment) {
        return baseMapper.incrementPreferenceLevel(userId, categoryId, increment) > 0;
    }

    @Override
    public List<UserCategoryPreference> getTopPreferredCategories(Long userId, Integer limit) {
        return baseMapper.selectTopPreferredCategories(userId, limit);
    }

    @Override
    public List<UserCategoryPreference> getTopInterestedUsers(Long categoryId, Integer limit) {
        return baseMapper.selectTopInterestedUsers(categoryId, limit);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addCategoryPreference(Long userId, Long categoryId, BigDecimal preferenceLevel) {
        // 判断关联是否已存在
        LambdaQueryWrapper<UserCategoryPreference> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserCategoryPreference::getUserId, userId)
                    .eq(UserCategoryPreference::getCategoryId, categoryId);
        
        UserCategoryPreference preference = baseMapper.selectOne(queryWrapper);
        
        if (preference != null) {
            // 关联已存在，更新偏好程度
            return updatePreferenceLevel(userId, categoryId, preferenceLevel);
        } else {
            // 新建关联
            UserCategoryPreference newPreference = UserCategoryPreference.builder()
                    .userId(userId)
                    .categoryId(categoryId)
                    .preferenceLevel(preferenceLevel)
                    .build();
            
            List<UserCategoryPreference> preferences = new ArrayList<>();
            preferences.add(newPreference);
            return batchSavePreferences(preferences);
        }
    }
} 