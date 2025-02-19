package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.dto.favorite.ProductFavoriteDTO;
import com.example.model.dto.favorite.ProductFavoritePageDTO;
import com.example.model.entity.ProductFavorite;

import java.util.List;

/**
 * @author 31815
 * @description 针对表【product_favorite(商品收藏表)】的数据库操作Service
 * @createDate 2025-02-18 23:44:15
 */
public interface ProductFavoriteService extends IService<ProductFavorite> {
    boolean addFavorite(Long userId, ProductFavoriteDTO favoriteDTO);

    boolean removeFavorite(Long userId, Long favoriteId);

    IPage<ProductFavorite> listFavorites(Long userId, Long folderId, ProductFavoritePageDTO queryDTO);

    boolean moveToFolder(Long userId, List<Long> favoriteIds, Long targetFolderId);

    int getUserFavoriteCount(Long userId);

    boolean checkFavoriteExists(Long userId, Long productId);
}
