package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.ProductFavorite;

import java.util.List;

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
}
