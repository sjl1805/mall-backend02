package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.dto.FavoriteFolderDTO;
import com.example.model.dto.PageDTO;
import com.example.model.entity.FavoriteFolder;

import java.util.List;

/**
 * 收藏夹服务接口
 * 
 * @author 31815
 * @description 提供用户收藏夹管理功能，包含：
 *              1. 收藏夹的创建与维护
 *              2. 公开状态管理
 *              3. 收藏项数量管理
 *              4. 分页查询与排序
 * @createDate 2025-02-18 23:44:24
 */
public interface FavoriteFolderService extends IService<FavoriteFolder> {

    /**
     * 创建收藏夹（带唯一性校验）
     * @param userId 用户ID
     * @param folderDTO 收藏夹信息，包含：
     *                  - name: 收藏夹名称（必须，同一用户下唯一）
     *                  - description: 描述信息
     *                  - isPublic: 是否公开（默认私有）
     * @return 操作是否成功
     * @throws com.example.exception.BusinessException 当名称重复时抛出FOLDER_NAME_EXISTS
     */
    boolean createFolder(Long userId, FavoriteFolderDTO folderDTO);

    /**
     * 获取用户收藏夹列表
     * @param userId 用户ID
     * @return 按排序值升序排列的收藏夹列表
     * @implNote 结果缓存优化，有效期15分钟
     */
    List<FavoriteFolderDTO> getUserFolders(Long userId);

    /**
     * 更新收藏夹信息
     * @param userId 用户ID
     * @param folderId 目标收藏夹ID
     * @param folderDTO 更新后的收藏夹信息
     * @return 操作是否成功
     * @throws com.example.exception.BusinessException 当名称重复或权限不足时抛出
     */
    boolean updateFolder(Long userId, Long folderId, FavoriteFolderDTO folderDTO);

    /**
     * 更新收藏夹公开状态
     * @param userId 用户ID
     * @param folderId 目标收藏夹ID
     * @param isPublic 新状态（1-公开，0-私有）
     * @return 操作是否成功
     * @throws com.example.exception.BusinessException 当权限不足时抛出
     */
    boolean updatePublicStatus(Long userId, Long folderId, Integer isPublic);

    /**
     * 调整收藏夹排序
     * @param userId 用户ID
     * @param folderId 目标收藏夹ID
     * @param newSort 新排序值
     * @return 操作是否成功
     * @implNote 采用直接指定排序值方式，需处理排序冲突
     */
    boolean updateSort(Long userId, Long folderId, Integer newSort);

    /**
     * 删除收藏夹（逻辑删除）
     * @param userId 用户ID
     * @param folderId 目标收藏夹ID
     * @return 操作是否成功
     * @throws com.example.exception.BusinessException 当收藏夹非空时抛出
     */
    boolean deleteFolder(Long userId, Long folderId);

    /**
     * 分页查询公开收藏夹
     * @param queryDTO 分页参数：
     *                 - page: 当前页
     *                 - size: 每页数量
     *                 - keyword: 搜索关键词
     * @return 分页结果（包含收藏夹基本信息）
     * @implNote 结果缓存优化，有效期5分钟
     */
    IPage<FavoriteFolderDTO> listPublicFolders(PageDTO<FavoriteFolderDTO> queryDTO);

    /**
     * 更新收藏项数量（原子操作）
     * @param userId 用户ID
     * @param folderId 目标收藏夹ID
     * @param delta 数量变化值（+1/-1）
     * @return 操作是否成功
     * @implNote 用于收藏/取消收藏时的数量同步
     */
    boolean updateItemCount(Long userId, Long folderId, Integer delta);

    /**
     * 验证收藏夹所有权
     * @param userId 用户ID
     * @param folderId 目标收藏夹ID
     * @return 是否拥有操作权限
     * @implNote 用于业务层权限校验
     */
    boolean checkFolderOwnership(Long userId, Long folderId);
}
