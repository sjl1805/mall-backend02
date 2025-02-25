package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.entity.ProductSimilarity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品相似度Mapper接口
 * 用于商品协同过滤推荐系统
 */
@Mapper
public interface ProductSimilarityMapper extends BaseMapper<ProductSimilarity> {
    
    /**
     * 获取与指定商品相似度最高的N个商品
     * @param productId 商品ID
     * @param limit 返回数量限制
     * @return 相似商品列表
     */
    List<ProductSimilarity> selectMostSimilarProducts(@Param("productId") Long productId, @Param("limit") Integer limit);
    
    /**
     * 查询两个商品之间的相似度
     * @param productIdA 商品A的ID
     * @param productIdB 商品B的ID
     * @return 相似度记录，不存在则返回null
     */
    ProductSimilarity selectByProductIds(@Param("productIdA") Long productIdA, @Param("productIdB") Long productIdB);
    
    /**
     * 更新或插入商品相似度记录
     * @param productIdA 商品A的ID
     * @param productIdB 商品B的ID
     * @param similarity 相似度分数
     * @param updateTime 更新时间
     * @return 影响行数
     */
    int insertOrUpdate(@Param("productIdA") Long productIdA, 
                       @Param("productIdB") Long productIdB, 
                       @Param("similarity") BigDecimal similarity, 
                       @Param("updateTime") LocalDateTime updateTime);
    
    /**
     * 批量插入商品相似度数据
     * @param similarityList 相似度列表
     * @return 影响行数
     */
    int batchInsert(@Param("list") List<ProductSimilarity> similarityList);
    
    /**
     * 根据相似度阈值查询高相似度商品对
     * @param threshold 相似度阈值
     * @return 高相似度商品对列表
     */
    List<ProductSimilarity> selectByThreshold(@Param("threshold") BigDecimal threshold);
    
    /**
     * 删除指定日期之前的相似度数据
     * @param beforeTime 截止时间
     * @return 影响行数
     */
    int deleteBeforeTime(@Param("beforeTime") LocalDateTime beforeTime);
    
    /**
     * 查询指定商品的所有相似度记录
     * @param productId 商品ID
     * @return 相似度记录列表
     */
    List<ProductSimilarity> selectAllByProductId(@Param("productId") Long productId);
    
    /**
     * 获取相似度最高的N个商品对
     * @param limit 返回数量限制
     * @return 相似度最高的商品对列表
     */
    List<ProductSimilarity> selectTopSimilarityPairs(@Param("limit") Integer limit);
    
    /**
     * 查询特定分类下的相似商品
     * @param productId 商品ID
     * @param categoryId 分类ID
     * @param limit 返回数量限制
     * @return 同分类下的相似商品
     */
    List<ProductSimilarity> selectSimilarProductsByCategory(
        @Param("productId") Long productId, 
        @Param("categoryId") Long categoryId, 
        @Param("limit") Integer limit
    );
} 