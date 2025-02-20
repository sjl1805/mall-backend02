package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.UserAddress;
import com.example.model.dto.users.UserAddressDTO;

import java.util.List;

/**
 * 用户地址服务接口
 * 
 * @author 31815
 * @description 提供用户收货地址管理功能，包含：
 *              1. 地址的增删改查
 *              2. 默认地址管理
 *              3. 地址有效性校验
 * @createDate 2025-02-18 23:43:56
 */
public interface UserAddressService extends IService<UserAddress> {

    /**
     * 设置默认地址（带权限校验）
     * @param userId 用户ID
     * @param addressId 地址ID
     * @return 操作是否成功
     * @throws com.example.exception.BusinessException 当地址不属于用户时抛出
     */
    boolean setDefaultAddress(Long userId, Long addressId);

    /**
     * 获取默认地址（带缓存）
     * @param userId 用户ID
     * @return 默认地址信息
     * @implNote 结果缓存优化，有效期30分钟
     */
    UserAddressDTO getDefaultAddress(Long userId);

    /**
     * 添加地址（自动设置默认）
     * @param address 地址信息
     * @return 操作是否成功
     * @implNote 首次添加自动设为默认地址
     */
    boolean addAddress(UserAddressDTO address);

    /**
     * 更新地址（带权限校验）
     * @param userId 用户ID
     * @param address 地址信息（必须包含ID）
     * @return 操作是否成功
     * @throws com.example.exception.BusinessException 当地址不属于用户时抛出
     */
    boolean updateAddress(Long userId, UserAddressDTO address);

    /**
     * 删除地址（带权限校验）
     * @param userId 用户ID
     * @param addressId 地址ID
     * @return 操作是否成功
     * @throws com.example.exception.BusinessException 当地址不属于用户时抛出
     */
    boolean deleteAddress(Long userId, Long addressId);

    /**
     * 获取用户地址列表（带缓存）
     * @param userId 用户ID
     * @return 地址列表（默认地址优先）
     * @implNote 结果缓存优化，有效期1小时
     */
    List<UserAddressDTO> listUserAddresses(Long userId);

    /**
     * 校验地址有效性
     * @param addressId 地址ID
     * @return 是否有效地址（包含完整行政区划信息）
     */
    boolean validateAddress(Long addressId);
}
