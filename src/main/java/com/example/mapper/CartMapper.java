package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.model.entity.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 31815
 * @description 针对表【cart(购物车表)】的数据库操作Mapper
 * @createDate 2025-02-24 12:04:25
 * @Entity model.entity.Cart
 */
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
     * @param page 分页信息
     * @return 购物车商品列表
     */
    IPage<Cart> selectPage(IPage<Cart> page);

    /**
     * 根据ID查询购物车商品
     *
     * @param id 购物车商品ID
     * @return 购物车商品信息
     */
    Cart selectById(@Param("id") Long id);

    /**
     * 插入新购物车商品
     *
     * @param cart 购物车商品信息
     * @return 插入结果
     */
    int insertCart(Cart cart);

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
}




