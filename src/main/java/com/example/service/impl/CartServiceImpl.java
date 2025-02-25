package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.CartMapper;
import com.example.model.entity.Cart;
import com.example.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 购物车服务实现类
 * <p>
 * 该类实现了购物车相关的业务逻辑，包括购物车的添加、查询、更新和删除等功能。
 * 购物车是电商系统中连接浏览和下单的关键环节，直接影响用户的购买决策和转化率。
 * 购物车记录了用户选择的商品、数量、规格等信息，并提供了结算前的商品预览和价格计算功能。
 * 购物车数据需要高效读取和实时更新，以提供流畅的购物体验。
 * 使用了Spring缓存机制对购物车信息进行缓存，提高查询效率。
 * 使用MyBatis-Plus的ServiceImpl简化数据访问操作。
 *
 * @author 31815
 * @description 针对表【cart(购物车表)】的数据库操作Service实现
 * @createDate 2025-02-24 12:04:25
 */
@Service
@CacheConfig(cacheNames = "carts") // 指定该服务类的缓存名称为"carts"
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart>
        implements CartService {

    @Autowired
    private CartMapper cartMapper;

    /**
     * 根据用户ID查询购物车列表
     * <p>
     * 该方法从缓存或数据库获取指定用户的所有购物车记录，
     * 用于展示用户购物车中的商品，便于用户进行结算操作
     *
     * @param userId 用户ID
     * @return 购物车列表
     */
    @Override
    @Cacheable(value = "carts", key = "#userId") // 缓存用户购物车数据，提高查询效率
    public List<Cart> selectByUserId(Long userId) {
        return cartMapper.selectByUserId(userId);
    }

    /**
     * 分页查询购物车数据
     * <p>
     * 该方法用于后台管理系统分页查看用户购物车数据，
     * 可用于分析用户购买意向和商品受欢迎程度
     *
     * @param page 分页参数
     * @return 购物车分页数据
     */
    @Override
    public IPage<Cart> selectPage(IPage<Cart> page, Long userId) {
        return cartMapper.selectPage(page, userId);
    }

    /**
     * 根据ID查询购物车记录
     * <p>
     * 该方法从缓存或数据库获取指定ID的购物车记录，
     * 用于查看特定购物车记录的详细信息
     *
     * @param id 购物车记录ID
     * @return 购物车实体
     */
    @Override
    @Cacheable(value = "carts", key = "#id") // 缓存购物车详情，提高查询效率
    public Cart selectById(Long id) {
        return cartMapper.selectById(id);
    }

    /**
     * 添加购物车记录
     * <p>
     * 该方法用于用户将商品添加到购物车，
     * 如果用户购物车中已有相同商品，应考虑合并数量而非创建新记录，
     * 添加成功后需要清除用户购物车缓存
     *
     * @param cart 购物车实体
     * @return 添加成功返回true，失败返回false
     */
    @Override
    @Transactional
    @CacheEvict(value = "carts", key = "#cart.userId") // 清除用户购物车缓存
    public boolean insertCart(Cart cart) {
        return cartMapper.insert(cart) > 0;
    }

    /**
     * 更新购物车记录
     * <p>
     * 该方法用于更新购物车中商品的数量、规格等信息，
     * 是用户调整购物意向的重要功能，需要实时反映，
     * 并清除相关缓存，确保数据一致性
     *
     * @param cart 购物车实体
     * @return 更新成功返回true，失败返回false
     */
    @Override
    @Transactional
    @CacheEvict(value = "carts", key = "#cart.userId") // 清除用户购物车缓存
    public boolean updateCart(Cart cart) {
        return cartMapper.updateById(cart) > 0;
    }

    /**
     * 删除购物车记录
     * <p>
     * 该方法用于用户从购物车中移除商品，
     * 或在下单完成后清理已购买的商品，
     * 并清除相关缓存，确保数据一致性
     *
     * @param id 购物车记录ID
     * @return 删除成功返回true，失败返回false
     */
    @Override
    @Transactional
    @CacheEvict(value = "carts", key = "#id") // 清除被删除购物车记录的缓存
    public boolean deleteCart(Long id) {
        return cartMapper.deleteById(id) > 0;
    }

    /**
     * 根据用户ID和商品ID查询购物车商品
     * <p>
     * 该方法用于检查用户购物车中是否已有特定商品，
     * 添加商品到购物车前需要检查，避免重复添加
     *
     * @param userId    用户ID
     * @param productId 商品ID
     * @return 购物车商品，不存在返回null
     */
    @Override
    @Cacheable(value = "carts", key = "#userId + '_' + #productId")
    public Cart selectByUserIdAndProductId(Long userId, Long productId) {
        return cartMapper.selectByUserIdAndProductId(userId, productId);
    }

    /**
     * 获取购物车商品详情
     * <p>
     * 该方法关联商品表获取详细信息，包括商品名称、价格、图片等，
     * 用于购物车页面展示完整的商品信息
     *
     * @param userId 用户ID
     * @return 购物车商品详情列表
     */
    @Override
    @Cacheable(value = "carts", key = "'detail_' + #userId")
    public List<Map<String, Object>> getCartDetail(Long userId) {
        Map<Long, Map<String, Object>> cartMap = cartMapper.selectCartDetail(userId);
        return new ArrayList<>(cartMap.values());
    }

    /**
     * 更新购物车商品数量
     * <p>
     * 该方法用于用户调整购物车中商品的购买数量，
     * 需要在更新后清除相关缓存，确保数据一致性
     *
     * @param id       购物车ID
     * @param quantity 数量
     * @return 更新结果
     */
    @Override
    @Transactional
    @CacheEvict(value = "carts", allEntries = true)
    public boolean updateQuantity(Long id, Integer quantity) {
        // 数量不能小于1
        if (quantity == null || quantity < 1) {
            return false;
        }
        return cartMapper.updateQuantity(id, quantity) > 0;
    }

    /**
     * 更新购物车商品选中状态
     * <p>
     * 该方法用于用户勾选或取消勾选购物车中的商品，
     * 影响结算时的商品范围和总价计算
     *
     * @param id      购物车ID
     * @param checked 选中状态
     * @return 更新结果
     */
    @Override
    @Transactional
    @CacheEvict(value = "carts", allEntries = true)
    public boolean updateChecked(Long id, Integer checked) {
        return cartMapper.updateChecked(id, checked) > 0;
    }

    /**
     * 全选/取消全选购物车商品
     * <p>
     * 该方法用于一键勾选或取消勾选用户购物车中的所有商品，
     * 提供批量操作的便捷性
     *
     * @param userId  用户ID
     * @param checked 选中状态
     * @return 更新结果
     */
    @Override
    @Transactional
    @CacheEvict(value = "carts", key = "#userId")
    public boolean updateAllChecked(Long userId, Integer checked) {
        return cartMapper.updateCheckedByUserId(userId, checked) > 0;
    }

    /**
     * 查询用户选中的购物车商品
     * <p>
     * 该方法获取用户购物车中已勾选的商品，
     * 用于订单创建前的商品确认和清单展示
     *
     * @param userId 用户ID
     * @return 选中的购物车商品列表
     */
    @Override
    @Cacheable(value = "carts", key = "'checked_' + #userId")
    public List<Cart> selectCheckedByUserId(Long userId) {
        return cartMapper.selectCheckedByUserId(userId);
    }

    /**
     * 获取选中商品详情
     * <p>
     * 该方法获取用户购物车中已勾选商品的详细信息，
     * 包括商品名称、价格、图片等，用于订单确认页展示
     *
     * @param userId 用户ID
     * @return 选中商品详情列表
     */
    @Override
    @Cacheable(value = "carts", key = "'checked_detail_' + #userId")
    public List<Map<String, Object>> getCheckedCartDetail(Long userId) {
        Map<Long, Map<String, Object>> cartMap = cartMapper.selectCheckedCartDetail(userId);
        return new ArrayList<>(cartMap.values());
    }

    /**
     * 清空用户购物车
     * <p>
     * 该方法用于清空用户的整个购物车，
     * 适用于批量删除、下单后清理等场景
     *
     * @param userId 用户ID
     * @return 清空结果
     */
    @Override
    @Transactional
    @CacheEvict(value = "carts", key = "#userId")
    public boolean clearCart(Long userId) {
        return cartMapper.deleteByUserId(userId) > 0;
    }

    /**
     * 删除选中的购物车商品
     * <p>
     * 该方法用于删除用户购物车中已勾选的商品，
     * 通常在提交订单后调用，清理已购买的商品
     *
     * @param userId 用户ID
     * @return 删除结果
     */
    @Override
    @Transactional
    @CacheEvict(value = "carts", key = "#userId")
    public boolean deleteChecked(Long userId) {
        return cartMapper.deleteCheckedByUserId(userId) > 0;
    }

    /**
     * 批量删除购物车商品
     * <p>
     * 该方法用于批量删除指定的购物车商品，
     * 适用于用户选择性删除多个商品的场景
     *
     * @param ids 购物车ID列表
     * @return 删除结果
     */
    @Override
    @Transactional
    @CacheEvict(value = "carts", allEntries = true)
    public boolean batchDelete(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        return cartMapper.batchDelete(ids) > 0;
    }

    /**
     * 统计用户购物车商品数量
     * <p>
     * 该方法用于获取用户购物车中的商品总数，
     * 通常用于头部导航栏的购物车图标旁显示数量
     *
     * @param userId 用户ID
     * @return 商品数量
     */
    @Override
    @Cacheable(value = "carts", key = "'count_' + #userId")
    public int countByUserId(Long userId) {
        return cartMapper.countByUserId(userId);
    }

    /**
     * 检查并添加商品到购物车
     * <p>
     * 该方法先检查购物车中是否已有该商品，
     * 有则更新数量，无则添加新记录，避免重复添加相同商品
     *
     * @param userId    用户ID
     * @param productId 商品ID
     * @param quantity  数量
     * @return 添加/更新结果
     */
    @Override
    @Transactional
    @CacheEvict(value = "carts", allEntries = true)
    public boolean addOrUpdateCart(Long userId, Long productId, Integer quantity) {
        if (quantity == null || quantity < 1) {
            return false;
        }

        // 查询是否已存在
        Cart existCart = selectByUserIdAndProductId(userId, productId);
        if (existCart != null) {
            // 更新数量
            existCart.setQuantity(existCart.getQuantity() + quantity);
            return updateCart(existCart);
        } else {
            // 新增记录
            Cart cart = new Cart();
            cart.setUserId(userId);
            cart.setProductId(productId);
            cart.setQuantity(quantity);
            cart.setChecked(1); // 默认选中
            return insertCart(cart);
        }
    }

    /**
     * 计算购物车选中商品价格
     * <p>
     * 该方法计算用户购物车中已勾选商品的总价，
     * 用于结算页面显示和订单创建时的金额校验
     *
     * @param userId 用户ID
     * @return 总价
     */
    @Override
    public BigDecimal calculateCheckedAmount(Long userId) {
        List<Map<String, Object>> cartItems = getCheckedCartDetail(userId);
        BigDecimal totalAmount = BigDecimal.ZERO;

        if (cartItems != null && !cartItems.isEmpty()) {
            for (Map<String, Object> item : cartItems) {
                if (item.containsKey("total_price")) {
                    BigDecimal itemPrice = new BigDecimal(item.get("total_price").toString());
                    totalAmount = totalAmount.add(itemPrice);
                }
            }
        }

        return totalAmount;
    }
}




