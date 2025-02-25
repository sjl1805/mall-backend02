package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.model.entity.Cart;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author 31815
 * @description 针对表【cart(购物车表)】的数据库操作Mapper
 * @createDate 2025-02-24 12:04:25
 * @Entity model.entity.Cart
 */
@Mapper
public interface CartMapper extends BaseMapper<Cart> {

    /**
     * 根据用户ID查询购物车商品
     *
     * @param userId 用户ID
     * @return 购物车商品列表
     */
    List<Cart> selectByUserId(@Param("userId") Long userId);

    /**
     * 分页查询购物车商品
     *
     * @param page   分页信息
     * @param userId 用户ID
     * @return 购物车商品列表
     */
    IPage<Cart> selectPage(IPage<Cart> page, @Param("userId") Long userId);

    /**
     * 根据ID查询购物车商品
     *
     * @param id 购物车商品ID
     * @return 购物车商品信息
     */
    Cart selectById(@Param("id") Long id);

    /**
     * 插入新购物车商品（处理唯一约束）
     *
     * @param cart 购物车商品信息
     * @return 插入结果
     */
    int insertOrUpdateCart(Cart cart);

    /**
     * 更新购物车商品信息
     *
     * @param cart 购物车商品信息
     * @return 更新结果
     */
    int updateCart(Cart cart);

    /**
     * 根据ID删除购物车商品
     *
     * @param id 购物车商品ID
     * @return 删除结果
     */
    int deleteCart(@Param("id") Long id);

    /**
     * 根据用户ID和商品ID查询购物车商品
     *
     * @param userId    用户ID
     * @param productId 商品ID
     * @return 购物车商品信息
     */
    Cart selectByUserIdAndProductId(
            @Param("userId") Long userId,
            @Param("productId") Long productId);

    /**
     * 查询购物车商品详情
     *
     * @param userId 用户ID
     * @return 购物车商品详情列表，以cart的id为key
     */
    @MapKey("id")
    Map<Long, Map<String, Object>> selectCartDetail(@Param("userId") Long userId);

    /**
     * 更新购物车商品数量
     *
     * @param id       购物车商品ID
     * @param quantity 新数量
     * @return 更新结果
     */
    int updateQuantity(
            @Param("id") Long id,
            @Param("quantity") Integer quantity);

    /**
     * 更新购物车商品勾选状态
     *
     * @param id      购物车商品ID
     * @param checked 勾选状态
     * @return 更新结果
     */
    int updateChecked(
            @Param("id") Long id,
            @Param("checked") Integer checked);

    /**
     * 批量更新购物车商品勾选状态
     *
     * @param userId  用户ID
     * @param checked 勾选状态
     * @return 更新结果
     */
    int updateCheckedByUserId(
            @Param("userId") Long userId,
            @Param("checked") Integer checked);

    /**
     * 清空用户购物车
     *
     * @param userId 用户ID
     * @return 删除结果
     */
    int deleteByUserId(@Param("userId") Long userId);

    /**
     * 删除用户勾选的购物车商品
     *
     * @param userId 用户ID
     * @return 删除结果
     */
    int deleteCheckedByUserId(@Param("userId") Long userId);

    /**
     * 查询用户勾选的购物车商品
     *
     * @param userId 用户ID
     * @return 勾选的购物车商品列表
     */
    List<Cart> selectCheckedByUserId(@Param("userId") Long userId);

    /**
     * 查询用户勾选的购物车商品详情
     *
     * @param userId 用户ID
     * @return 勾选的购物车商品详情列表，以cart的id为key
     */
    @MapKey("id")
    Map<Long, Map<String, Object>> selectCheckedCartDetail(@Param("userId") Long userId);

    /**
     * 批量删除购物车商品
     *
     * @param ids 购物车商品ID列表
     * @return 删除结果
     */
    int batchDelete(@Param("ids") List<Long> ids);

    /**
     * 统计用户购物车商品数量
     *
     * @param userId 用户ID
     * @return 商品数量
     */
    int countByUserId(@Param("userId") Long userId);

    /**
     * 批量更新购物车商品勾选状态
     *
     * @param ids     购物车商品ID列表
     * @param userId  用户ID
     * @param checked 勾选状态
     * @return 更新结果
     */
    int batchUpdateChecked(
            @Param("ids") List<Long> ids,
            @Param("userId") Long userId,
            @Param("checked") Integer checked);
}




