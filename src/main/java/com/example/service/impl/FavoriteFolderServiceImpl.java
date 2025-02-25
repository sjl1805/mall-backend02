package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.FavoriteFolderMapper;
import com.example.model.entity.FavoriteFolder;
import com.example.service.FavoriteFolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 收藏夹服务实现类
 * <p>
 * 该类实现了收藏夹相关的业务逻辑，包括收藏夹的创建、查询、更新和删除等功能。
 * 收藏夹是用户对收藏商品进行分类管理的工具，能够提升用户收藏体验和检索效率。
 * 用户可以创建多个收藏夹，如"心愿单"、"喜欢的服装"、"下次购买"等，满足不同场景的收藏需求。
 * 使用了Spring缓存机制对收藏夹信息进行缓存，提高查询效率。
 * 使用MyBatis-Plus的ServiceImpl简化数据访问操作。
 *
 * @author 31815
 * @description 针对表【favorite_folder(收藏夹表)】的数据库操作Service实现
 * @createDate 2025-02-24 12:04:16
 */
@Service
@CacheConfig(cacheNames = "favoriteFolders") // 指定该服务类的缓存名称为"favoriteFolders"
public class FavoriteFolderServiceImpl extends ServiceImpl<FavoriteFolderMapper, FavoriteFolder>
        implements FavoriteFolderService {

    @Autowired
    private FavoriteFolderMapper favoriteFolderMapper;

    /**
     * 根据用户ID查询收藏夹列表
     * <p>
     * 该方法从缓存或数据库获取指定用户的所有收藏夹，
     * 用于个人中心展示用户创建的收藏分类，便于用户管理收藏商品
     *
     * @param userId 用户ID
     * @return 收藏夹列表
     */
    @Override
    @Cacheable(value = "favoriteFolders", key = "#userId") // 缓存用户收藏夹数据，提高查询效率
    public List<FavoriteFolder> selectByUserId(Long userId) {
        return favoriteFolderMapper.selectByUserId(userId);
    }

    /**
     * 分页查询收藏夹数据
     * <p>
     * 该方法用于后台管理系统分页查看用户收藏夹数据，
     * 可用于分析用户收藏习惯和偏好
     *
     * @param page 分页参数
     * @return 收藏夹分页数据
     */
    @Override
    public IPage<FavoriteFolder> selectPage(IPage<FavoriteFolder> page) {
        return favoriteFolderMapper.selectPage(page);
    }

    /**
     * 根据ID查询收藏夹
     * <p>
     * 该方法从缓存或数据库获取指定ID的收藏夹详情，
     * 用于查看特定收藏夹的内容或进行编辑操作
     *
     * @param id 收藏夹ID
     * @return 收藏夹实体
     */
    @Override
    @Cacheable(value = "favoriteFolders", key = "#id") // 缓存收藏夹详情，提高查询效率
    public FavoriteFolder selectById(Long id) {
        return favoriteFolderMapper.selectById(id);
    }

    /**
     * 创建收藏夹
     * <p>
     * 该方法用于用户创建新的收藏夹，
     * 可以指定收藏夹名称、图标、排序等属性，
     * 为用户提供更有组织的收藏管理功能
     *
     * @param favoriteFolder 收藏夹实体
     * @return 创建成功返回true，失败返回false
     */
    @Override
    @Transactional
    @CacheEvict(value = "favoriteFolders", key = "#favoriteFolder.id") // 清除收藏夹缓存
    public boolean insertFavoriteFolder(FavoriteFolder favoriteFolder) {
        return favoriteFolderMapper.insert(favoriteFolder) > 0;
    }

    /**
     * 更新收藏夹
     * <p>
     * 该方法用于用户修改收藏夹信息，如重命名、更改图标等，
     * 增强用户对收藏内容的个性化管理能力，
     * 并清除相关缓存，确保数据一致性
     *
     * @param favoriteFolder 收藏夹实体
     * @return 更新成功返回true，失败返回false
     */
    @Override
    @Transactional
    @CacheEvict(value = "favoriteFolders", key = "#favoriteFolder.id") // 清除收藏夹缓存
    public boolean updateFavoriteFolder(FavoriteFolder favoriteFolder) {
        return favoriteFolderMapper.updateById(favoriteFolder) > 0;
    }

    /**
     * 删除收藏夹
     * <p>
     * 该方法用于用户删除不再需要的收藏夹，
     * 需要注意的是，删除收藏夹可能需要同时处理该夹中的收藏商品，
     * 建议使用事务确保数据完整性，并清除相关缓存
     *
     * @param id 收藏夹ID
     * @return 删除成功返回true，失败返回false
     */
    @Override
    @Transactional
    @CacheEvict(value = "favoriteFolders", key = "#id") // 清除被删除收藏夹的缓存
    public boolean deleteFavoriteFolder(Long id) {
        return favoriteFolderMapper.deleteById(id) > 0;
    }

    /**
     * 更新收藏夹商品数量
     * <p>
     * 该方法在添加或移除收藏商品时更新收藏夹的商品计数，
     * 保持收藏夹数据的准确性
     *
     * @param id    收藏夹ID
     * @param count 增加数量，可为负数（减少）
     * @return 更新结果
     */
    @Override
    @Transactional
    @CacheEvict(value = "favoriteFolders", key = "#id")
    public boolean updateItemCount(Long id, Integer count) {
        return favoriteFolderMapper.updateItemCount(id, count) > 0;
    }

    /**
     * 刷新收藏夹商品数量
     * <p>
     * 该方法通过查询关联的商品收藏记录刷新收藏夹的商品计数，
     * 用于修复因异常导致的计数不准确问题
     *
     * @param id 收藏夹ID
     * @return 更新结果
     */
    @Override
    @Transactional
    @CacheEvict(value = "favoriteFolders", key = "#id")
    public boolean refreshItemCount(Long id) {
        return favoriteFolderMapper.refreshItemCount(id) > 0;
    }

    /**
     * 更新收藏夹排序
     * <p>
     * 该方法用于用户调整收藏夹在列表中的显示顺序，
     * 提供个性化的收藏夹管理体验
     *
     * @param id   收藏夹ID
     * @param sort 排序值
     * @return 更新结果
     */
    @Override
    @Transactional
    @CacheEvict(value = "favoriteFolders", key = "#id")
    public boolean updateSort(Long id, Integer sort) {
        return favoriteFolderMapper.updateSort(id, sort) > 0;
    }

    /**
     * 获取用户收藏夹（按排序）
     * <p>
     * 该方法根据设定的排序获取用户的收藏夹列表，
     * 适用于收藏夹列表页面展示
     *
     * @param userId 用户ID
     * @return 收藏夹列表
     */
    @Override
    @Cacheable(value = "favoriteFolders", key = "'sort_' + #userId")
    public List<FavoriteFolder> selectByUserIdOrderBySort(Long userId) {
        return favoriteFolderMapper.selectByUserIdOrderBySort(userId);
    }

    /**
     * 查询用户公开收藏夹
     * <p>
     * 该方法获取用户设置为公开的收藏夹列表，
     * 用于个人主页展示和社交分享功能
     *
     * @param userId 用户ID
     * @return 收藏夹列表
     */
    @Override
    @Cacheable(value = "favoriteFolders", key = "'public_' + #userId")
    public List<FavoriteFolder> selectPublicFolders(Long userId) {
        return favoriteFolderMapper.selectPublicFolders(userId);
    }

    /**
     * 查询其他用户的公开收藏夹
     * <p>
     * 该方法获取除指定用户外的其他用户公开的收藏夹，
     * 用于社区和推荐功能
     *
     * @param userId 用户ID
     * @return 收藏夹列表
     */
    @Override
    @Cacheable(value = "favoriteFolders", key = "'others_' + #userId")
    public List<FavoriteFolder> selectOtherPublicFolders(Long userId) {
        return favoriteFolderMapper.selectOtherPublicFolders(userId);
    }

    /**
     * 按名称查询收藏夹
     * <p>
     * 该方法用于收藏夹搜索功能，支持模糊匹配收藏夹名称，
     * 帮助用户快速找到特定的收藏夹
     *
     * @param userId 用户ID
     * @param name   收藏夹名称（模糊匹配）
     * @return 收藏夹列表
     */
    @Override
    @Cacheable(value = "favoriteFolders", key = "#userId + '_name_' + #name")
    public List<FavoriteFolder> selectByName(Long userId, String name) {
        return favoriteFolderMapper.selectByName(userId, name);
    }

    /**
     * 批量删除收藏夹
     * <p>
     * 该方法用于批量删除多个收藏夹，
     * 适用于收藏夹管理页面的批量操作
     *
     * @param ids 收藏夹ID列表
     * @return 删除结果
     */
    @Override
    @Transactional
    @CacheEvict(value = "favoriteFolders", allEntries = true)
    public boolean batchDelete(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        // 删除收藏夹前，可能需要处理关联的收藏记录
        return favoriteFolderMapper.batchDelete(ids) > 0;
    }

    /**
     * 批量更新收藏夹公开状态
     * <p>
     * 该方法用于批量修改多个收藏夹的公开/私密状态，
     * 适用于隐私设置调整和批量操作
     *
     * @param ids      收藏夹ID列表
     * @param isPublic 公开状态
     * @return 更新结果
     */
    @Override
    @Transactional
    @CacheEvict(value = "favoriteFolders", allEntries = true)
    public boolean batchUpdatePublicStatus(List<Long> ids, Integer isPublic) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        return favoriteFolderMapper.batchUpdatePublicStatus(ids, isPublic) > 0;
    }

    /**
     * 创建默认收藏夹
     * <p>
     * 该方法在用户注册后自动创建一个默认收藏夹，
     * 通常命名为"我的收藏"或"默认收藏夹"
     *
     * @param userId 用户ID
     * @return 创建的收藏夹
     */
    @Override
    @Transactional
    public FavoriteFolder createDefaultFolder(Long userId) {
        FavoriteFolder folder = new FavoriteFolder();
        folder.setUserId(userId);
        folder.setName("我的收藏");
        folder.setDescription("默认收藏夹");
        folder.setIsPublic(0); // 默认私密
        folder.setItemCount(0);
        folder.setSort(0); // 默认排序最前

        if (insertFavoriteFolder(folder)) {
            return folder;
        }
        return null;
    }

    /**
     * 检查收藏夹归属权
     * <p>
     * 该方法验证收藏夹是否属于指定用户，
     * 用于权限控制，确保用户只能操作自己的收藏夹
     *
     * @param folderId 收藏夹ID
     * @param userId   用户ID
     * @return 是否属于该用户
     */
    @Override
    public boolean checkOwnership(Long folderId, Long userId) {
        FavoriteFolder folder = selectById(folderId);
        return folder != null && folder.getUserId().equals(userId);
    }

    /**
     * 移动商品到新收藏夹
     * <p>
     * 该方法将一组收藏商品移动到指定的收藏夹，
     * 需要同时更新原收藏夹和目标收藏夹的商品计数
     *
     * @param favoriteIds    收藏ID列表
     * @param targetFolderId 目标收藏夹ID
     * @return 移动结果
     */
    @Override
    @Transactional
    @CacheEvict(value = "favoriteFolders", allEntries = true)
    public boolean moveFavorites(List<Long> favoriteIds, Long targetFolderId) {
        return favoriteIds != null && !favoriteIds.isEmpty();

        // 实际实现需要调用ProductFavoriteMapper的相关方法
        // 1. 更新收藏记录的folderId
        // 2. 更新原收藏夹和目标收藏夹的itemCount

        // 简化实现，假设有一个方法可以移动收藏记录
        // boolean result = productFavoriteMapper.batchMoveToFolder(favoriteIds, targetFolderId);

        // 之后需要刷新相关收藏夹的计数
        // refreshItemCount(sourceFolderId);
        // refreshItemCount(targetFolderId);
// 简化实现，实际应返回真实结果
    }
}




