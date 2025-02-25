package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.UserCategoryPreference;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 用户-分类偏好关联服务接口
 */
public interface UserCategoryPreferenceService extends IService<UserCategoryPreference> {
    
    /**
     * 根据用户ID查询所有分类偏好
     * @param userId 用户ID
     * @return 分类偏好列表
     */
    List<UserCategoryPreference> getByUserId(Long userId);
    
    /**
     * 根据分类ID查询所有用户偏好
     * @param categoryId 分类ID
     * @return 用户偏好列表
     */
    List<UserCategoryPreference> getByCategoryId(Long categoryId);
    
    /**
     * 查询用户的高偏好分类
     * @param userId 用户ID
     * @param minPreference 最小偏好值
     * @return 高偏好分类列表
     */
    List<UserCategoryPreference> getHighPreferenceCategories(Long userId, BigDecimal minPreference);
    
    /**
     * 查询分类的高偏好用户
     * @param categoryId 分类ID
     * @param minPreference 最小偏好值
     * @return 高偏好用户列表
     */
    List<UserCategoryPreference> getHighPreferenceUsers(Long categoryId, BigDecimal minPreference);
    
    /**
     * 更新用户分类偏好程度
     * @param userId 用户ID
     * @param categoryId 分类ID
     * @param preferenceLevel 新偏好程度
     * @return 是否更新成功
     */
    boolean updatePreferenceLevel(Long userId, Long categoryId, BigDecimal preferenceLevel);
    
    /**
     * 批量保存用户分类偏好
     * @param preferences 偏好列表
     * @return 是否保存成功
     */
    boolean batchSavePreferences(List<UserCategoryPreference> preferences);
    
    /**
     * 删除用户的所有分类偏好
     * @param userId 用户ID
     * @return 是否删除成功
     */
    boolean removeByUserId(Long userId);
    
    /**
     * 删除特定的用户分类偏好
     * @param userId 用户ID
     * @param categoryId 分类ID
     * @return 是否删除成功
     */
    boolean removePreference(Long userId, Long categoryId);
    
    /**
     * 查询分类的用户偏好统计
     * @return 分类偏好统计信息
     */
    Map<Long, Map<String, Object>> getCategoryPreferenceStats();
    
    /**
     * 分页查询某分类的用户偏好
     * @param categoryId 分类ID
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<UserCategoryPreference> getUsersByCategoryIdPage(Long categoryId, Page<UserCategoryPreference> page);
    
    /**
     * 查询用户的分类偏好数量
     * @param userId 用户ID
     * @return 偏好数量
     */
    int countByUserId(Long userId);
    
    /**
     * 增加分类偏好程度
     * @param userId 用户ID
     * @param categoryId 分类ID
     * @param increment 增加的偏好值
     * @return 是否增加成功
     */
    boolean incrementPreferenceLevel(Long userId, Long categoryId, BigDecimal increment);
    
    /**
     * 查询用户最喜欢的前N个分类
     * @param userId 用户ID
     * @param limit 返回数量限制
     * @return 最喜欢的分类列表
     */
    List<UserCategoryPreference> getTopPreferredCategories(Long userId, Integer limit);
    
    /**
     * 查询分类最受欢迎的前N个用户
     * @param categoryId 分类ID
     * @param limit 返回数量限制
     * @return 最喜欢该分类的用户列表
     */
    List<UserCategoryPreference> getTopInterestedUsers(Long categoryId, Integer limit);
    
    /**
     * 为用户添加分类偏好
     * @param userId 用户ID
     * @param categoryId 分类ID
     * @param preferenceLevel 偏好程度
     * @return 是否添加成功
     */
    boolean addCategoryPreference(Long userId, Long categoryId, BigDecimal preferenceLevel);
} 