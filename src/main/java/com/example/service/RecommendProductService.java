package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.RecommendProduct;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * @author 31815
 * @description 针对表【recommend_product(推荐商品表)】的数据库操作Service
 * @createDate 2025-02-24 12:03:53
 */
public interface RecommendProductService extends IService<RecommendProduct> {

    /**
     * 根据用户ID查询推荐商品
     * @param userId 用户ID
     * @return 推荐商品列表
     */
    List<RecommendProduct> selectByUserId(Long userId);

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
    RecommendProduct selectById(Long id);

    /**
     * 新增推荐商品
     * @param recommendProduct 推荐商品信息
     * @return 插入结果
     */
    boolean insertRecommendProduct(RecommendProduct recommendProduct);

    /**
     * 更新推荐商品信息
     * @param recommendProduct 推荐商品信息
     * @return 更新结果
     */
    boolean updateRecommendProduct(RecommendProduct recommendProduct);

    /**
     * 根据ID删除推荐商品
     * @param id 推荐商品ID
     * @return 删除结果
     */
    boolean deleteRecommendProduct(Long id);
}
