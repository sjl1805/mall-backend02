package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.example.model.dto.ProductFavoriteDTO;
import com.example.model.entity.ProductFavorite;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 商品收藏管理Mapper接口
 * 实现收藏夹管理、批量操作和收藏统计功能
 * 
 * @author 毕业设计学生
 */
public interface ProductFavoriteMapper extends BaseMapper<ProductFavorite> {

    /**
     * 分页查询收藏记录（支持多条件过滤）
     * 
     * @param page     分页参数（包含当前页、每页数量）
     * @param queryDTO 查询条件DTO（包含用户ID、商品ID等）
     * @return 分页结果（包含收藏列表和分页信息）
     */
    IPage<ProductFavorite> selectFavoritePage(IPage<ProductFavorite> page,
                                             @Param("query") ProductFavoriteDTO queryDTO);

    /**
     * 统计用户收藏总数（用于个人中心展示）
     * 
     * @param userId 用户ID（必填）
     * @return 用户的收藏商品总数
     */
    int countUserFavorites(@Param("userId") Long userId);

    /**
     * 检查收藏关系是否存在（防止重复收藏）
     * 
     * @param userId    用户ID（必填）
     * @param productId 商品ID（必填）
     * @return 存在返回1，否则返回0
     */
    int checkFavoriteExists(@Param("userId") Long userId,
                            @Param("productId") Long productId);

    /**
     * 批量删除收藏记录（支持多选操作）
     * 
     * @param ids 收藏记录ID列表（至少包含1个元素）
     * @return 成功删除的记录数
     */
    int batchDelete(@Param("ids") List<Long> ids);

    /**
     * 转移收藏到其他收藏夹（支持批量转移）
     * 
     * @param favoriteIds     要转移的收藏ID列表
     * @param targetFolderId 目标收藏夹ID
     * @return 成功转移的记录数
     */
    int moveToFolder(@Param("favoriteIds") List<Long> favoriteIds,
                     @Param("targetFolderId") Long targetFolderId);

    /**
     * 更新单个收藏的所属收藏夹
     * 
     * @param userId     用户ID（用于权限校验）
     * @param favoriteId 收藏记录ID（必填）
     * @param folderId   新收藏夹ID（0表示默认收藏夹）
     * @return 影响的行数（0表示操作失败）
     */
    @Update("UPDATE product_favorite SET folder_id = #{folderId} " +
            "WHERE id = #{favoriteId} AND user_id = #{userId}")
    int updateFolder(@Param("userId") Long userId,
                     @Param("favoriteId") Long favoriteId,
                     @Param("folderId") Long folderId);

    /**
     * 批量添加收藏记录（支持导入功能）
     * 
     * @param favorites 收藏记录DTO列表
     * @return 成功插入的记录数
     */
    int batchInsert(@Param("favorites") List<ProductFavoriteDTO> favorites);
}




