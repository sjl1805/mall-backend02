package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.service.ProductFavoriteService;
import com.example.model.entity.ProductFavorite;
import com.example.mapper.ProductFavoriteMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.example.model.dto.favorite.ProductFavoriteDTO;
import com.example.model.dto.favorite.ProductFavoritePageDTO;
import com.example.exception.BusinessException;
import com.example.common.ResultCode;
import com.example.service.FavoriteFolderService;

/**
* @author 31815
* @description 针对表【product_favorite(商品收藏表)】的数据库操作Service实现
* @createDate 2025-02-18 23:44:15
*/
@Service
@CacheConfig(cacheNames = "productFavoriteService")
public class ProductFavoriteServiceImpl extends ServiceImpl<ProductFavoriteMapper, ProductFavorite>
    implements ProductFavoriteService {

    //private static final Logger logger = LoggerFactory.getLogger(ProductFavoriteServiceImpl.class);

    @Autowired
    private FavoriteFolderService folderService;

    @Override
    @Transactional
    @CacheEvict(key = "'user:' + #userId")
    public boolean addFavorite(Long userId, ProductFavoriteDTO favoriteDTO) {
        // 校验收藏夹归属
        if (!folderService.checkFolderOwnership(userId, favoriteDTO.getFolderId())) {
            throw new BusinessException(ResultCode.FOLDER_ACCESS_DENIED);
        }

        // 检查是否已收藏
        if (baseMapper.checkFavoriteExists(userId, favoriteDTO.getProductId()) > 0) {
            throw new BusinessException(ResultCode.FAVORITE_ALREADY_EXISTS);
        }

        ProductFavorite favorite = new ProductFavorite();
        BeanUtils.copyProperties(favoriteDTO, favorite);
        favorite.setUserId(userId);
        
        boolean success = save(favorite);
        if (success) {
            // 更新收藏夹计数
            folderService.updateItemCount(userId, favoriteDTO.getFolderId(), 1);
        }
        return success;
    }

    @Override
    @Transactional
    @CacheEvict(key = "'user:' + #userId")
    public boolean removeFavorite(Long userId, Long favoriteId) {
        ProductFavorite favorite = getById(favoriteId);
        if (favorite == null) {
            throw new BusinessException(ResultCode.FAVORITE_NOT_FOUND);
        }

        if (!favorite.getUserId().equals(userId)) {
            throw new BusinessException(ResultCode.FAVORITE_ACCESS_DENIED);
        }

        boolean success = removeById(favoriteId);
        if (success) {
            folderService.updateItemCount(userId, favorite.getFolderId(), -1);
        }
        return success;
    }

    @Override
    @Cacheable(key = "'user:' + #userId + ':folder:' + #folderId")
    public IPage<ProductFavorite> listFavorites(Long userId, Long folderId, ProductFavoritePageDTO queryDTO) {
        queryDTO.setUserId(userId);
        queryDTO.setFolderId(folderId);
        Page<ProductFavorite> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        return baseMapper.selectFavoritePage(page, queryDTO);
    }

    @Override
    @Transactional
    @CacheEvict(key = "'user:' + #userId")
    public boolean moveToFolder(Long userId, List<Long> favoriteIds, Long targetFolderId) {
        // 校验目标收藏夹归属
        if (!folderService.checkFolderOwnership(userId, targetFolderId)) {
            throw new BusinessException(ResultCode.FOLDER_ACCESS_DENIED);
        }

        // 获取原收藏夹ID用于更新计数
        List<ProductFavorite> favorites = listByIds(favoriteIds);
        Map<Long, Integer> folderCountMap = favorites.stream()
                .filter(f -> f.getUserId().equals(userId))
                .collect(Collectors.groupingBy(
                        ProductFavorite::getFolderId,
                        Collectors.summingInt(e -> 1)
                ));

        // 执行转移
        int affected = baseMapper.moveToFolder(favoriteIds, targetFolderId);

        // 更新收藏夹计数
        folderCountMap.forEach((folderId, count) -> 
            folderService.updateItemCount(userId, folderId, -count)
        );
        folderService.updateItemCount(userId, targetFolderId, affected);

        return affected > 0;
    }

    @Override
    @Cacheable(key = "'user:' + #userId + ':count'")
    public int getUserFavoriteCount(Long userId) {
        return baseMapper.countUserFavorites(userId);
    }

    @Override
    public boolean checkFavoriteExists(Long userId, Long productId) {
        return baseMapper.checkFavoriteExists(userId, productId) > 0;
    }
}




