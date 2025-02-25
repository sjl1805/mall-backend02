package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.model.entity.UserCategoryPreference;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 用户-分类偏好关联Mapper接口
 */
@Mapper
public interface UserCategoryPreferenceMapper extends BaseMapper<UserCategoryPreference> {
    
    /**
     * 根据用户ID查询所有分类偏好
     * @param userId 用户ID
     * @return 分类偏好列表
     */
    List<UserCategoryPreference> selectByUserId(@Param("userId") Long userId);
    
    /**
     * 根据分类ID查询所有用户偏好
     * @param categoryId 分类ID
     * @return 用户偏好列表
     */
    List<UserCategoryPreference> selectByCategoryId(@Param("categoryId") Long categoryId);
    
    /**
     * 查询用户的高偏好分类(偏好程度大于指定值)
     * @param userId 用户ID
     * @param minPreference 最小偏好值
     * @return 高偏好分类列表
     */
    List<UserCategoryPreference> selectHighPreferenceCategories(@Param("userId") Long userId, @Param("minPreference") BigDecimal minPreference);
    
    /**
     * 查询分类的高偏好用户(偏好程度大于指定值)
     * @param categoryId 分类ID
     * @param minPreference 最小偏好值
     * @return 高偏好用户列表
     */
    List<UserCategoryPreference> selectHighPreferenceUsers(@Param("categoryId") Long categoryId, @Param("minPreference") BigDecimal minPreference);
    
    /**
     * 更新用户分类偏好程度
     * @param userId 用户ID
     * @param categoryId 分类ID
     * @param preferenceLevel 新偏好程度
     * @return 影响行数
     */
    int updatePreferenceLevel(@Param("userId") Long userId, @Param("categoryId") Long categoryId, @Param("preferenceLevel") BigDecimal preferenceLevel);
    
    /**
     * 批量插入用户分类偏好
     * @param list 用户分类偏好列表
     * @return 影响行数
     */
    int batchInsert(@Param("list") List<UserCategoryPreference> list);
    
    /**
     * 删除用户的所有分类偏好
     * @param userId 用户ID
     * @return 影响行数
     */
    int deleteByUserId(@Param("userId") Long userId);
    
    /**
     * 删除特定的用户分类偏好
     * @param userId 用户ID
     * @param categoryId 分类ID
     * @return 影响行数
     */
    int deleteByUserIdAndCategoryId(@Param("userId") Long userId, @Param("categoryId") Long categoryId);
    
    /**
     * 查询分类的用户偏好统计
     * @return 分类偏好统计列表，包含categoryId、userCount（用户数量）、avgPreference（平均偏好）
     */
    @MapKey("categoryId")
    Map<Long, Map<String, Object>> selectCategoryPreferenceStats();
    
    /**
     * 分页查询某分类的用户偏好
     * @param page 分页参数
     * @param categoryId 分类ID
     * @return 分页数据
     */
    IPage<UserCategoryPreference> selectUsersByCategoryIdPage(Page<UserCategoryPreference> page, @Param("categoryId") Long categoryId);
    
    /**
     * 查询用户的分类偏好数量
     * @param userId 用户ID
     * @return 偏好数量
     */
    int countByUserId(@Param("userId") Long userId);
    
    /**
     * 增加分类偏好程度
     * @param userId 用户ID
     * @param categoryId 分类ID
     * @param increment 增量值
     * @return 影响行数
     */
    int incrementPreferenceLevel(@Param("userId") Long userId, @Param("categoryId") Long categoryId, @Param("increment") BigDecimal increment);
    
    /**
     * 查询用户最喜欢的前N个分类
     * @param userId 用户ID
     * @param limit 返回数量限制
     * @return 最喜欢的分类列表
     */
    List<UserCategoryPreference> selectTopPreferredCategories(@Param("userId") Long userId, @Param("limit") Integer limit);
    
    /**
     * 查询分类最受欢迎的前N个用户
     * @param categoryId 分类ID
     * @param limit 返回数量限制
     * @return 最喜欢该分类的用户列表
     */
    List<UserCategoryPreference> selectTopInterestedUsers(@Param("categoryId") Long categoryId, @Param("limit") Integer limit);
} 