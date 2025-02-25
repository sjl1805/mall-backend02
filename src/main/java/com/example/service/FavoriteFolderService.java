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

    /**
     * 更新收藏夹商品数量
     *
     * @param id    收藏夹ID
     * @param count 增加数量，可为负数（减少）
     * @return 更新结果
     */
    boolean updateItemCount(Long id, Integer count);

    /**
     * 刷新收藏夹商品数量（根据实际收藏商品数量）
     *
     * @param id 收藏夹ID
     * @return 更新结果
     */
    boolean refreshItemCount(Long id);

    /**
     * 更新收藏夹排序
     *
     * @param id   收藏夹ID
     * @param sort 排序值
     * @return 更新结果
     */
    boolean updateSort(Long id, Integer sort);

    /**
     * 获取用户收藏夹（按排序）
     *
     * @param userId 用户ID
     * @return 收藏夹列表
     */
    List<FavoriteFolder> selectByUserIdOrderBySort(Long userId);

    /**
     * 查询用户公开收藏夹
     *
     * @param userId 用户ID
     * @return 收藏夹列表
     */
    List<FavoriteFolder> selectPublicFolders(Long userId);

    /**
     * 查询其他用户的公开收藏夹
     *
     * @param userId 用户ID
     * @return 收藏夹列表
     */
    List<FavoriteFolder> selectOtherPublicFolders(Long userId);

    /**
     * 按名称查询收藏夹
     *
     * @param userId 用户ID
     * @param name   收藏夹名称（模糊匹配）
     * @return 收藏夹列表
     */
    List<FavoriteFolder> selectByName(Long userId, String name);

    /**
     * 批量删除收藏夹
     *
     * @param ids 收藏夹ID列表
     * @return 删除结果
     */
    boolean batchDelete(List<Long> ids);

    /**
     * 批量更新收藏夹公开状态
     *
     * @param ids      收藏夹ID列表
     * @param isPublic 公开状态
     * @return 更新结果
     */
    boolean batchUpdatePublicStatus(List<Long> ids, Integer isPublic);

    /**
     * 创建默认收藏夹
     *
     * @param userId 用户ID
     * @return 创建的收藏夹
     */
    FavoriteFolder createDefaultFolder(Long userId);

    /**
     * 检查收藏夹归属权
     *
     * @param folderId 收藏夹ID
     * @param userId   用户ID
     * @return 是否属于该用户
     */
    boolean checkOwnership(Long folderId, Long userId);

    /**
     * 移动商品到新收藏夹
     *
     * @param favoriteIds    收藏ID列表
     * @param targetFolderId 目标收藏夹ID
     * @return 移动结果
     */
    boolean moveFavorites(List<Long> favoriteIds, Long targetFolderId);
}
