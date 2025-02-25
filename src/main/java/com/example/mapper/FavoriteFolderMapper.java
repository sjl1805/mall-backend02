package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.model.entity.FavoriteFolder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 31815
 * @description 针对表【favorite_folder(收藏夹表)】的数据库操作Mapper
 * @createDate 2025-02-24 12:04:16
 * @Entity model.entity.FavoriteFolder
 */
@Mapper
public interface FavoriteFolderMapper extends BaseMapper<FavoriteFolder> {

    /**
     * 根据用户ID查询收藏夹
     *
     * @param userId 用户ID
     * @return 收藏夹列表
     */
    List<FavoriteFolder> selectByUserId(@Param("userId") Long userId);

    /**
     * 分页查询收藏夹
     *
     * @param page 分页信息
     * @return 收藏夹列表
     */
    IPage<FavoriteFolder> selectPage(IPage<FavoriteFolder> page);

    /**
     * 根据ID查询收藏夹
     *
     * @param id 收藏夹ID
     * @return 收藏夹信息
     */
    FavoriteFolder selectById(@Param("id") Long id);

    /**
     * 插入新收藏夹
     *
     * @param favoriteFolder 收藏夹信息
     * @return 插入结果
     */
    int insertFavoriteFolder(FavoriteFolder favoriteFolder);

    /**
     * 更新收藏夹信息
     *
     * @param favoriteFolder 收藏夹信息
     * @return 更新结果
     */
    int updateFavoriteFolder(FavoriteFolder favoriteFolder);

    /**
     * 根据ID删除收藏夹
     *
     * @param id 收藏夹ID
     * @return 删除结果
     */
    int deleteFavoriteFolder(@Param("id") Long id);

    /**
     * 增加收藏夹商品数量
     *
     * @param id 收藏夹ID
     * @param count 增加数量，可为负数（减少）
     * @return 更新结果
     */
    int updateItemCount(@Param("id") Long id, @Param("count") Integer count);

    /**
     * 刷新收藏夹商品数量（根据实际收藏商品数量）
     *
     * @param id 收藏夹ID
     * @return 更新结果
     */
    int refreshItemCount(@Param("id") Long id);

    /**
     * 更新收藏夹排序
     *
     * @param id 收藏夹ID
     * @param sort 排序值
     * @return 更新结果
     */
    int updateSort(@Param("id") Long id, @Param("sort") Integer sort);

    /**
     * 获取用户收藏夹（按排序）
     *
     * @param userId 用户ID
     * @return 收藏夹列表
     */
    List<FavoriteFolder> selectByUserIdOrderBySort(@Param("userId") Long userId);

    /**
     * 查询公开收藏夹
     *
     * @param userId 用户ID
     * @return 收藏夹列表
     */
    List<FavoriteFolder> selectPublicFolders(@Param("userId") Long userId);

    /**
     * 查询其他用户的公开收藏夹
     *
     * @return 收藏夹列表
     */
    List<FavoriteFolder> selectOtherPublicFolders(@Param("userId") Long userId);

    /**
     * 按名称查询收藏夹
     *
     * @param userId 用户ID
     * @param name 收藏夹名称（模糊匹配）
     * @return 收藏夹列表
     */
    List<FavoriteFolder> selectByName(
            @Param("userId") Long userId,
            @Param("name") String name);

    /**
     * 批量删除收藏夹
     *
     * @param ids 收藏夹ID列表
     * @return 删除结果
     */
    int batchDelete(@Param("ids") List<Long> ids);

    /**
     * 批量更新收藏夹公开状态
     *
     * @param ids 收藏夹ID列表
     * @param isPublic 公开状态
     * @return 更新结果
     */
    int batchUpdatePublicStatus(
            @Param("ids") List<Long> ids,
            @Param("isPublic") Integer isPublic);
}




