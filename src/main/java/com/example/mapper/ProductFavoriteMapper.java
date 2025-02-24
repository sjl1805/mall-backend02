package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.entity.ProductFavorite;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * @author 31815
 * @description 针对表【product_favorite(商品收藏表)】的数据库操作Mapper
 * @createDate 2025-02-24 12:04:07
 * @Entity model.entity.ProductFavorite
 */
public interface ProductFavoriteMapper extends BaseMapper<ProductFavorite> {

    /**
     * 添加/更新收藏记录
     * @param userId 用户ID
     * @param productId 商品ID
     * @param folderId 收藏夹ID
     * @return 影响行数
     */
    int insertOrUpdateFavorite(@Param("userId") Long userId,
                              @Param("productId") Long productId,
                              @Param("folderId") Long folderId);

    /**
     * 批量取消收藏
     * @param userId 用户ID
     * @param productIds 商品ID列表
     * @return 删除行数
     */
    int deleteBatchFavorites(@Param("userId") Long userId,
                           @Param("productIds") List<Long> productIds);

    /**
     * 统计用户收藏数量
     * @param userId 用户ID
     * @param folderId 收藏夹ID（可选）
     * @return 收藏数量
     */
    int countUserFavorites(@Param("userId") Long userId,
                         @Param("folderId") Long folderId);

    /**
     * 查询用户收藏列表（带商品信息）
     * @param userId 用户ID
     * @param query 查询条件
     * @return 收藏列表
     */
    List<ProductFavorite> selectUserFavorites(@Param("userId") Long userId,
                                            @Param("query") FavoriteQuery query);

    /**
     * 检查是否已收藏
     * @param userId 用户ID
     * @param productId 商品ID
     * @return 是否存在
     */
    boolean checkFavoriteExists(@Param("userId") Long userId,
                                @Param("productId") Long productId);

    /**
     * 移动收藏到其他收藏夹
     * @param userId 用户ID
     * @param productId 商品ID
     * @param targetFolderId 目标收藏夹ID
     * @return 影响行数
     */
    int moveToFolder(@Param("userId") Long userId,
                    @Param("productId") Long productId,
                    @Param("targetFolderId") Long targetFolderId);

    class FavoriteQuery {
        private Long folderId;
    }
}




