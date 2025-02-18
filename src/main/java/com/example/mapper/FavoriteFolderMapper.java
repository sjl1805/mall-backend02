package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.model.entity.FavoriteFolder;
import com.example.model.query.FavoriteFolderQuery;
import org.apache.ibatis.annotations.Param;

/**
 * 收藏夹管理Mapper接口
 * @author 毕业设计学生
 */
public interface FavoriteFolderMapper extends BaseMapper<FavoriteFolder> {

    /**
     * 分页查询收藏夹列表（带条件）
     * @param page 分页参数
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<FavoriteFolder> selectFolderPage(IPage<FavoriteFolder> page, 
                                          @Param("query") FavoriteFolderQuery query);

    /**
     * 更新收藏夹公开状态
     * @param folderId 收藏夹ID
     * @param isPublic 是否公开
     * @return 影响行数
     */
    int updatePublicStatus(@Param("folderId") Long folderId, 
                          @Param("isPublic") Integer isPublic);

    /**
     * 更新收藏夹排序
     * @param folderId 收藏夹ID
     * @param newSort 新排序值
     * @return 影响行数
     */
    int updateSort(@Param("folderId") Long folderId, 
                  @Param("newSort") Integer newSort);

    /**
     * 检查收藏夹名称唯一性
     * @param userId 用户ID
     * @param name 收藏夹名称
     * @param excludeId 排除的ID
     * @return 存在的记录数
     */
    int checkNameUnique(@Param("userId") Long userId,
                       @Param("name") String name,
                       @Param("excludeId") Long excludeId);
}




