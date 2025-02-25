package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.Cart;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author 31815
 * @description 针对表【cart(购物车表)】的数据库操作Service
 * @createDate 2025-02-24 12:04:25
 */
public interface CartService extends IService<Cart> {

    /**
     * 根据用户ID查询购物车
     *
     * @param userId 用户ID
     * @return 购物车列表
     */
    List<Cart> selectByUserId(Long userId);

    /**
     * 分页查询购物车
     *
     * @param page 分页信息
     * @return 购物车列表
     */
    IPage<Cart> selectPage(IPage<Cart> page, Long userId);

    /**
     * 根据ID查询购物车
     *
     * @param id 购物车ID
     * @return 购物车信息
     */
    Cart selectById(Long id);

    /**
     * 新增购物车
     *
     * @param cart 购物车信息
     * @return 插入结果
     */
    boolean insertCart(Cart cart);

    /**
     * 更新购物车信息
     *
     * @param cart 购物车信息
     * @return 更新结果
     */
    boolean updateCart(Cart cart);

    /**
     * 根据ID删除购物车
     *
     * @param id 购物车ID
     * @return 删除结果
     */
    boolean deleteCart(Long id);

    /**
     * 根据用户ID和商品ID查询购物车商品
     *
     * @param userId    用户ID
     * @param productId 商品ID
     * @return 购物车商品
     */
    Cart selectByUserIdAndProductId(Long userId, Long productId);

    /**
     * 获取购物车商品详情（包含商品信息）
     *
     * @param userId 用户ID
     * @return 购物车商品详情
     */
    List<Map<String, Object>> getCartDetail(Long userId);

    /**
     * 更新购物车商品数量
     *
     * @param id       购物车ID
     * @param quantity 数量
     * @return 更新结果
     */
    boolean updateQuantity(Long id, Integer quantity);

    /**
     * 更新购物车商品选中状态
     *
     * @param id      购物车ID
     * @param checked 选中状态
     * @return 更新结果
     */
    boolean updateChecked(Long id, Integer checked);

    /**
     * 全选/取消全选购物车商品
     *
     * @param userId  用户ID
     * @param checked 选中状态
     * @return 更新结果
     */
    boolean updateAllChecked(Long userId, Integer checked);

    /**
     * 查询用户选中的购物车商品
     *
     * @param userId 用户ID
     * @return 选中的购物车商品
     */
    List<Cart> selectCheckedByUserId(Long userId);

    /**
     * 获取选中商品详情
     *
     * @param userId 用户ID
     * @return 选中商品详情
     */
    List<Map<String, Object>> getCheckedCartDetail(Long userId);

    /**
     * 清空用户购物车
     *
     * @param userId 用户ID
     * @return 清空结果
     */
    boolean clearCart(Long userId);

    /**
     * 删除选中的购物车商品
     *
     * @param userId 用户ID
     * @return 删除结果
     */
    boolean deleteChecked(Long userId);

    /**
     * 批量删除购物车商品
     *
     * @param ids 购物车ID列表
     * @return 删除结果
     */
    boolean batchDelete(List<Long> ids);

    /**
     * 统计用户购物车商品数量
     *
     * @param userId 用户ID
     * @return 商品数量
     */
    int countByUserId(Long userId);

    /**
     * 检查并添加商品到购物车
     * 如果购物车中已存在该商品，则更新数量；否则添加新记录
     *
     * @param userId    用户ID
     * @param productId 商品ID
     * @param quantity  数量
     * @return 添加/更新结果
     */
    boolean addOrUpdateCart(Long userId, Long productId, Integer quantity);

    /**
     * 计算购物车选中商品价格
     *
     * @param userId 用户ID
     * @return 总价
     */
    BigDecimal calculateCheckedAmount(Long userId);
}
