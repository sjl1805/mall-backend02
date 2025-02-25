package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.ProductReviewMapper;
import com.example.model.entity.ProductReview;
import com.example.service.ProductReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商品评价服务实现类
 * 
 * 该类实现了商品评价相关的业务逻辑，包括评价的添加、修改、删除和查询等功能。
 * 商品评价是电商系统中重要的用户反馈机制，对提高用户信任感和购买决策有重要影响。
 * 评价数据同时也是商品质量监控和销售分析的重要数据来源。
 * 使用了Spring缓存机制对评价信息进行缓存，提高查询效率。
 * 使用MyBatis-Plus的ServiceImpl简化数据访问操作。
 *
 * @author 31815
 * @description 针对表【product_review(商品评价表)】的数据库操作Service实现
 * @createDate 2025-02-24 12:04:04
 */
@Service
@CacheConfig(cacheNames = "productReviews") // 指定该服务类的缓存名称为"productReviews"
public class ProductReviewServiceImpl extends ServiceImpl<ProductReviewMapper, ProductReview>
        implements ProductReviewService {

    @Autowired
    private ProductReviewMapper productReviewMapper;

    /**
     * 根据商品ID查询评价列表
     * 
     * 该方法从缓存或数据库获取指定商品的所有评价信息，
     * 用于商品详情页展示用户评价，帮助潜在买家做出购买决策
     *
     * @param productId 商品ID
     * @return 商品评价列表
     */
    @Override
    @Cacheable(value = "productReviews", key = "#productId") // 缓存商品评价信息，提高查询效率
    public List<ProductReview> selectByProductId(Long productId) {
        return productReviewMapper.selectByProductId(productId);
    }

    /**
     * 分页查询商品评价数据
     * 
     * 该方法用于前台分页展示评价和后台管理系统审核评价，
     * 支持按评分、时间等条件排序
     *
     * @param page 分页参数
     * @return 商品评价分页数据
     */
    @Override
    public IPage<ProductReview> selectPage(IPage<ProductReview> page) {
        return productReviewMapper.selectPage(page);
    }

    /**
     * 根据ID查询商品评价
     *
     * @param id 评价ID
     * @return 商品评价实体
     */
    @Override
    @Cacheable(value = "productReviews", key = "#id") // 缓存评价详情，提高查询效率
    public ProductReview selectById(Long id) {
        return productReviewMapper.selectById(id);
    }

    /**
     * 添加商品评价
     * 
     * 该方法用于用户在订单完成后提交商品评价，
     * 包括评分、评价内容、图片等信息，
     * 添加后可能需要触发商品评分的重新计算
     *
     * @param productReview 商品评价实体
     * @return 添加成功返回true，失败返回false
     */
    @Override
    @Transactional
    @CacheEvict(value = "productReviews", key = "#productReview.id") // 清除评价缓存
    public boolean insertProductReview(ProductReview productReview) {
        return productReviewMapper.insert(productReview) > 0;
    }

    /**
     * 更新商品评价
     * 
     * 该方法用于用户修改评价或后台管理系统审核评价，
     * 如修改评价内容、状态（显示/隐藏）等，
     * 并清除相关缓存，确保数据一致性
     *
     * @param productReview 商品评价实体
     * @return 更新成功返回true，失败返回false
     */
    @Override
    @Transactional
    @CacheEvict(value = "productReviews", key = "#productReview.id") // 清除评价缓存
    public boolean updateProductReview(ProductReview productReview) {
        return productReviewMapper.updateById(productReview) > 0;
    }

    /**
     * 删除商品评价
     * 
     * 该方法用于用户删除自己的评价或后台管理系统删除违规评价，
     * 删除后可能需要触发商品评分的重新计算，
     * 并清除相关缓存，确保数据一致性
     *
     * @param id 评价ID
     * @return 删除成功返回true，失败返回false
     */
    @Override
    @Transactional
    @CacheEvict(value = "productReviews", key = "#id") // 清除被删除评价的缓存
    public boolean deleteProductReview(Long id) {
        return productReviewMapper.deleteById(id) > 0;
    }
}




