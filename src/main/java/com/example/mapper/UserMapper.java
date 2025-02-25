package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.model.entity.User;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 用户Mapper接口
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户对象
     */
    User selectByUsername(String username);

    /**
     * 根据手机号查询用户
     *
     * @param phone 手机号
     * @return 用户对象
     */
    User selectByPhone(String phone);

    /**
     * 根据邮箱查询用户
     *
     * @param email 邮箱
     * @return 用户对象
     */
    User selectByEmail(String email);

    /**
     * 分页查询用户列表（支持多条件筛选）
     *
     * @param page 分页对象
     * @param params 查询参数
     * @return 分页用户列表
     */
    IPage<User> selectUserPage(Page<User> page, @Param("params") Map<String, Object> params);

    /**
     * 更新用户最后活跃时间
     *
     * @param userId 用户ID
     * @return 影响行数
     */
    int updateLastActiveTime(Long userId);

    /**
     * 更新用户状态
     *
     * @param userId 用户ID
     * @param status 状态值
     * @return 影响行数
     */
    int updateStatus(@Param("userId") Long userId, @Param("status") Integer status);

    /**
     * 更新用户密码
     *
     * @param userId 用户ID
     * @param newPassword 新密码（已加密）
     * @return 影响行数
     */
    int updatePassword(@Param("userId") Long userId, @Param("newPassword") String newPassword);

    /**
     * 批量查询用户
     *
     * @param userIds 用户ID列表
     * @return 用户列表
     */
    List<User> selectBatchByIds(@Param("userIds") List<Long> userIds);

    /**
     * 更新用户标签
     *
     * @param userId 用户ID
     * @param tags 标签JSON
     * @return 影响行数
     */
    int updateUserTags(@Param("userId") Long userId, @Param("tags") String tags);

    /**
     * 更新用户偏好分类
     *
     * @param userId 用户ID
     * @param preferredCategories 偏好分类JSON
     * @return 影响行数
     */
    int updateUserPreferredCategories(@Param("userId") Long userId, @Param("preferredCategories") String preferredCategories);

    /**
     * 统计不同性别的用户数量
     *
     * @return 统计结果
     */
    @MapKey("gender")
    List<Map<String, Object>> countUserByGender();

    /**
     * 统计不同年龄段的用户数量
     *
     * @return 统计结果
     */
    @MapKey("ageRange")
    List<Map<String, Object>> countUserByAgeRange();

    /**
     * 统计不同消费能力的用户数量
     *
     * @return 统计结果
     */
    @MapKey("consumptionLevel")
    List<Map<String, Object>> countUserByConsumptionLevel();

    /**
     * 统计用户近期增长数据
     *
     * @param days 天数
     * @return 统计结果
     */
    @MapKey("date")
    List<Map<String, Object>> countUserGrowth(@Param("days") Integer days);

    /**
     * 查询活跃用户列表
     *
     * @param days 活跃天数
     * @param limit 限制数量
     * @return 活跃用户列表
     */
    List<User> selectActiveUsers(@Param("days") Integer days, @Param("limit") Integer limit);

    /**
     * 获取指定天数内活跃的用户ID列表
     * 
     * @param days 活跃天数
     * @return 活跃用户ID列表
     */
    List<Long> selectActiveUserIds(Integer days);
} 