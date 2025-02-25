package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.model.entity.UserBehavior;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 用户行为与交互Mapper接口
 */
@Mapper
public interface UserBehaviorMapper extends BaseMapper<UserBehavior> {
    
    /**
     * 根据用户ID查询行为记录
     * @param userId 用户ID
     * @param limit 返回数量限制
     * @return 行为记录列表
     */
    List<UserBehavior> selectByUserId(@Param("userId") Long userId, @Param("limit") Integer limit);
    
    /**
     * 根据商品ID查询行为记录
     * @param productId 商品ID
     * @param limit 返回数量限制
     * @return 行为记录列表
     */
    List<UserBehavior> selectByProductId(@Param("productId") Long productId, @Param("limit") Integer limit);
    
    /**
     * 根据行为类型查询记录
     * @param behaviorType 行为类型
     * @param limit 返回数量限制
     * @return 行为记录列表
     */
    List<UserBehavior> selectByBehaviorType(@Param("behaviorType") Integer behaviorType, @Param("limit") Integer limit);
    
    /**
     * 查询用户对特定商品的行为记录
     * @param userId 用户ID
     * @param productId 商品ID
     * @return 行为记录列表
     */
    List<UserBehavior> selectByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);
    
    /**
     * 查询用户在特定分类下的行为记录
     * @param userId 用户ID
     * @param categoryId 分类ID
     * @param limit 返回数量限制
     * @return 行为记录列表
     */
    List<UserBehavior> selectByUserIdAndCategoryId(@Param("userId") Long userId, @Param("categoryId") Long categoryId, @Param("limit") Integer limit);
    
    /**
     * 查询热门搜索关键词
     * @param limit 返回数量限制
     * @return 热门关键词列表，包含keyword、searchCount
     */
    @MapKey("keyword")
    List<Map<String, Object>> selectHotSearchKeywords(@Param("limit") Integer limit);
    
    /**
     * 查询商品的平均评分
     * @param productId 商品ID
     * @return 平均评分
     */
    BigDecimal selectAvgRatingByProductId(@Param("productId") Long productId);
    
    /**
     * 查询商品的评价数量
     * @param productId 商品ID
     * @return 评价数量
     */
    Integer countReviewsByProductId(@Param("productId") Long productId);
    
    /**
     * 查询某分类下的热门商品（基于用户行为）
     * @param categoryId 分类ID
     * @param limit 返回数量限制
     * @return 热门商品列表，包含productId、viewCount、cartCount、buyCount
     */
    @MapKey("productId")
    List<Map<String, Object>> selectHotProductsByCategory(@Param("categoryId") Long categoryId, @Param("limit") Integer limit);
    
    /**
     * 查询指定时间段内的用户行为统计
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 行为统计，按行为类型分组
     */
    @MapKey("behaviorType")
    Map<Integer, Map<String, Object>> selectBehaviorStatsByDateRange(
        @Param("startTime") LocalDateTime startTime, 
        @Param("endTime") LocalDateTime endTime
    );
    
    /**
     * 更新用户行为记录（如增加浏览次数）
     * @param userId 用户ID
     * @param productId 商品ID
     * @param behaviorType 行为类型
     * @return 影响行数
     */
    int updateBehaviorCount(@Param("userId") Long userId, @Param("productId") Long productId, @Param("behaviorType") Integer behaviorType);
    
    /**
     * 分页查询用户评价
     * @param page 分页参数
     * @param productId 商品ID
     * @return 分页数据
     */
    IPage<UserBehavior> selectReviewsByProductIdPage(Page<UserBehavior> page, @Param("productId") Long productId);
    
    /**
     * 查询用户最近浏览的商品
     * @param userId 用户ID
     * @param limit 返回数量限制
     * @return 最近浏览的商品ID列表
     */
    List<Long> selectRecentViewedProducts(@Param("userId") Long userId, @Param("limit") Integer limit);
    
    /**
     * 查询用户收藏的商品
     * @param userId 用户ID
     * @param limit 返回数量限制
     * @return 收藏的商品ID列表
     */
    List<Long> selectFavoriteProducts(@Param("userId") Long userId, @Param("limit") Integer limit);
    
    /**
     * 统计商品的收藏人数
     * @param productId 商品ID
     * @return 收藏人数
     */
    Integer countFavoriteUsers(@Param("productId") Long productId);
    
    /**
     * 查询用户的搜索历史
     * @param userId 用户ID
     * @param limit 返回数量限制
     * @return 搜索关键词列表
     */
    List<String> selectUserSearchHistory(@Param("userId") Long userId, @Param("limit") Integer limit);
} 