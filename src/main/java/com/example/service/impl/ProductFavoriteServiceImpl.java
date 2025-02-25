package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.ProductFavoriteMapper;
import com.example.model.entity.ProductFavorite;
import com.example.service.ProductFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商品收藏服务实现类
 * 
 * 该类实现了商品收藏相关的业务逻辑，包括收藏的添加、取消、查询等功能。
 * 商品收藏是电商系统中重要的用户互动功能，既便于用户快速找到感兴趣的商品，
 * 也为个性化推荐系统提供重要的用户偏好数据。
 * 使用了Spring缓存机制对收藏信息进行缓存，提高查询效率。
 * 使用MyBatis-Plus的ServiceImpl简化数据访问操作。
 *
 * @author 31815
 * @description 针对表【product_favorite(商品收藏表)】的数据库操作Service实现
 * @createDate 2025-02-24 12:04:07
 */
@Service
@CacheConfig(cacheNames = "productFavorites") // 指定该服务类的缓存名称为"productFavorites"
public class ProductFavoriteServiceImpl extends ServiceImpl<ProductFavoriteMapper, ProductFavorite>
        implements ProductFavoriteService {

    @Autowired
    private ProductFavoriteMapper productFavoriteMapper;

    /**
     * 根据用户ID查询收藏商品列表
     * 
     * 该方法从缓存或数据库获取指定用户的所有收藏商品，
     * 用于个人中心的收藏列表展示，方便用户快速访问感兴趣的商品
     *
     * @param userId 用户ID
     * @return 收藏商品列表
     */
    @Override
    @Cacheable(value = "productFavorites", key = "#userId") // 缓存用户收藏数据，提高查询效率
    public List<ProductFavorite> selectByUserId(Long userId) {
        return productFavoriteMapper.selectByUserId(userId);
    }

    /**
     * 分页查询商品收藏数据
     * 
     * 该方法用于后台管理系统分页查看用户收藏数据，
     * 可用于分析热门收藏商品、用户偏好等
     *
     * @param page 分页参数
     * @return 商品收藏分页数据
     */
    @Override
    public IPage<ProductFavorite> selectPage(IPage<ProductFavorite> page) {
        return productFavoriteMapper.selectPage(page);
    }

    /**
     * 根据ID查询商品收藏
     *
     * @param id 收藏记录ID
     * @return 商品收藏实体
     */
    @Override
    @Cacheable(value = "productFavorites", key = "#id") // 缓存收藏详情，提高查询效率
    public ProductFavorite selectById(Long id) {
        return productFavoriteMapper.selectById(id);
    }

    /**
     * 添加商品收藏
     * 
     * 该方法用于用户收藏商品时创建收藏记录，
     * 收藏操作会影响用户的个性化推荐结果，
     * 并清除相关缓存，确保数据一致性
     *
     * @param productFavorite 商品收藏实体
     * @return 添加成功返回true，失败返回false
     */
    @Override
    @Transactional
    @CacheEvict(value = "productFavorites", key = "#productFavorite.id") // 清除收藏缓存
    public boolean insertProductFavorite(ProductFavorite productFavorite) {
        return productFavoriteMapper.insert(productFavorite) > 0;
    }

    /**
     * 更新商品收藏
     * 
     * 该方法用于更新收藏记录的信息，如修改收藏时间、分组等，
     * 并清除相关缓存，确保数据一致性
     *
     * @param productFavorite 商品收藏实体
     * @return 更新成功返回true，失败返回false
     */
    @Override
    @Transactional
    @CacheEvict(value = "productFavorites", key = "#productFavorite.id") // 清除收藏缓存
    public boolean updateProductFavorite(ProductFavorite productFavorite) {
        return productFavoriteMapper.updateById(productFavorite) > 0;
    }

    /**
     * 删除商品收藏
     * 
     * 该方法用于用户取消收藏商品，
     * 取消收藏可能会影响用户的个性化推荐结果，
     * 并清除相关缓存，确保数据一致性
     *
     * @param id 收藏记录ID
     * @return 删除成功返回true，失败返回false
     */
    @Override
    @Transactional
    @CacheEvict(value = "productFavorites", key = "#id") // 清除被删除收藏的缓存
    public boolean deleteProductFavorite(Long id) {
        return productFavoriteMapper.deleteById(id) > 0;
    }
}




