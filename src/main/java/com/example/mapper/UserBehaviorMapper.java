package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.entity.UserBehavior;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

/**
 * @author 31815
 * @description 针对表【user_behavior(用户行为记录表)】的数据库操作Mapper
 * @createDate 2025-02-24 12:03:47
 * @Entity model.entity.UserBehavior
 */
public interface UserBehaviorMapper extends BaseMapper<UserBehavior> {

    /**
     * 插入或更新用户行为（基于用户+商品+行为类型唯一）
     * @param userId 用户ID
     * @param productId 商品ID
     * @param behaviorType 行为类型
     * @param duration 持续时间（秒）
     * @param weight 权重值
     * @return 影响行数
     */
    int insertOrUpdateBehavior(@Param("userId") Long userId,
                              @Param("productId") Long productId,
                              @Param("behaviorType") Integer behaviorType,
                              @Param("duration") Integer duration,
                              @Param("weight") Double weight);
    
    /**
     * 获取用户偏好商品（最近30天）
     * @param userId 用户ID
     * @param limit 返回数量
     * @return 包含商品ID和权重的列表
     */
    List<Map<String, Object>> selectUserPreferences(@Param("userId") Long userId,
                                                   @Param("limit") int limit);
    
    /**
     * 获取热门商品（最近7天）
     * @param limit 返回数量
     * @return 包含商品ID和权重的列表
     */
    List<Map<String, Object>> selectHotProducts(@Param("limit") int limit);
    
    /**
     * 统计用户行为数据
     * @param userId 用户ID
     * @return 按行为类型分组的统计数据
     */
    List<Map<String, Object>> selectBehaviorStats(@Param("userId") Long userId);
    
    /**
     * 清理过期行为记录（90天前）
     * @return 删除行数
     */
    int cleanExpiredBehaviors();
}




