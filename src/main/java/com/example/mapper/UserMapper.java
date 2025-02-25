package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.model.entity.User;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 用户表 Mapper 接口
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    User selectByUsername(@Param("username") String username);

    /**
     * 根据手机号查询用户信息
     *
     * @param phone 手机号
     * @return 用户信息
     */
    User selectByPhone(@Param("phone") String phone);

    /**
     * 根据邮箱查询用户信息
     *
     * @param email 邮箱
     * @return 用户信息
     */
    User selectByEmail(@Param("email") String email);

    /**
     * 更新用户最后活跃时间
     *
     * @param userId 用户ID
     * @param lastActiveTime 最后活跃时间
     * @return 影响行数
     */
    int updateLastActiveTime(@Param("userId") Long userId, @Param("lastActiveTime") LocalDateTime lastActiveTime);

    /**
     * 更新用户状态
     *
     * @param userId 用户ID
     * @param status 状态：0禁用 1启用
     * @return 影响行数
     */
    int updateStatus(@Param("userId") Long userId, @Param("status") Integer status);

    /**
     * 更新用户密码
     *
     * @param userId 用户ID
     * @param password 新密码(已加密)
     * @return 影响行数
     */
    int updatePassword(@Param("userId") Long userId, @Param("password") String password);

    /**
     * 更新用户偏好分类
     *
     * @param userId 用户ID
     * @param preferredCategories 偏好分类JSON
     * @return 影响行数
     */
    int updatePreferredCategories(@Param("userId") Long userId, @Param("preferredCategories") String preferredCategories);

    /**
     * 更新用户标签
     *
     * @param userId 用户ID
     * @param tags 用户标签JSON
     * @return 影响行数
     */
    int updateTags(@Param("userId") Long userId, @Param("tags") String tags);

    /**
     * 分页查询用户列表
     *
     * @param page 分页参数
     * @param params 查询参数
     * @return 用户列表
     */
    IPage<User> selectUserPage(Page<User> page, @Param("params") Map<String, Object> params);

    /**
     * 查询用户数量统计(按消费能力分组)
     *
     * @return 统计结果
     */
    @MapKey("consumptionLevel")
    List<Map<String, Object>> selectUserCountByConsumptionLevel();

    /**
     * 查询用户数量统计(按活跃度分组)
     *
     * @return 统计结果
     */
    @MapKey("activityLevel")
    List<Map<String, Object>> selectUserCountByActivityLevel();

    /**
     * 查询用户数量统计(按注册时间分组)
     *
     * @param days 最近天数
     * @return 统计结果
     */
    @MapKey("registerDate")
    List<Map<String, Object>> selectUserCountByRegisterDate(@Param("days") Integer days);

    /**
     * 批量更新用户活跃度
     *
     * @param userActivityLevels 用户ID和活跃度映射
     * @return 影响行数
     */
    int batchUpdateActivityLevel(@Param("list") List<Map<String, Object>> userActivityLevels);

    /**
     * 批量更新用户消费能力
     *
     * @param userConsumptionLevels 用户ID和消费能力映射
     * @return 影响行数
     */
    int batchUpdateConsumptionLevel(@Param("list") List<Map<String, Object>> userConsumptionLevels);
} 