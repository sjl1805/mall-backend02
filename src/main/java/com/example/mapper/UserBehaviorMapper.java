package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.model.dto.users.UserBehaviorDTO;
import com.example.model.dto.users.UserBehaviorPageDTO;
import com.example.model.entity.UserBehavior;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用户行为分析Mapper接口
 * 实现行为记录、用户画像分析和推荐算法支持
 * 
 * @author 毕业设计学生
 */
public interface UserBehaviorMapper extends BaseMapper<UserBehavior> {

    /**
     * 分页查询用户行为（支持多条件过滤）
     * 
     * @param page     分页参数（包含当前页、每页数量）
     * @param queryDTO 查询条件DTO（包含用户ID、商品ID等）
     * @return 分页结果（包含行为记录和分页信息）
     */
    IPage<UserBehavior> selectBehaviorPage(IPage<UserBehavior> page,
                                           @Param("query") UserBehaviorPageDTO queryDTO);

    /**
     * 计算用户行为权重（用于推荐系统）
     * 
     * @param userId 用户ID（必填）
     * @return 包含总权重、行为次数等统计结果
     */
    Map<String, Object> calculateUserWeight(@Param("userId") Long userId);

    /**
     * 获取用户最近行为（用于生成用户画像）
     * 
     * @param userId 用户ID（必填）
     * @param limit  最大返回数量（1-100）
     * @return 按时间倒序排列的行为列表
     */
    List<UserBehavior> selectRecentBehaviors(@Param("userId") Long userId,
                                             @Param("limit") Integer limit);

    /**
     * 统计行为类型分布（用于数据分析看板）
     * 
     * @param days 最近天数（最大365天）
     * @return 各行为类型的统计结果
     */
    List<Map<String, Object>> countBehaviorDistribution(@Param("days") Integer days);

    /**
     * 检查行为是否存在（防止重复记录）
     * 
     * @param userId       用户ID（必填）
     * @param productId    商品ID（必填）
     * @param behaviorType 行为类型（1-浏览 2-收藏 3-加购 4-购买）
     * @return 存在返回1，否则返回0
     */
    int checkBehaviorExists(@Param("userId") Long userId,
                            @Param("productId") Long productId,
                            @Param("behaviorType") Integer behaviorType);

    /**
     * 更新行为权重（算法训练时调用）
     * 
     * @param id     行为记录ID（必填）
     * @param weight 新的权重值（0.0-1.0）
     * @return 影响的行数
     */
    @Update("UPDATE user_behavior SET weight = #{weight} WHERE id = #{id}")
    int updateBehaviorWeight(@Param("id") Long id,
                             @Param("weight") Double weight);

    /**
     * 分析用户行为模式（用于推荐算法）
     * 
     * @param startTime   分析开始时间
     * @param endTime     分析结束时间
     * @param minDuration 最小有效时长（秒）
     * @return 商品行为模式分析结果
     */
    List<Map<String, Object>> analyzeBehaviorPattern(
            @Param("startTime") Date startTime,
            @Param("endTime") Date endTime,
            @Param("minDuration") Integer minDuration);

    /**
     * 批量插入行为记录（数据采集时使用）
     * 
     * @param behaviors 行为记录DTO列表
     * @return 成功插入的记录数
     */
    int batchInsert(@Param("behaviors") List<UserBehaviorDTO> behaviors);
}




