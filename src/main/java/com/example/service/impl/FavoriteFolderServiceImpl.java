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

import java.util.List;

/**
 * @author 31815
 * @description 针对表【favorite_folder(收藏夹表)】的数据库操作Service实现
 * @createDate 2025-02-24 12:04:16
 */
@Service
@CacheConfig(cacheNames = "favoriteFolders")
public class FavoriteFolderServiceImpl extends ServiceImpl<FavoriteFolderMapper, FavoriteFolder>
        implements FavoriteFolderService {

    @Autowired
    private FavoriteFolderMapper favoriteFolderMapper;

    @Override
    @Cacheable(value = "favoriteFolders", key = "#userId")
    public List<FavoriteFolder> selectByUserId(Long userId) {
        return favoriteFolderMapper.selectByUserId(userId);
    }

    @Override
    public IPage<FavoriteFolder> selectPage(IPage<FavoriteFolder> page) {
        return favoriteFolderMapper.selectPage(page);
    }

    @Override
    @Cacheable(value = "favoriteFolders", key = "#id")
    public FavoriteFolder selectById(Long id) {
        return favoriteFolderMapper.selectById(id);
    }

    @Override
    @CacheEvict(value = "favoriteFolders", key = "#favoriteFolder.id")
    public boolean insertFavoriteFolder(FavoriteFolder favoriteFolder) {
        return favoriteFolderMapper.insert(favoriteFolder) > 0;
    }

    @Override
    @CacheEvict(value = "favoriteFolders", key = "#favoriteFolder.id")
    public boolean updateFavoriteFolder(FavoriteFolder favoriteFolder) {
        return favoriteFolderMapper.updateById(favoriteFolder) > 0;
    }

    @Override
    @CacheEvict(value = "favoriteFolders", key = "#id")
    public boolean deleteFavoriteFolder(Long id) {
        return favoriteFolderMapper.deleteById(id) > 0;
    }
}




