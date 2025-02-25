package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.UserCouponMapper;
import com.example.model.entity.UserCoupon;
import com.example.service.UserCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户优惠券服务实现类
 * 
 * 该类实现了用户优惠券相关的业务逻辑，包括优惠券的领取、使用、查询等功能。
 * 使用了Spring缓存机制对用户优惠券信息进行缓存，提高查询效率。
 * 使用MyBatis-Plus的ServiceImpl简化数据访问操作。
 * 
 * @author 31815
 * @description 针对表【user_coupon(用户优惠券表)】的数据库操作Service实现
 * @createDate 2025-02-24 12:03:44
 */
@Service
@CacheConfig(cacheNames = "userCoupons") // 指定该服务类的缓存名称为"userCoupons"
public class UserCouponServiceImpl extends ServiceImpl<UserCouponMapper, UserCoupon>
        implements UserCouponService {

    @Autowired
    private UserCouponMapper userCouponMapper;

    /**
     * 根据用户ID查询用户优惠券列表
     * 
     * 该方法从缓存或数据库获取指定用户的所有优惠券信息
     *
     * @param userId 用户ID
     * @return 用户优惠券列表
     */
    @Override
    @Cacheable(value = "userCoupons", key = "#userId") // 缓存用户优惠券信息，提高查询效率
    public List<UserCoupon> selectByUserId(Long userId) {
        return userCouponMapper.selectByUserId(userId);
    }

    /**
     * 添加用户优惠券
     * 
     * 该方法用于用户领取优惠券时，向用户优惠券表中插入记录，
     * 并清除相关用户的优惠券缓存，确保数据一致性
     *
     * @param userCoupon 用户优惠券实体
     * @return 添加成功返回true，失败返回false
     */
    @Override
    @Transactional
    @CacheEvict(value = "userCoupons", key = "#userCoupon.userId") // 清除用户优惠券缓存
    public boolean insertUserCoupon(UserCoupon userCoupon) {
        return userCouponMapper.insert(userCoupon) > 0;
    }

    /**
     * 更新用户优惠券
     * 
     * 该方法用于更新用户优惠券信息，比如标记优惠券为已使用状态，
     * 并清除相关用户的优惠券缓存，确保数据一致性
     *
     * @param userCoupon 用户优惠券实体
     * @return 更新成功返回true，失败返回false
     */
    @Override
    @Transactional
    @CacheEvict(value = "userCoupons", key = "#userCoupon.userId") // 清除用户优惠券缓存
    public boolean updateUserCoupon(UserCoupon userCoupon) {
        return userCouponMapper.updateById(userCoupon) > 0;
    }

    /**
     * 删除用户优惠券
     * 
     * 该方法用于删除用户优惠券记录，
     * 并清除相关缓存，确保数据一致性
     *
     * @param id 用户优惠券ID
     * @return 删除成功返回true，失败返回false
     */
    @Override
    @Transactional
    @CacheEvict(value = "userCoupons", key = "#id") // 清除被删除优惠券的缓存
    public boolean deleteUserCoupon(Long id) {
        return userCouponMapper.deleteById(id) > 0;
    }

    /**
     * 分页查询用户优惠券
     *
     * @param page 分页参数
     * @return 用户优惠券分页数据
     */
    @Override
    public IPage<UserCoupon> selectPage(IPage<UserCoupon> page) {
        return userCouponMapper.selectPage(page, null);
    }

    /**
     * 根据ID查询用户优惠券
     *
     * @param id 用户优惠券ID
     * @return 用户优惠券实体
     */
    @Override
    public UserCoupon selectById(Long id) {
        return userCouponMapper.selectById(id);
    }

    /**
     * 根据用户ID和状态查询用户优惠券列表
     * 
     * 该方法用于查询指定用户在特定状态下的优惠券，
     * 如未使用的优惠券、已使用的优惠券、已过期的优惠券等
     *
     * @param userId 用户ID
     * @param status 优惠券状态：0-未使用 1-已使用 2-已过期
     * @return 符合条件的用户优惠券列表
     */
    @Override
    public List<UserCoupon> selectByUserIdAndStatus(Long userId, Integer status) {
        return userCouponMapper.selectByUserIdAndStatus(userId, status);
    }
}




