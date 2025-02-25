package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.RecommendProduct;

import java.util.List;

/**
 * 推荐商品服务接口
 * <p>
 * 定义了推荐商品相关的业务功能，包括基础的CRUD操作和高级推荐功能
 * 推荐系统是电商平台提升用户体验和商品转化率的关键组件
 *
 * @author 31815
 * @description 针对表【recommend_product(推荐商品表)】的数据库操作Service
 * @createDate 2025-02-24 12:03:53
 */
public interface RecommendProductService extends IService<RecommendProduct> {

    /**
     * 根据用户ID查询推荐商品
     *
     * @param userId 用户ID
     * @return 推荐商品列表
     */
    List<RecommendProduct> selectByUserId(Long userId);

    /**
     * 分页查询推荐商品
     *
     * @param page 分页信息
     * @return 推荐商品列表
     */
    IPage<RecommendProduct> selectPage(IPage<RecommendProduct> page);

    /**
     * 根据ID查询推荐商品
     *
     * @param id 推荐商品ID
     * @return 推荐商品信息
     */
    RecommendProduct selectById(Long id);

    /**
     * 新增推荐商品
     *
     * @param recommendProduct 推荐商品信息
     * @return 插入结果
     */
    boolean insertRecommendProduct(RecommendProduct recommendProduct);

    /**
     * 更新推荐商品信息
     *
     * @param recommendProduct 推荐商品信息
     * @return 更新结果
     */
    boolean updateRecommendProduct(RecommendProduct recommendProduct);

    /**
     * 根据ID删除推荐商品
     *
     * @param id 推荐商品ID
     * @return 删除结果
     */
    boolean deleteRecommendProduct(Long id);

    /**
     * 根据商品ID查询推荐商品
     *
     * @param productId 商品ID
     * @return 推荐商品列表
     */
    List<RecommendProduct> selectByProductId(Long productId);

    /**
     * 查询当前有效的推荐商品
     *
     * @param type  推荐类型（可选）
     * @param limit 限制数量
     * @return 有效的推荐商品列表
     */
    List<RecommendProduct> selectActiveRecommends(Integer type, Integer limit);

    /**
     * 按类型查询推荐商品
     *
     * @param type 推荐类型
     * @return 推荐商品列表
     */
    List<RecommendProduct> selectByType(Integer type);

    /**
     * 更新推荐商品状态
     *
     * @param id     推荐商品ID
     * @param status 新状态
     * @return 更新结果
     */
    boolean updateStatus(Long id, Integer status);

    /**
     * 生成基于用户行为的个性化推荐
     * 分析用户行为数据，生成个性化商品推荐
     *
     * @param userId 用户ID
     * @param limit  推荐数量
     * @return 推荐商品列表
     */
    List<RecommendProduct> generatePersonalizedRecommends(Long userId, Integer limit);

    /**
     * 生成相似商品推荐
     * 基于当前商品特性，推荐相似商品
     *
     * @param productId 商品ID
     * @param limit     推荐数量
     * @return 相似商品推荐列表
     */
    List<RecommendProduct> generateSimilarProductRecommends(Long productId, Integer limit);

    /**
     * 生成"猜你喜欢"推荐
     * 根据用户历史行为，推荐猜测用户喜欢的商品
     *
     * @param userId 用户ID
     * @param limit  推荐数量
     * @return 推荐商品列表
     */
    List<RecommendProduct> generateYouMayLikeRecommends(Long userId, Integer limit);

    /**
     * 生成"购买此商品的人还购买了"推荐
     * 基于协同过滤算法的相关商品推荐
     *
     * @param productId 当前商品ID
     * @param limit     推荐数量
     * @return 推荐商品列表
     */
    List<RecommendProduct> generateAlsoBoughtRecommends(Long productId, Integer limit);

    /**
     * 批量更新推荐商品状态
     *
     * @param ids    推荐商品ID列表
     * @param status 新状态
     * @return 更新结果
     */
    boolean batchUpdateStatus(List<Long> ids, Integer status);
}
