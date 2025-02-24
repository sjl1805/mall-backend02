package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
     * 根据用户ID查询收藏夹
     * @param userId 用户ID
     * @return 收藏夹列表
     */
    List<FavoriteFolder> selectByUserId(@Param("userId") Long userId);

    /**
     * 分页查询收藏夹
     * @param page 分页信息
     * @return 收藏夹列表
     */
    IPage<FavoriteFolder> selectPage(IPage<FavoriteFolder> page);

    /**
     * 根据ID查询收藏夹
     * @param id 收藏夹ID
     * @return 收藏夹信息
     */
    FavoriteFolder selectById(@Param("id") Long id);

    /**
     * 插入新收藏夹
     * @param favoriteFolder 收藏夹信息
     * @return 插入结果
     */
    int insertFavoriteFolder(FavoriteFolder favoriteFolder);

    /**
     * 更新收藏夹信息
     * @param favoriteFolder 收藏夹信息
     * @return 更新结果
     */
    int updateFavoriteFolder(FavoriteFolder favoriteFolder);

    /**
     * 根据ID删除收藏夹
     * @param id 收藏夹ID
     * @return 删除结果
     */
    int deleteFavoriteFolder(@Param("id") Long id);
}




