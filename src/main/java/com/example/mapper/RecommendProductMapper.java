package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.model.entity.RecommendProduct;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 31815
 * @description 针对表【recommend_product(推荐商品表)】的数据库操作Mapper
 * @createDate 2025-02-24 12:03:53
 * @Entity model.entity.RecommendProduct
 */
public interface RecommendProductMapper extends BaseMapper<RecommendProduct> {

    /**
     * 根据商品ID查询推荐商品
     * @param productId 商品ID
     * @return 推荐商品列表
     */
    List<RecommendProduct> selectByProductId(@Param("productId") Long productId);

    /**
     * 根据用户ID查询推荐商品
     * @param userId 用户ID
     * @return 推荐商品列表
     */
    List<RecommendProduct> selectByUserId(@Param("userId") Long userId);

    /**
     * 分页查询推荐商品
     * @param page 分页信息
     * @return 推荐商品列表
     */
    IPage<RecommendProduct> selectPage(IPage<RecommendProduct> page);

    /**
     * 根据ID查询推荐商品
     * @param id 推荐商品ID
     * @return 推荐商品信息
     */
    RecommendProduct selectById(@Param("id") Long id);

    /**
     * 插入新推荐商品
     * @param recommendProduct 推荐商品信息
     * @return 插入结果
     */
    int insertRecommendProduct(RecommendProduct recommendProduct);

    /**
     * 更新推荐商品信息
     * @param recommendProduct 推荐商品信息
     * @return 更新结果
     */
    int updateRecommendProduct(RecommendProduct recommendProduct);

    /**
     * 根据ID删除推荐商品
     * @param id 推荐商品ID
     * @return 删除结果
     */
    int deleteRecommendProduct(@Param("id") Long id);
}



