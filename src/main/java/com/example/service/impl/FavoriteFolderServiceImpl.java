package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.ResultCode;
import com.example.exception.BusinessException;
import com.example.mapper.FavoriteFolderMapper;
import com.example.model.dto.favorite.FavoriteFolderDTO;
import com.example.model.dto.favorite.FavoriteFolderPageDTO;
import com.example.model.entity.FavoriteFolder;
import com.example.service.FavoriteFolderService;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 31815
 * @description 针对表【favorite_folder(收藏夹表)】的数据库操作Service实现
 * @createDate 2025-02-18 23:44:24
 */
@Service
@CacheConfig(cacheNames = "favoriteService")
public class FavoriteFolderServiceImpl extends ServiceImpl<FavoriteFolderMapper, FavoriteFolder>
        implements FavoriteFolderService {

    //private static final Logger logger = LoggerFactory.getLogger(FavoriteFolderServiceImpl.class);

    @Override
    @Transactional
    @CacheEvict(key = "'user:' + #userId")
    public boolean createFolder(Long userId, FavoriteFolderDTO folderDTO) {
        // 校验名称唯一性
        if (baseMapper.checkNameUnique(userId, folderDTO.getName(), null) > 0) {
            throw new BusinessException(ResultCode.FOLDER_NAME_EXISTS);
        }

        FavoriteFolder folder = new FavoriteFolder();
        BeanUtils.copyProperties(folderDTO, folder);
        folder.setUserId(userId);
        return save(folder);
    }

    @Override
    @Cacheable(key = "'user:' + #userId")
    public List<FavoriteFolder> getUserFolders(Long userId) {
        return lambdaQuery()
                .eq(FavoriteFolder::getUserId, userId)
                .orderByAsc(FavoriteFolder::getSort)
                .list();
    }

    @Override
    @Transactional
    @CacheEvict(key = "'user:' + #userId")
    public boolean updateFolder(Long userId, Long folderId, FavoriteFolderDTO folderDTO) {
        // 校验名称唯一性
        if (baseMapper.checkNameUnique(userId, folderDTO.getName(), folderId) > 0) {
            throw new BusinessException(ResultCode.FOLDER_NAME_EXISTS);
        }

        return lambdaUpdate()
                .eq(FavoriteFolder::getId, folderId)
                .eq(FavoriteFolder::getUserId, userId)
                .set(FavoriteFolder::getName, folderDTO.getName())
                .set(FavoriteFolder::getDescription, folderDTO.getDescription())
                .set(FavoriteFolder::getIsPublic, folderDTO.getIsPublic())
                .update();
    }

    @Override
    @Transactional
    @CacheEvict(key = "'user:' + #userId")
    public boolean updatePublicStatus(Long userId, Long folderId, Integer isPublic) {
        return baseMapper.updatePublicStatus(folderId, isPublic) > 0;
    }

    @Override
    @Transactional
    @CacheEvict(key = "'user:' + #userId")
    public boolean updateSort(Long userId, Long folderId, Integer newSort) {
        return baseMapper.updateSort(folderId, newSort) > 0;
    }

    @Override
    @Transactional
    @CacheEvict(key = "'user:' + #userId")
    public boolean deleteFolder(Long userId, Long folderId) {
        return lambdaUpdate()
                .eq(FavoriteFolder::getId, folderId)
                .eq(FavoriteFolder::getUserId, userId)
                .remove();
    }

    @Override
    @Cacheable(key = "'public:page:' + #queryDTO.hashCode()")
    public IPage<FavoriteFolder> listPublicFolders(FavoriteFolderPageDTO queryDTO) {
        Page<FavoriteFolder> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        return baseMapper.selectFolderPage(page, queryDTO);
    }

    @Override
    @Transactional
    @CacheEvict(key = "'user:' + #userId")
    public boolean updateItemCount(Long userId, Long folderId, Integer delta) {
        return baseMapper.updateItemCount(folderId, delta) > 0;
    }

    @Override
    public boolean checkFolderOwnership(Long userId, Long folderId) {
        return lambdaQuery()
                .eq(FavoriteFolder::getId, folderId)
                .eq(FavoriteFolder::getUserId, userId)
                .exists();
    }
}




