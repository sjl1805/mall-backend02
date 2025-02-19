package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.model.dto.favorite.FavoriteFolderPageDTO;
import com.example.model.entity.FavoriteFolder;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 收藏夹管理Mapper接口
 * 实现收藏夹的创建、管理、商品收藏统计等功能
 * 
 * @author 毕业设计学生
 */
public interface FavoriteFolderMapper extends BaseMapper<FavoriteFolder> {

    /**
     * 分页查询收藏夹（支持多条件过滤和动态排序）
     * 
     * @param page     分页参数（包含当前页、每页数量）
     * @param queryDTO 查询条件DTO（包含名称、公开状态等）
     * @return 分页结果（包含收藏夹列表和分页信息）
     */
    IPage<FavoriteFolder> selectFolderPage(IPage<FavoriteFolder> page,
                                           @Param("query") FavoriteFolderPageDTO queryDTO);

    /**
     * 更新收藏夹公开状态（同时更新修改时间）
     * 
     * @param folderId 收藏夹ID（必填）
     * @param isPublic 新公开状态（0-私密 1-公开）
     * @return 影响的行数
     */
    @Update("UPDATE favorite_folder SET is_public = #{isPublic}, update_time = NOW() WHERE id = #{folderId}")
    int updatePublicStatus(@Param("folderId") Long folderId,
                           @Param("isPublic") Integer isPublic);

    /**
     * 更新收藏夹排序值（用于拖拽排序功能）
     * 
     * @param folderId 要更新的收藏夹ID
     * @param newSort  新的排序值
     * @return 影响的行数
     */
    @Update("UPDATE favorite_folder SET sort = #{newSort}, update_time = NOW() WHERE id = #{folderId}")
    int updateSort(@Param("folderId") Long folderId,
                   @Param("newSort") Integer newSort);

    /**
     * 检查收藏夹名称唯一性（同一用户下不可重复）
     * 
     * @param userId    用户ID（用于确定所属用户）
     * @param name      要检查的收藏夹名称
     * @param excludeId 需要排除的ID（更新操作时使用）
     * @return 存在重复返回大于0的值，否则返回0
     */
    int checkNameUnique(@Param("userId") Long userId,
                        @Param("name") String name,
                        @Param("excludeId") Long excludeId);

    /**
     * 更新收藏夹中的商品数量（添加/删除收藏时自动更新）
     * 
     * @param folderId 收藏夹ID
     * @param delta    数量变化值（+1或-1）
     * @return 影响的行数
     */
    @Update("UPDATE favorite_folder SET item_count = item_count + #{delta} WHERE id = #{folderId}")
    int updateItemCount(@Param("folderId") Long folderId,
                        @Param("delta") Integer delta);
}




