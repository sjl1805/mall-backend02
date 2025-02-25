package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.ProductFavorite;

import java.util.List;
import java.util.Map;

/**
 * @author 31815
 * @description 针对表【product_favorite(商品收藏表)】的数据库操作Service
 * @createDate 2025-02-24 12:04:07
 */
public interface ProductFavoriteService extends IService<ProductFavorite> {

    /**
     * 根据用户ID查询商品收藏
     *
     * @param userId 用户ID
     * @return 商品收藏列表
     */
    List<ProductFavorite> selectByUserId(Long userId);

    /**
     * 分页查询商品收藏
     *
     * @param page 分页信息
     * @return 商品收藏列表
     */
    IPage<ProductFavorite> selectPage(IPage<ProductFavorite> page);

    /**
     * 根据ID查询商品收藏
     *
     * @param id 商品收藏ID
     * @return 商品收藏信息
     */
    ProductFavorite selectById(Long id);

    /**
     * 新增商品收藏
     *
     * @param productFavorite 商品收藏信息
     * @return 插入结果
     */
    boolean insertProductFavorite(ProductFavorite productFavorite);

    /**
     * 更新商品收藏信息
     *
     * @param productFavorite 商品收藏信息
     * @return 更新结果
     */
    boolean updateProductFavorite(ProductFavorite productFavorite);

    /**
     * 根据ID删除商品收藏
     *
     * @param id 商品收藏ID
     * @return 删除结果
     */
    boolean deleteProductFavorite(Long id);

    /**
     * 检查用户是否已收藏商品
     *
     * @param userId 用户ID
     * @param productId 商品ID
     * @return 收藏记录，未收藏则返回null
     */
    ProductFavorite checkFavorite(Long userId, Long productId);

    /**
     * 切换收藏状态（收藏/取消收藏）
     *
     * @param userId 用户ID
     * @param productId 商品ID
     * @return true-收藏成功，false-取消收藏成功
     */
    boolean toggleFavorite(Long userId, Long productId);

    /**
     * 根据收藏夹查询收藏商品
     *
     * @param userId 用户ID
     * @param folderId 收藏夹ID，为null则查询默认收藏夹
     * @return 收藏商品列表
     */
    List<ProductFavorite> selectByFolder(Long userId, Long folderId);

    /**
     * 获取用户的收藏夹列表
     *
     * @param userId 用户ID
     * @return 收藏夹列表
     */
    List<Map<String, Object>> getUserFolders(Long userId);

    /**
     * 创建收藏夹
     *
     * @param userId 用户ID
     * @param folderName 收藏夹名称
     * @return 创建结果
     */
    boolean createFolder(Long userId, String folderName);

    /**
     * 移动商品到指定收藏夹
     *
     * @param id 收藏ID
     * @param folderId 目标收藏夹ID
     * @return 移动结果
     */
    boolean moveToFolder(Long id, Long folderId);

    /**
     * 查询热门收藏商品
     *
     * @param limit 限制数量
     * @return 热门收藏商品及收藏次数
     */
    List<Map<String, Object>> getHotFavorites(Integer limit);

    /**
     * 获取带商品信息的收藏列表
     *
     * @param userId 用户ID
     * @return 收藏商品列表（包含商品信息）
     */
    List<ProductFavorite> selectWithProductInfo(Long userId);

    /**
     * 批量删除收藏
     *
     * @param ids 收藏ID列表
     * @return 删除结果
     */
    boolean batchDelete(List<Long> ids);

    /**
     * 统计用户收藏商品数量
     *
     * @param userId 用户ID
     * @return 收藏数量
     */
    int countUserFavorites(Long userId);
}
