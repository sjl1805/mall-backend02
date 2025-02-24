package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.entity.FavoriteFolder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 31815
 * @description 针对表【favorite_folder(收藏夹表)】的数据库操作Mapper
 * @createDate 2025-02-24 12:04:16
 * @Entity model.entity.FavoriteFolder
 */
public interface FavoriteFolderMapper extends BaseMapper<FavoriteFolder> {

    /**
     * 创建收藏夹
     * @param folder 收藏夹实体
     * @return 影响行数
     */
    int insertFolder(FavoriteFolder folder);

    /**
     * 更新收藏夹信息
     * @param id 收藏夹ID
     * @param userId 用户ID
     * @param name 新名称（可选）
     * @param description 新描述（可选）
     * @param isPublic 是否公开（可选）
     * @return 影响行数
     */
    int updateFolderInfo(@Param("id") Long id,
                        @Param("userId") Long userId,
                        @Param("name") String name,
                        @Param("description") String description,
                        @Param("isPublic") Boolean isPublic);

    /**
     * 更新收藏夹项目数量
     * @param folderId 收藏夹ID
     * @return 影响行数
     */
    int updateItemCount(@Param("folderId") Long folderId);

    /**
     * 查询用户收藏夹列表
     * @param userId 用户ID
     * @return 收藏夹列表
     */
    List<FavoriteFolder> selectUserFolders(@Param("userId") Long userId);

    /**
     * 查询公开收藏夹（带用户信息）
     * @param keyword 搜索关键词（可选）
     * @param limit 返回数量
     * @return 公开收藏夹列表
     */
    List<FavoriteFolder> selectPublicFolders(@Param("keyword") String keyword,
                                           @Param("limit") int limit);

    /**
     * 调整收藏夹排序
     * @param folderId 收藏夹ID
     * @param userId 用户ID
     * @param newSort 新排序值
     * @return 影响行数
     */
    int updateSort(@Param("folderId") Long folderId,
                 @Param("userId") Long userId,
                 @Param("newSort") Integer newSort);
}




