package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.model.entity.ProductFavorite;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author 31815
 * @description 针对表【product_favorite(商品收藏表)】的数据库操作Mapper
 * @createDate 2025-02-24 12:04:07
 * @Entity model.entity.ProductFavorite
 */
@Mapper
public interface ProductFavoriteMapper extends BaseMapper<ProductFavorite> {

    /**
     * 根据用户ID查询收藏商品
     *
     * @param userId 用户ID
     * @return 收藏商品列表
     */
    List<ProductFavorite> selectByUserId(@Param("userId") Long userId);

    /**
     * 分页查询用户收藏商品
     *
     * @param page 分页信息
     * @return 收藏商品列表
     */
    IPage<ProductFavorite> selectPage(IPage<ProductFavorite> page);

    /**
     * 根据ID查询收藏商品
     *
     * @param id 收藏商品ID
     * @return 收藏商品信息
     */
    ProductFavorite selectById(@Param("id") Long id);

    /**
     * 插入新收藏商品
     *
     * @param productFavorite 收藏商品信息
     * @return 插入结果
     */
    int insertProductFavorite(ProductFavorite productFavorite);

    /**
     * 更新收藏商品信息
     *
     * @param productFavorite 收藏商品信息
     * @return 更新结果
     */
    int updateProductFavorite(ProductFavorite productFavorite);

    /**
     * 根据ID删除收藏商品
     *
     * @param id 收藏商品ID
     * @return 删除结果
     */
    int deleteProductFavorite(@Param("id") Long id);

    /**
     * 查询用户是否已收藏商品
     *
     * @param userId    用户ID
     * @param productId 商品ID
     * @return 收藏记录，未收藏则返回null
     */
    ProductFavorite checkFavorite(
            @Param("userId") Long userId,
            @Param("productId") Long productId);

    /**
     * 根据文件夹ID查询收藏商品
     *
     * @param userId   用户ID
     * @param folderId 收藏夹ID，为null则查询默认收藏夹
     * @return 收藏商品列表
     */
    List<ProductFavorite> selectByFolder(
            @Param("userId") Long userId,
            @Param("folderId") Long folderId);

    /**
     * 查询热门收藏商品
     *
     * @param limit 限制数量
     * @return 热门收藏商品ID及收藏次数
     */
    @MapKey("product_id")
    List<Map<String, Object>> selectHotFavorites(@Param("limit") Integer limit);

    /**
     * 移动商品到指定收藏夹
     *
     * @param id       收藏ID
     * @param folderId 目标收藏夹ID
     * @return 更新结果
     */
    int moveToFolder(@Param("id") Long id, @Param("folderId") Long folderId);

    /**
     * 批量删除收藏
     *
     * @param ids 收藏ID列表
     * @return 删除结果
     */
    int batchDelete(@Param("ids") List<Long> ids);
}




