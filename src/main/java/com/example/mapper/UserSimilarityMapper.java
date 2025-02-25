package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.entity.UserSimilarity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户相似度Mapper接口
 * 用于协同过滤推荐系统
 */
@Mapper
public interface UserSimilarityMapper extends BaseMapper<UserSimilarity> {
    
    /**
     * 获取与指定用户相似度最高的N个用户
     * @param userId 用户ID
     * @param limit 返回记录数量限制
     * @return 相似用户列表
     */
    List<UserSimilarity> selectMostSimilarUsers(@Param("userId") Long userId, @Param("limit") Integer limit);
    
    /**
     * 查询两个用户之间的相似度
     * @param userIdA 用户A的ID
     * @param userIdB 用户B的ID
     * @return 相似度记录，不存在则返回null
     */
    UserSimilarity selectByUserIds(@Param("userIdA") Long userIdA, @Param("userIdB") Long userIdB);
    
    /**
     * 更新或插入用户相似度记录
     * @param userIdA 用户A的ID
     * @param userIdB 用户B的ID
     * @param similarity 相似度分数
     * @param updateTime 更新时间
     * @return 影响行数
     */
    int insertOrUpdate(@Param("userIdA") Long userIdA, 
                       @Param("userIdB") Long userIdB, 
                       @Param("similarity") BigDecimal similarity, 
                       @Param("updateTime") LocalDateTime updateTime);
    
    /**
     * 批量插入用户相似度数据
     * @param similarityList 相似度列表
     * @return 影响行数
     */
    int batchInsert(@Param("list") List<UserSimilarity> similarityList);
    
    /**
     * 根据相似度阈值查询高相似度用户对
     * @param threshold 相似度阈值
     * @return 高相似度用户对列表
     */
    List<UserSimilarity> selectByThreshold(@Param("threshold") BigDecimal threshold);
    
    /**
     * 删除指定日期之前的相似度数据
     * @param beforeTime 截止时间
     * @return 影响行数
     */
    int deleteBeforeTime(@Param("beforeTime") LocalDateTime beforeTime);
    
    /**
     * 查询指定用户的所有相似度记录
     * @param userId 用户ID
     * @return 相似度记录列表
     */
    List<UserSimilarity> selectAllByUserId(@Param("userId") Long userId);
} 