package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.UserAddress;

import java.util.List;

/**
 * 用户收货地址服务接口
 */
public interface UserAddressService extends IService<UserAddress> {

    /**
     * 获取用户所有收货地址
     *
     * @param userId 用户ID
     * @return 地址列表
     */
    List<UserAddress> getUserAddressList(Long userId);

    /**
     * 获取用户默认收货地址
     *
     * @param userId 用户ID
     * @return 默认地址，如果没有则返回null
     */
    UserAddress getDefaultAddress(Long userId);

    /**
     * 添加收货地址
     *
     * @param address 收货地址信息
     * @return 添加的地址
     */
    UserAddress addUserAddress(UserAddress address);

    /**
     * 更新收货地址
     *
     * @param address 收货地址信息
     * @return 是否更新成功
     */
    boolean updateUserAddress(UserAddress address);

    /**
     * 删除收货地址
     *
     * @param addressId 地址ID
     * @param userId 用户ID
     * @return 是否删除成功
     */
    boolean deleteUserAddress(Long addressId, Long userId);

    /**
     * 设置默认收货地址
     *
     * @param addressId 地址ID
     * @param userId 用户ID
     * @return 是否设置成功
     */
    boolean setDefaultAddress(Long addressId, Long userId);

    /**
     * 获取用户收货地址数量
     *
     * @param userId 用户ID
     * @return 地址数量
     */
    int getUserAddressCount(Long userId);
    
    /**
     * 获取收货地址详情
     *
     * @param addressId 地址ID
     * @param userId 用户ID
     * @return 地址详情
     */
    UserAddress getAddressDetail(Long addressId, Long userId);

    /**
     * 检查用户是否有默认地址
     *
     * @param userId 用户ID
     * @return 是否有默认地址
     */
    boolean hasDefaultAddress(Long userId);
    
    /**
     * 批量删除收货地址
     *
     * @param addressIds 地址ID列表
     * @param userId 用户ID
     * @return 是否删除成功
     */
    boolean batchDeleteUserAddresses(List<Long> addressIds, Long userId);
} 