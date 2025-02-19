package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.dto.users.UserBehaviorDTO;
import com.example.model.entity.UserBehavior;

import java.util.List;
import java.util.Map;

/**
 * 用户行为服务接口
 * 
 * @author 31815
 * @description 提供用户行为管理功能，包含：
 *              1. 行为记录与统计
 *              2. 用户权重计算
 *              3. 行为分析接口
 * @createDate 2025-02-18 23:43:52
 */
public interface UserBehaviorService extends IService<UserBehavior> {

    /**
     * 记录用户行为（带重复校验）
     * @param behaviorDTO 行为信息，包含：
     *                    - userId: 用户ID（必须）
     *                    - productId: 商品ID（必须）
     *                    - behaviorType: 行为类型（0-浏览，1-收藏，2-加购）
     * @return 操作是否成功
     * @throws com.example.exception.BusinessException 当重复行为时抛出
     */
    boolean recordBehavior(UserBehaviorDTO behaviorDTO);

    /**
     * 获取最近行为（带缓存）
     * @param userId 用户ID
     * @param limit 最大数量
     * @return 行为列表（按时间倒序）
     * @implNote 结果缓存优化，有效期15分钟
     */
    List<UserBehavior> getRecentBehaviors(Long userId, Integer limit);

    /**
     * 计算用户权重（带缓存）
     * @param userId 用户ID
     * @return 权重计算结果（包含活跃度、偏好等）
     * @implNote 结果缓存优化，有效期1小时
     */
    Map<String, Object> getUserWeight(Long userId);

    /**
     * 获取行为分布统计（带缓存）
     * @param days 统计天数（最大30天）
     * @return 按行为类型分类的统计结果
     * @implNote 结果缓存优化，有效期2小时
     */
    List<Map<String, Object>> getBehaviorDistribution(Integer days);

    /**
     * 批量记录行为（事务操作）
     * @param behaviors 行为列表
     * @return 操作是否成功
     * @throws com.example.exception.BusinessException 当存在重复行为时抛出
     */
    boolean batchRecordBehaviors(List<UserBehaviorDTO> behaviors);
    // void analyzeBehaviors();
}
