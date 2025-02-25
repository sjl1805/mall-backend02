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

import java.util.List;

/**
 * 购物车服务实现类
 * 
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
     * 
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
     * 
     * 该方法用于后台管理系统分页查看用户购物车数据，
     * 可用于分析用户购买意向和商品受欢迎程度
     *
     * @param page 分页参数
     * @return 购物车分页数据
     */
    @Override
    public IPage<Cart> selectPage(IPage<Cart> page) {
        return cartMapper.selectPage(page);
    }

    /**
     * 根据ID查询购物车记录
     * 
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
     * 
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
     * 
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
     * 
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
}




