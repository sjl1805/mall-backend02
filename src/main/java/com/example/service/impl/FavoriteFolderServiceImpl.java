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
 * 
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
     * 
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
     * 
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
     * 
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
     * 
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
     * 
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
     * 
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
}




