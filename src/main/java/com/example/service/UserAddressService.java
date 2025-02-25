package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.UserAddress;

import java.util.List;

/**
 * 用户地址服务接口
 */
public interface UserAddressService extends IService<UserAddress> {
    
    /**
     * 获取用户地址列表
     *
     * @param userId 用户ID
     * @return 地址列表
     */
    List<UserAddress> getUserAddresses(Long userId);
    
    /**
     * 获取用户默认地址
     *
     * @param userId 用户ID
     * @return 默认地址
     */
    UserAddress getDefaultAddress(Long userId);
    
    /**
     * 添加地址
     *
     * @param address 地址信息
     * @return 添加成功的地址
     */
    UserAddress addAddress(UserAddress address);
    
    /**
     * 更新地址
     *
     * @param address 地址信息
     * @return 是否更新成功
     */
    boolean updateAddress(UserAddress address);
    
    /**
     * 设置默认地址
     *
     * @param userId 用户ID
     * @param addressId 地址ID
     * @return 是否设置成功
     */
    boolean setDefaultAddress(Long userId, Long addressId);
    
    /**
     * 删除地址
     *
     * @param userId 用户ID
     * @param addressId 地址ID
     * @return 是否删除成功
     */
    boolean deleteAddress(Long userId, Long addressId);
} 