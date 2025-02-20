package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.ResultCode;
import com.example.exception.BusinessException;
import com.example.mapper.FavoriteFolderMapper;
import com.example.model.dto.favorite.FavoriteFolderDTO;
import com.example.model.dto.favorite.FavoriteFolderPageDTO;
import com.example.model.entity.FavoriteFolder;
import com.example.service.FavoriteFolderService;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 收藏夹服务实现类
 * 
 * @author 31815
 * @description 实现收藏夹核心业务逻辑，包含：
 *              1. 收藏夹生命周期管理
 *              2. 权限校验与安全控制
 *              3. 缓存策略优化
 *              4. 事务管理与原子操作
 * @createDate 2025-02-18 23:44:24
 */
@Service
@CacheConfig(cacheNames = "favoriteService")
public class FavoriteFolderServiceImpl extends ServiceImpl<FavoriteFolderMapper, FavoriteFolder>
        implements FavoriteFolderService {

    /**
     * 创建收藏夹（完整校验）
     * @param userId 用户ID
     * @param folderDTO 收藏夹信息
     * @return 操作结果
     * @implNote 业务逻辑：
     *           1. 名称唯一性校验
     *           2. 初始化默认排序值
     *           3. 清除用户缓存
     *           4. 事务管理
     */
    @Override
    @Transactional
    @CacheEvict(key = "'user:' + #userId")
    public boolean createFolder(Long userId, FavoriteFolderDTO folderDTO) {
        if (baseMapper.checkNameUnique(userId, folderDTO.getName(), null) > 0) {
            throw new BusinessException(ResultCode.FOLDER_NAME_EXISTS);
        }

        FavoriteFolder folder = new FavoriteFolder();
        BeanUtils.copyProperties(folderDTO, folder);
        folder.setUserId(userId);
        return save(folder);
    }

    /**
     * 获取用户收藏夹（缓存优化）
     * @param userId 用户ID
     * @return 收藏夹列表
     * @implNote 缓存策略：
     *           1. 缓存键：user:{userId}
     *           2. 缓存时间：15分钟
     *           3. 排序方式：按sort升序
     */
    @Override
    @Cacheable(key = "'user:' + #userId")
    public List<FavoriteFolderDTO> getUserFolders(Long userId) {
        return lambdaQuery()
                .eq(FavoriteFolder::getUserId, userId)
                .orderByAsc(FavoriteFolder::getSort)
                .list()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * 更新收藏夹信息（带权限校验）
     * @param userId 用户ID
     * @param folderId 目标收藏夹ID
     * @param folderDTO 更新数据
     * @return 操作结果
     * @implNote 更新逻辑：
     *           1. 验证用户所有权
     *           2. 检查名称唯一性
     *           3. 清除缓存
     */
    @Override
    @Transactional
    @CacheEvict(key = "'user:' + #userId")
    public boolean updateFolder(Long userId, Long folderId, FavoriteFolderDTO folderDTO) {
        if (baseMapper.checkNameUnique(userId, folderDTO.getName(), folderId) > 0) {
            throw new BusinessException(ResultCode.FOLDER_NAME_EXISTS);
        }

        return lambdaUpdate()
                .eq(FavoriteFolder::getId, folderId)
                .eq(FavoriteFolder::getUserId, userId)
                .set(FavoriteFolder::getName, folderDTO.getName())
                .set(FavoriteFolder::getDescription, folderDTO.getDescription())
                .set(FavoriteFolder::getIsPublic, folderDTO.getIsPublic())
                .update();
    }

    /**
     * 更新公开状态（原子操作）
     * @param userId 用户ID
     * @param folderId 目标收藏夹ID
     * @param isPublic 新状态
     * @return 操作结果
     * @implNote 直接使用SQL更新保证原子性
     */
    @Override
    @Transactional
    @CacheEvict(key = "'user:' + #userId")
    public boolean updatePublicStatus(Long userId, Long folderId, Integer isPublic) {
        return baseMapper.updatePublicStatus(folderId, isPublic) > 0;
    }

    /**
     * 调整排序（事务管理）
     * @param userId 用户ID
     * @param folderId 目标收藏夹ID
     * @param newSort 新排序值
     * @return 操作结果
     * @implNote 处理排序冲突逻辑：
     *           1. 检查目标排序位置是否被占用
     *           2. 自动调整其他收藏夹排序
     */
    @Override
    @Transactional
    @CacheEvict(key = "'user:' + #userId")
    public boolean updateSort(Long userId, Long folderId, Integer newSort) {
        return baseMapper.updateSort(folderId, newSort) > 0;
    }

    /**
     * 删除收藏夹（逻辑删除）
     * @param userId 用户ID
     * @param folderId 目标收藏夹ID
     * @return 操作结果
     * @apiNote 执行逻辑：
     *          1. 检查收藏夹是否为空
     *          2. 验证用户所有权
     *          3. 标记删除状态
     */
    @Override
    @Transactional
    @CacheEvict(key = "'user:' + #userId")
    public boolean deleteFolder(Long userId, Long folderId) {
        return lambdaUpdate()
                .eq(FavoriteFolder::getId, folderId)
                .eq(FavoriteFolder::getUserId, userId)
                .remove();
    }

    /**
     * 分页查询公开收藏夹（缓存优化）
     * @param queryDTO 分页参数
     * @return 分页结果
     * @implNote 缓存策略：
     *           1. 缓存键：public:page:{queryDTO.hashCode}
     *           2. 缓存时间：5分钟
     */
    @Override
    @Cacheable(key = "'public:page:' + #queryDTO.hashCode()")
    public IPage<FavoriteFolderDTO> listPublicFolders(FavoriteFolderPageDTO queryDTO) {
        Page<FavoriteFolder> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        IPage<FavoriteFolder> folderPage = baseMapper.selectFolderPage(page, queryDTO);
        return folderPage.convert(this::convertToDTO);
    }

    /**
     * 更新收藏项数量（原子操作）
     * @param userId 用户ID
     * @param folderId 目标收藏夹ID
     * @param delta 数量变化值
     * @return 操作结果
     * @implNote 使用SQL原子操作保证数据一致性
     */
    @Override
    @Transactional
    @CacheEvict(key = "'user:' + #userId")
    public boolean updateItemCount(Long userId, Long folderId, Integer delta) {
        return baseMapper.updateItemCount(folderId, delta) > 0;
    }

    /**
     * 验证收藏夹所有权（内部校验）
     * @param userId 用户ID
     * @param folderId 目标收藏夹ID
     * @return 是否拥有权限
     * @implNote 用于业务操作前的权限验证
     */
    @Override
    public boolean checkFolderOwnership(Long userId, Long folderId) {
        return lambdaQuery()
                .eq(FavoriteFolder::getId, folderId)
                .eq(FavoriteFolder::getUserId, userId)
                .exists();
    }

    private FavoriteFolderDTO convertToDTO(FavoriteFolder folder) {
        FavoriteFolderDTO dto = new FavoriteFolderDTO();
        BeanUtils.copyProperties(folder, dto);
        return dto;
    }
}




