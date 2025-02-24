package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.FavoriteFolder;

import java.util.List;

/**
 * @author 31815
 * @description 针对表【favorite_folder(收藏夹表)】的数据库操作Service
 * @createDate 2025-02-24 12:04:16
 */
public interface FavoriteFolderService extends IService<FavoriteFolder> {

    /**
     * 根据用户ID查询收藏夹
     *
     * @param userId 用户ID
     * @return 收藏夹列表
     */
    List<FavoriteFolder> selectByUserId(Long userId);

    /**
     * 分页查询收藏夹
     *
     * @param page 分页信息
     * @return 收藏夹列表
     */
    IPage<FavoriteFolder> selectPage(IPage<FavoriteFolder> page);

    /**
     * 根据ID查询收藏夹
     *
     * @param id 收藏夹ID
     * @return 收藏夹信息
     */
    FavoriteFolder selectById(Long id);

    /**
     * 新增收藏夹
     *
     * @param favoriteFolder 收藏夹信息
     * @return 插入结果
     */
    boolean insertFavoriteFolder(FavoriteFolder favoriteFolder);

    /**
     * 更新收藏夹信息
     *
     * @param favoriteFolder 收藏夹信息
     * @return 更新结果
     */
    boolean updateFavoriteFolder(FavoriteFolder favoriteFolder);

    /**
     * 根据ID删除收藏夹
     *
     * @param id 收藏夹ID
     * @return 删除结果
     */
    boolean deleteFavoriteFolder(Long id);
}
