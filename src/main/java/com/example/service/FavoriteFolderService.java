package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.dto.favorite.FavoriteFolderDTO;
import com.example.model.dto.favorite.FavoriteFolderPageDTO;
import com.example.model.entity.FavoriteFolder;

import java.util.List;

/**
 * @author 31815
 * @description 针对表【favorite_folder(收藏夹表)】的数据库操作Service
 * @createDate 2025-02-18 23:44:24
 */
public interface FavoriteFolderService extends IService<FavoriteFolder> {
    boolean createFolder(Long userId, FavoriteFolderDTO folderDTO);

    List<FavoriteFolder> getUserFolders(Long userId);

    boolean updateFolder(Long userId, Long folderId, FavoriteFolderDTO folderDTO);

    boolean updatePublicStatus(Long userId, Long folderId, Integer isPublic);

    boolean updateSort(Long userId, Long folderId, Integer newSort);

    boolean deleteFolder(Long userId, Long folderId);

    IPage<FavoriteFolder> listPublicFolders(FavoriteFolderPageDTO queryDTO);

    boolean updateItemCount(Long userId, Long folderId, Integer delta);

    boolean checkFolderOwnership(Long userId, Long folderId);
}
