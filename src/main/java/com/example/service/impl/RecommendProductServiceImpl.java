package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.RecommendProductMapper;
import com.example.model.entity.RecommendProduct;
import com.example.service.RecommendProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 推荐商品服务实现类
 * 
 * 该类实现了商品推荐相关的业务逻辑，包括热门商品推荐、新品推荐、个性化推荐等功能。
 * 商品推荐是提高用户购物体验和促进销售转化的重要功能。
 * 使用了Spring缓存机制对推荐商品信息进行缓存，提高查询效率。
 * 使用MyBatis-Plus的ServiceImpl简化数据访问操作。
 *
 * @author 31815
 * @description 针对表【recommend_product(推荐商品表)】的数据库操作Service实现
 * @createDate 2025-02-24 12:03:53
 */
@Service
@CacheConfig(cacheNames = "recommendProducts") // 指定该服务类的缓存名称为"recommendProducts"
public class RecommendProductServiceImpl extends ServiceImpl<RecommendProductMapper, RecommendProduct>
        implements RecommendProductService {

    @Autowired
    private RecommendProductMapper recommendProductMapper;

    /**
     * 根据用户ID查询推荐商品列表
     * 
     * 该方法从缓存或数据库获取针对特定用户的个性化推荐商品，
     * 通常基于用户的浏览历史、购买记录和偏好等数据进行推荐
     *
     * @param userId 用户ID
     * @return 推荐商品列表
     */
    @Override
    @Cacheable(value = "recommendProducts", key = "#userId") // 缓存用户的推荐商品，提高查询效率
    public List<RecommendProduct> selectByUserId(Long userId) {
        return recommendProductMapper.selectByUserId(userId);
    }

    /**
     * 分页查询推荐商品数据
     * 
     * 该方法用于后台管理系统分页查看所有推荐商品数据
     *
     * @param page 分页参数
     * @return 推荐商品分页数据
     */
    @Override
    public IPage<RecommendProduct> selectPage(IPage<RecommendProduct> page) {
        return recommendProductMapper.selectPage(page);
    }

    /**
     * 根据ID查询推荐商品
     *
     * @param id 推荐商品ID
     * @return 推荐商品实体
     */
    @Override
    public RecommendProduct selectById(Long id) {
        return recommendProductMapper.selectById(id);
    }

    /**
     * 添加推荐商品
     * 
     * 该方法用于后台管理系统添加新的推荐商品，
     * 可以设置推荐类型（热门、新品、算法生成等）、生效时间和排序等信息，
     * 并清除相关缓存，确保数据一致性
     *
     * @param recommendProduct 推荐商品实体
     * @return 添加成功返回true，失败返回false
     */
    @Override
    @Transactional
    @CacheEvict(value = "recommendProducts", key = "#recommendProduct.id") // 清除推荐商品缓存
    public boolean insertRecommendProduct(RecommendProduct recommendProduct) {
        return recommendProductMapper.insert(recommendProduct) > 0;
    }

    /**
     * 更新推荐商品
     * 
     * 该方法用于后台管理系统更新推荐商品信息，
     * 如修改排序、推荐理由、生效时间等，
     * 并清除相关缓存，确保数据一致性
     *
     * @param recommendProduct 推荐商品实体
     * @return 更新成功返回true，失败返回false
     */
    @Override
    @Transactional
    @CacheEvict(value = "recommendProducts", key = "#recommendProduct.id") // 清除推荐商品缓存
    public boolean updateRecommendProduct(RecommendProduct recommendProduct) {
        return recommendProductMapper.updateById(recommendProduct) > 0;
    }

    /**
     * 删除推荐商品
     * 
     * 该方法用于后台管理系统删除不需要的推荐商品，
     * 并清除相关缓存，确保数据一致性
     *
     * @param id 推荐商品ID
     * @return 删除成功返回true，失败返回false
     */
    @Override
    @Transactional
    @CacheEvict(value = "recommendProducts", key = "#id") // 清除被删除推荐商品的缓存
    public boolean deleteRecommendProduct(Long id) {
        return recommendProductMapper.deleteById(id) > 0;
    }
}




