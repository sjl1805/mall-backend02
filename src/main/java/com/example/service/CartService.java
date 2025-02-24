package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.Cart;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * @author 31815
 * @description 针对表【cart(购物车表)】的数据库操作Service
 * @createDate 2025-02-24 12:04:25
 */
public interface CartService extends IService<Cart> {

    /**
     * 根据用户ID查询购物车
     * @param userId 用户ID
     * @return 购物车列表
     */
    List<Cart> selectByUserId(Long userId);

    /**
     * 分页查询购物车
     * @param page 分页信息
     * @return 购物车列表
     */
    IPage<Cart> selectPage(IPage<Cart> page);

    /**
     * 根据ID查询购物车
     * @param id 购物车ID
     * @return 购物车信息
     */
    Cart selectById(Long id);

    /**
     * 新增购物车
     * @param cart 购物车信息
     * @return 插入结果
     */
    boolean insertCart(Cart cart);

    /**
     * 更新购物车信息
     * @param cart 购物车信息
     * @return 更新结果
     */
    boolean updateCart(Cart cart);

    /**
     * 根据ID删除购物车
     * @param id 购物车ID
     * @return 删除结果
     */
    boolean deleteCart(Long id);
}
