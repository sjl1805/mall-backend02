package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.model.entity.ProductFavorite;
import com.example.model.query.FavoriteQuery;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
* @author 31815
* @description 针对表【product_favorite(商品收藏表)】的数据库操作Mapper
* @createDate 2025-02-18 23:44:15
* @Entity model.entity.ProductFavorite
*/
public interface ProductFavoriteMapper extends BaseMapper<ProductFavorite> {

    /**
     * 分页查询收藏列表
     * @param page 分页参数
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<ProductFavorite> selectFavoritePage(IPage<ProductFavorite> page,
                                             @Param("query") FavoriteQuery query);

    /**
     * 统计用户收藏数量
     * @param userId 用户ID
     * @return 收藏数量
     */
    int countUserFavorites(@Param("userId") Long userId);

    /**
     * 检查是否已收藏
     * @param userId 用户ID
     * @param productId 商品ID
     * @return 存在数量
     */
    int checkFavoriteExists(@Param("userId") Long userId,
                          @Param("productId") Long productId);

    /**
     * 批量删除收藏
     * @param ids 收藏ID列表
     * @return 删除数量
     */
    int batchDelete(@Param("ids") List<Long> ids);

    /**
     * 转移收藏夹
     * @param fromFolderId 原收藏夹ID
     * @param toFolderId 目标收藏夹ID
     * @return 影响行数
     */
    int moveToFolder(@Param("fromFolderId") Long fromFolderId,
                   @Param("toFolderId") Long toFolderId);
}




