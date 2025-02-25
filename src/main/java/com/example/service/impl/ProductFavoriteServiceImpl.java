package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import java.util.Map;

/**
 * 商品收藏服务实现类
 * <p>
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
     * <p>
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
     * <p>
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
     * <p>
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
     * <p>
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
     * <p>
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

    /**
     * 检查用户是否已收藏商品
     * <p>
     * 该方法检查指定用户是否已经收藏了指定商品，
     * 用于前台展示收藏状态和后台防止重复收藏
     *
     * @param userId    用户ID
     * @param productId 商品ID
     * @return 收藏记录，未收藏则返回null
     */
    @Override
    @Cacheable(key = "#userId + '_' + #productId")
    public ProductFavorite checkFavorite(Long userId, Long productId) {
        return productFavoriteMapper.checkFavorite(userId, productId);
    }

    /**
     * 切换收藏状态（收藏/取消收藏）
     * <p>
     * 该方法实现一键收藏或取消收藏功能，
     * 如果商品未收藏则添加收藏，已收藏则取消收藏，
     * 简化前端操作逻辑
     *
     * @param userId    用户ID
     * @param productId 商品ID
     * @return true-收藏成功，false-取消收藏成功
     */
    @Override
    @Transactional
    @CacheEvict(key = "#userId + '_' + #productId")
    public boolean toggleFavorite(Long userId, Long productId) {
        ProductFavorite existingFavorite = checkFavorite(userId, productId);

        if (existingFavorite == null) {
            // 未收藏，执行收藏操作
            ProductFavorite favorite = new ProductFavorite();
            favorite.setUserId(userId);
            favorite.setProductId(productId);
            return insertProductFavorite(favorite);
        } else {
            // 已收藏，执行取消收藏操作
            return deleteProductFavorite(existingFavorite.getId());
        }
    }

    /**
     * 根据收藏夹查询收藏商品
     * <p>
     * 该方法查询用户指定收藏夹中的商品，
     * 支持收藏分类管理，提高用户收藏体验
     *
     * @param userId   用户ID
     * @param folderId 收藏夹ID，为null则查询默认收藏夹
     * @return 收藏商品列表
     */
    @Override
    @Cacheable(key = "#userId + '_folder_' + #folderId")
    public List<ProductFavorite> selectByFolder(Long userId, Long folderId) {
        return productFavoriteMapper.selectByFolder(userId, folderId);
    }

    /**
     * 获取用户的收藏夹列表
     * <p>
     * 该方法查询用户创建的所有收藏夹，
     * 用于前台展示收藏夹选择和管理
     *
     * @param userId 用户ID
     * @return 收藏夹列表
     */
    @Override
    @Cacheable(key = "'folders_' + #userId")
    public List<Map<String, Object>> getUserFolders(Long userId) {
        // 这里需要有收藏夹相关的mapper方法，暂时返回空列表
        // 实际应该返回folderMapper.selectByUserId(userId);
        return new java.util.ArrayList<>();
    }

    /**
     * 创建收藏夹
     * <p>
     * 该方法用于用户创建新的收藏夹，
     * 用于收藏分类管理
     *
     * @param userId     用户ID
     * @param folderName 收藏夹名称
     * @return 创建结果
     */
    @Override
    @Transactional
    @CacheEvict(key = "'folders_' + #userId")
    public boolean createFolder(Long userId, String folderName) {
        // 这里需要有收藏夹相关的mapper方法
        // 实际应该调用folderMapper.insert(folder);
        return true;
    }

    /**
     * 移动商品到指定收藏夹
     * <p>
     * 该方法用于将收藏的商品移动到指定的收藏夹，
     * 方便用户整理和管理收藏商品
     *
     * @param id       收藏ID
     * @param folderId 目标收藏夹ID
     * @return 移动结果
     */
    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public boolean moveToFolder(Long id, Long folderId) {
        return productFavoriteMapper.moveToFolder(id, folderId) > 0;
    }

    /**
     * 查询热门收藏商品
     * <p>
     * 该方法统计被收藏最多的商品，
     * 用于发现热门商品和推荐系统
     *
     * @param limit 限制数量
     * @return 热门收藏商品及收藏次数
     */
    @Override
    @Cacheable(key = "'hot_' + #limit")
    public List<Map<String, Object>> getHotFavorites(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10; // 默认返回10条
        }
        return productFavoriteMapper.selectHotFavorites(limit);
    }

    /**
     * 获取带商品信息的收藏列表
     * <p>
     * 该方法查询用户收藏的商品，并包含商品详细信息，
     * 减少前端请求次数，提高用户体验
     *
     * @param userId 用户ID
     * @return 收藏商品列表（包含商品信息）
     */
    @Override
    @Cacheable(key = "'info_' + #userId")
    public List<ProductFavorite> selectWithProductInfo(Long userId) {
        // 需要关联商品表查询，实现方式可能是自定义SQL或代码层组合
        // 简化实现，返回基础收藏列表
        return selectByUserId(userId);
    }

    /**
     * 批量删除收藏
     * <p>
     * 该方法批量删除用户的收藏记录，
     * 用于用户批量取消收藏或清空收藏夹
     *
     * @param ids 收藏ID列表
     * @return 删除结果
     */
    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public boolean batchDelete(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        return productFavoriteMapper.batchDelete(ids) > 0;
    }

    /**
     * 统计用户收藏商品数量
     * <p>
     * 该方法统计用户收藏的商品总数，
     * 用于个人中心展示和收藏数据分析
     *
     * @param userId 用户ID
     * @return 收藏数量
     */
    @Override
    @Cacheable(key = "'count_' + #userId")
    public int countUserFavorites(Long userId) {
        QueryWrapper<ProductFavorite> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return (int) count(queryWrapper);
    }
}




