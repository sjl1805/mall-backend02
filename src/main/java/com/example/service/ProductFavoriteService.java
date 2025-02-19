package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.dto.favorite.ProductFavoriteDTO;
import com.example.model.dto.favorite.ProductFavoritePageDTO;
import com.example.model.entity.ProductFavorite;

import java.util.List;

/**
 * 商品收藏服务接口
 * 
 * @author 31815
 * @description 提供商品收藏管理功能，包含：
 *              1. 收藏/取消收藏操作
 *              2. 收藏夹转移
 *              3. 分页查询与统计
 * @createDate 2025-02-18 23:44:15
 */
public interface ProductFavoriteService extends IService<ProductFavorite> {

    /**
     * 添加收藏（带权限校验）
     * @param userId 用户ID
     * @param favoriteDTO 收藏信息，包含：
     *                    - productId: 商品ID（必须）
     *                    - folderId: 收藏夹ID（必须）
     * @return 操作是否成功
     * @throws com.example.exception.BusinessException 当商品已收藏时抛出FAVORITE_ALREADY_EXISTS
     */
    boolean addFavorite(Long userId, ProductFavoriteDTO favoriteDTO);

    /**
     * 移除收藏（级联更新）
     * @param userId 用户ID
     * @param favoriteId 收藏记录ID
     * @return 操作是否成功
     * @throws com.example.exception.BusinessException 当权限不足时抛出FAVORITE_ACCESS_DENIED
     */
    boolean removeFavorite(Long userId, Long favoriteId);

    /**
     * 分页查询收藏列表
     * @param userId 用户ID
     * @param folderId 收藏夹ID（0表示全部）
     * @param queryDTO 分页参数：
     *                 - page: 当前页
     *                 - size: 每页数量
     *                 - keyword: 商品名称搜索
     * @return 分页结果（包含商品基本信息）
     * @implNote 结果缓存优化，有效期10分钟
     */
    IPage<ProductFavorite> listFavorites(Long userId, Long folderId, ProductFavoritePageDTO queryDTO);

    /**
     * 批量转移收藏夹
     * @param userId 用户ID
     * @param favoriteIds 收藏记录ID列表
     * @param targetFolderId 目标收藏夹ID
     * @return 转移成功的数量
     * @throws com.example.exception.BusinessException 当目标收藏夹不存在时抛出
     */
    boolean moveToFolder(Long userId, List<Long> favoriteIds, Long targetFolderId);

    /**
     * 获取用户收藏总数
     * @param userId 用户ID
     * @return 收藏商品总数
     * @implNote 独立缓存计数器，避免全量查询
     */
    int getUserFavoriteCount(Long userId);

    /**
     * 检查是否已收藏
     * @param userId 用户ID
     * @param productId 商品ID
     * @return 是否已收藏
     * @implNote 用于商品详情页状态展示
     */
    boolean checkFavoriteExists(Long userId, Long productId);
}
