package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.ResultCode;
import com.example.exception.BusinessException;
import com.example.mapper.ProductFavoriteMapper;
import com.example.model.dto.ProductFavoriteDTO;
import com.example.model.dto.PageDTO;
import com.example.model.entity.ProductFavorite;
import com.example.service.FavoriteFolderService;
import com.example.service.ProductFavoriteService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商品收藏服务实现类
 * 
 * @author 31815
 * @description 实现商品收藏核心业务逻辑，包含：
 *              1. 收藏操作的事务管理
 *              2. 收藏夹计数同步
 *              3. 批量操作优化
 *              4. 缓存策略应用
 * @createDate 2025-02-18 23:44:15
 */
@Service
@CacheConfig(cacheNames = "productFavoriteService")
public class ProductFavoriteServiceImpl extends ServiceImpl<ProductFavoriteMapper, ProductFavorite>
        implements ProductFavoriteService {

    //private static final Logger logger = LoggerFactory.getLogger(ProductFavoriteServiceImpl.class);

    @Autowired
    private FavoriteFolderService folderService;

    /**
     * 添加收藏（完整校验）
     * @param userId 用户ID
     * @param favoriteDTO 收藏信息
     * @return 操作结果
     * @implNote 业务逻辑：
     *           1. 验证收藏夹归属
     *           2. 检查重复收藏
     *           3. 更新收藏夹计数
     *           4. 清除用户缓存
     */
    @Override
    @Transactional
    @CacheEvict(key = "'user:' + #userId")
    public boolean addFavorite(Long userId, ProductFavoriteDTO favoriteDTO) {
        if (!folderService.checkFolderOwnership(userId, favoriteDTO.getFolderId())) {
            throw new BusinessException(ResultCode.FOLDER_ACCESS_DENIED);
        }

        if (baseMapper.checkFavoriteExists(userId, favoriteDTO.getProductId()) > 0) {
            throw new BusinessException(ResultCode.FAVORITE_ALREADY_EXISTS);
        }

        ProductFavorite favorite = new ProductFavorite();
        BeanUtils.copyProperties(favoriteDTO, favorite);
        favorite.setUserId(userId);

        boolean success = save(favorite);
        if (success) {
            folderService.updateItemCount(userId, favoriteDTO.getFolderId(), 1);
        }
        return success;
    }

    /**
     * 移除收藏（级联操作）
     * @param userId 用户ID
     * @param favoriteId 收藏记录ID
     * @return 操作结果
     * @implNote 执行逻辑：
     *           1. 验证收藏记录归属
     *           2. 删除收藏记录
     *           3. 更新收藏夹计数
     */
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

    /**
     * 分页查询收藏（缓存优化）
     * @param userId 用户ID
     * @param folderId 收藏夹ID
     * @param queryDTO 分页参数
     * @return 分页结果
     * @implNote 缓存策略：
     *           1. 缓存键：user:{userId}:folder:{folderId}
     *           2. 缓存时间：10分钟
     */
    @Override
    @Cacheable(key = "'user:' + #userId + ':folder:' + #folderId")
    public IPage<ProductFavoriteDTO> listFavorites(Long userId, Long folderId, PageDTO<ProductFavoriteDTO> queryDTO) {
        queryDTO.getQuery().setUserId(userId);
        queryDTO.getQuery().setFolderId(folderId);
        Page<ProductFavorite> page = new Page<>(queryDTO.getCurrent(), queryDTO.getSize());
        IPage<ProductFavorite> favoritePage = baseMapper.selectFavoritePage(page, queryDTO.getQuery());
        return favoritePage.convert(ProductFavoriteDTO::fromEntity);
    }

    /**
     * 批量转移收藏夹（事务管理）
     * @param userId 用户ID
     * @param favoriteIds 收藏记录ID列表
     * @param targetFolderId 目标收藏夹ID
     * @return 转移结果
     * @implNote 执行步骤：
     *           1. 验证目标收藏夹权限
     *           2. 统计原收藏夹计数
     *           3. 执行批量转移
     *           4. 同步更新收藏夹计数
     */
    @Override
    @Transactional
    @CacheEvict(key = "'user:' + #userId")
    public boolean moveToFolder(Long userId, List<Long> favoriteIds, Long targetFolderId) {
        if (!folderService.checkFolderOwnership(userId, targetFolderId)) {
            throw new BusinessException(ResultCode.FOLDER_ACCESS_DENIED);
        }

        List<ProductFavorite> favorites = listByIds(favoriteIds);
        Map<Long, Integer> folderCountMap = favorites.stream()
                .filter(f -> f.getUserId().equals(userId))
                .collect(Collectors.groupingBy(
                        ProductFavorite::getFolderId,
                        Collectors.summingInt(e -> 1)
                ));

        int affected = baseMapper.moveToFolder(favoriteIds, targetFolderId);

        folderCountMap.forEach((folderId, count) ->
                folderService.updateItemCount(userId, folderId, -count)
        );
        folderService.updateItemCount(userId, targetFolderId, affected);

        return affected > 0;
    }

    /**
     * 获取用户收藏总数（独立缓存）
     * @param userId 用户ID
     * @return 收藏总数
     * @implNote 使用独立缓存键避免全表扫描
     */
    @Override
    @Cacheable(key = "'user:' + #userId + ':count'")
    public int getUserFavoriteCount(Long userId) {
        return baseMapper.countUserFavorites(userId);
    }

    /**
     * 检查收藏状态（快速查询）
     * @param userId 用户ID
     * @param productId 商品ID
     * @return 是否已收藏
     * @implNote 使用数据库存在性查询优化性能
     */
    @Override
    public boolean checkFavoriteExists(Long userId, Long productId) {
        return baseMapper.checkFavoriteExists(userId, productId) > 0;
    }

}




