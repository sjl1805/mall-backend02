package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.ProductsMapper;
import com.example.mapper.RecommendProductMapper;
import com.example.model.entity.Products;
import com.example.model.entity.RecommendProduct;
import com.example.model.entity.UserBehavior;
import com.example.service.RecommendProductService;
import com.example.service.UserBehaviorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 推荐商品服务实现类
 * <p>
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

    @Autowired
    private UserBehaviorService userBehaviorService;

    @Autowired
    private ProductsMapper productsMapper;

    /**
     * 根据用户ID查询推荐商品列表
     * <p>
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
     * <p>
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
     * <p>
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
     * <p>
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
     * <p>
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

    /**
     * 根据商品ID查询推荐商品
     * <p>
     * 该方法查询与特定商品相关的推荐商品，
     * 用于"相关推荐"、"猜你喜欢"等功能
     *
     * @param productId 商品ID
     * @return 推荐商品列表
     */
    @Override
    @Cacheable(value = "recommendProducts", key = "'product_' + #productId")
    public List<RecommendProduct> selectByProductId(Long productId) {
        return recommendProductMapper.selectByProductId(productId);
    }

    /**
     * 查询当前有效的推荐商品
     * <p>
     * 该方法获取当前有效期内且状态为生效的推荐商品，
     * 适用于首页、分类页等位置的推荐展示
     *
     * @param type  推荐类型（可选）
     * @param limit 限制数量
     * @return 有效的推荐商品列表
     */
    @Override
    @Cacheable(value = "recommendProducts", key = "'active_' + #type + '_' + #limit")
    public List<RecommendProduct> selectActiveRecommends(Integer type, Integer limit) {
        return recommendProductMapper.selectActiveRecommends(type, limit);
    }

    /**
     * 按类型查询推荐商品
     * <p>
     * 该方法获取特定类型的推荐商品，如热门商品、新品推荐等，
     * 用于不同场景下的推荐展示
     *
     * @param type 推荐类型
     * @return 推荐商品列表
     */
    @Override
    @Cacheable(value = "recommendProducts", key = "'type_' + #type")
    public List<RecommendProduct> selectByType(Integer type) {
        return recommendProductMapper.selectByType(type);
    }

    /**
     * 更新推荐商品状态
     * <p>
     * 该方法用于启用或禁用推荐商品，
     * 适用于后台管理系统的商品推荐管理
     *
     * @param id     推荐商品ID
     * @param status 新状态
     * @return 更新结果
     */
    @Override
    @Transactional
    @CacheEvict(value = "recommendProducts", allEntries = true)
    public boolean updateStatus(Long id, Integer status) {
        return recommendProductMapper.updateStatus(id, status) > 0;
    }

    /**
     * 生成基于用户行为的个性化推荐
     * <p>
     * 该方法分析用户的浏览、收藏、购买等行为，
     * 生成符合用户兴趣的个性化商品推荐
     *
     * @param userId 用户ID
     * @param limit  推荐数量
     * @return 推荐商品列表
     */
    @Override
    @Cacheable(value = "recommendProducts", key = "'personalized_' + #userId + '_' + #limit")
    public List<RecommendProduct> generatePersonalizedRecommends(Long userId, Integer limit) {
        // 1. 获取用户行为数据
        List<UserBehavior> behaviors = userBehaviorService.selectByUserId(userId);
        if (behaviors.isEmpty()) {
            // 如果没有用户行为数据，返回热门推荐
            return selectActiveRecommends(1, limit);
        }

        // 2. 分析用户行为，找出用户感兴趣的商品类型
        Set<Long> viewedProducts = new HashSet<>();
        Map<Long, Double> productScores = new HashMap<>();

        for (UserBehavior behavior : behaviors) {
            Long productId = behavior.getProductId();
            viewedProducts.add(productId);

            // 根据行为类型和权重计算商品得分
            // 浏览=1分，收藏=2分，购买=3分
            double score = 0;
            switch (behavior.getBehaviorType()) {
                case 1: // 浏览
                    score = 1.0;
                    break;
                case 2: // 收藏
                    score = 2.0;
                    break;
                case 3: // 购买
                    score = 3.0;
                    break;
                default:
                    score = 0.5;
            }

            // 累加得分
            productScores.put(productId,
                    productScores.getOrDefault(productId, 0.0) + score);
        }

        // 3. 创建个性化推荐商品
        List<RecommendProduct> recommends = new ArrayList<>();
        // 获取商品的类别
        Map<Long, Products> productsMap = new HashMap<>();
        for (Long productId : productScores.keySet()) {
            Products product = productsMapper.selectById(productId);
            if (product != null) {
                productsMap.put(productId, product);
            }
        }

        // 4. 找出与用户感兴趣商品相似的其他商品
        for (Map.Entry<Long, Double> entry : productScores.entrySet().stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(5) // 取用户最感兴趣的5个商品
                .collect(Collectors.toList())) {

            Long productId = entry.getKey();
            Products product = productsMap.get(productId);
            if (product == null) continue;

            // 查找同类别的商品
            List<Products> similarProducts = productsMapper.selectByCategoryId(product.getCategoryId());
            for (Products similarProduct : similarProducts) {
                // 排除用户已经看过的商品
                if (!viewedProducts.contains(similarProduct.getId())) {
                    RecommendProduct recommend = new RecommendProduct();
                    recommend.setProductId(similarProduct.getId());
                    recommend.setType(3); // 算法生成
                    recommend.setStatus(1); // 生效中
                    recommend.setSort(recommends.size() + 1);
                    recommend.setStartTime(LocalDateTime.now());
                    recommend.setEndTime(LocalDateTime.now().plusDays(7)); // 设置7天有效期
                    recommend.setAlgorithmVersion("1.0");
                    recommend.setRecommendReason("根据您的兴趣推荐");

                    recommends.add(recommend);

                    if (recommends.size() >= limit) {
                        break;
                    }
                }
            }

            if (recommends.size() >= limit) {
                break;
            }
        }

        // 如果推荐不足，用热门商品补充
        if (recommends.size() < limit) {
            int remain = limit - recommends.size();
            List<RecommendProduct> hotRecommends = selectActiveRecommends(1, remain);
            recommends.addAll(hotRecommends);
        }

        return recommends;
    }

    /**
     * 生成相似商品推荐
     * <p>
     * 该方法基于当前商品特性，查找并推荐相似商品，
     * 适用于商品详情页的相似商品推荐
     *
     * @param productId 商品ID
     * @param limit     推荐数量
     * @return 相似商品推荐列表
     */
    @Override
    @Cacheable(value = "recommendProducts", key = "'similar_' + #productId + '_' + #limit")
    public List<RecommendProduct> generateSimilarProductRecommends(Long productId, Integer limit) {
        // 实现相似商品推荐算法
        // 1. 获取当前商品信息
        Products product = productsMapper.selectById(productId);
        if (product == null) {
            return new ArrayList<>();
        }

        // 2. 查找同类别的商品
        List<Products> similarProducts = productsMapper.selectByCategoryId(product.getCategoryId());

        // 3. 排除当前商品并限制数量
        List<RecommendProduct> recommends = new ArrayList<>();
        for (Products similarProduct : similarProducts) {
            if (!similarProduct.getId().equals(productId)) {
                RecommendProduct recommend = new RecommendProduct();
                recommend.setProductId(similarProduct.getId());
                recommend.setType(3); // 算法生成
                recommend.setStatus(1); // 生效中
                recommend.setSort(recommends.size() + 1);
                recommend.setStartTime(LocalDateTime.now());
                recommend.setEndTime(LocalDateTime.now().plusDays(7)); // 设置7天有效期
                recommend.setAlgorithmVersion("1.0");
                recommend.setRecommendReason("相似商品推荐");

                recommends.add(recommend);

                if (recommends.size() >= limit) {
                    break;
                }
            }
        }

        return recommends;
    }

    /**
     * 生成"猜你喜欢"推荐
     * <p>
     * 该方法根据用户历史行为，推荐猜测用户喜欢的商品，
     * 适用于首页、个人中心等场景的个性化推荐
     *
     * @param userId 用户ID
     * @param limit  推荐数量
     * @return 推荐商品列表
     */
    @Override
    @Cacheable(value = "recommendProducts", key = "'youmaylike_' + #userId + '_' + #limit")
    public List<RecommendProduct> generateYouMayLikeRecommends(Long userId, Integer limit) {
        // 实现"猜你喜欢"推荐算法
        // 简化实现：结合用户近期行为和热门商品

        // 1. 获取用户个性化推荐
        List<RecommendProduct> personalizedRecommends =
                generatePersonalizedRecommends(userId, limit / 2);

        // 2. 获取热门推荐
        List<RecommendProduct> hotRecommends =
                selectActiveRecommends(1, limit - personalizedRecommends.size());

        // 3. 合并推荐结果
        List<RecommendProduct> recommends = new ArrayList<>(personalizedRecommends);

        // 排除重复商品
        Set<Long> existProductIds = personalizedRecommends.stream()
                .map(RecommendProduct::getProductId)
                .collect(Collectors.toSet());

        for (RecommendProduct hot : hotRecommends) {
            if (!existProductIds.contains(hot.getProductId())) {
                recommends.add(hot);
                if (recommends.size() >= limit) {
                    break;
                }
            }
        }

        return recommends;
    }

    /**
     * 生成"购买此商品的人还购买了"推荐
     * <p>
     * 该方法基于协同过滤算法，分析购买行为关联性，
     * 推荐与当前商品具有购买关联的其他商品
     *
     * @param productId 当前商品ID
     * @param limit     推荐数量
     * @return 推荐商品列表
     */
    @Override
    @Cacheable(value = "recommendProducts", key = "'alsobought_' + #productId + '_' + #limit")
    public List<RecommendProduct> generateAlsoBoughtRecommends(Long productId, Integer limit) {
        // 实现"购买此商品的人还购买了"推荐算法
        // 本方法需要结合订单数据分析，这里简化实现

        // 简化：返回与当前商品相似的商品，作为替代方案
        return generateSimilarProductRecommends(productId, limit);
    }

    /**
     * 批量更新推荐商品状态
     * <p>
     * 该方法用于批量启用或禁用推荐商品，
     * 适用于后台管理系统的商品推荐管理
     *
     * @param ids    推荐商品ID列表
     * @param status 新状态
     * @return 更新结果
     */
    @Override
    @Transactional
    @CacheEvict(value = "recommendProducts", allEntries = true)
    public boolean batchUpdateStatus(List<Long> ids, Integer status) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }

        int successCount = 0;
        for (Long id : ids) {
            if (recommendProductMapper.updateStatus(id, status) > 0) {
                successCount++;
            }
        }

        return successCount > 0;
    }
}




