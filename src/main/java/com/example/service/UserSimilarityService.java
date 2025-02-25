package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.UserSimilarity;
import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import com.example.vo.RecommendUserVO;
import com.example.vo.SimilarityPairVO;

public interface UserSimilarityService extends IService<UserSimilarity> {
    /**
     * 获取相似度最高的用户
     * @param userId 用户ID
     * @param limit 限制数量
     * @return 相似度最高的用户列表
     */
    List<UserSimilarity> getMostSimilarUsers(Long userId, Integer limit);

    /**
     * 获取两个用户之间的相似度
     * @param userIdA 用户A的ID
     * @param userIdB 用户B的ID
     * @return 相似度
     */ 
    UserSimilarity getSimilarityBetweenUsers(Long userIdA, Long userIdB);

    /**
     * 更新或插入相似度
     * @param userIdA 用户A的ID
     * @param userIdB 用户B的ID
     * @param similarity 相似度
     */
    boolean updateOrInsertSimilarity(Long userIdA, Long userIdB, BigDecimal similarity);

    /**
     * 批量插入相似度
     * @param similarityList 相似度列表
     */
    boolean batchInsertSimilarities(List<UserSimilarity> similarityList);

    /**
     * 获取相似度大于等于阈值的用户对
     * @param threshold 阈值
     * @return 相似度大于等于阈值的用户对列表
     */     
    List<UserSimilarity> getSimilarUsersByThreshold(BigDecimal threshold);

    /**
     * 删除过期的相似度
     * @param beforeTime 过期时间
     */
    int deleteOldSimilarities(LocalDateTime beforeTime);

    /**
     * 获取指定用户的所有相似度
     * @param userId 用户ID
     * @return 指定用户的所有相似度列表
     */     
    List<UserSimilarity> getAllSimilaritiesByUserId(Long userId);

    /**
     * 计算两个用户之间的相似度
     * @param userIdA 用户A的ID
     * @param userIdB 用户B的ID
     * @return 相似度
     */
    BigDecimal calculateUserSimilarity(Long userIdA, Long userIdB);

    /**
     * 构建用户相似度矩阵
     * @param batchSize 批量大小
     * @return 构建的相似度矩阵
     */
    int buildUserSimilarityMatrix(Integer batchSize);

    /**
     * 获取推荐用户列表
     * @param userId 用户ID
     * @param limit 限制数量
     * @return 推荐用户列表
     */
    List<RecommendUserVO> getRecommendedUsers(Long userId, Integer limit);

    /**
     * 获取高相似度用户对
     * @param threshold 阈值
     * @return 高相似度用户对列表
     */
    List<SimilarityPairVO> getHighSimilarityUserPairs(BigDecimal threshold);

    /**
     * 获取相似度分布
     * @param days 天数
     * @return 相似度分布
     */
    Map<String, Integer> getSimilarityDistribution(Integer days);
    



}
