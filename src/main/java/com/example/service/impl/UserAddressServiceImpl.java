package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.ResultCode;
import com.example.exception.BusinessException;
import com.example.mapper.UserAddressMapper;
import com.example.model.dto.users.UserAddressDTO;
import com.example.model.entity.UserAddress;
import com.example.service.UserAddressService;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户地址服务实现类
 * 
 * @author 31815
 * @description 实现用户地址核心业务逻辑，包含：
 *              1. 地址归属校验
 *              2. 默认地址管理
 *              3. 缓存策略优化
 * @createDate 2025-02-18 23:43:56
 */
@Service
@CacheConfig(cacheNames = "userAddressService")
public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddress>
        implements UserAddressService {

    /**
     * 设置默认地址（事务操作）
     * @param userId 用户ID
     * @param addressId 地址ID
     * @return 操作结果
     * @implNote 执行逻辑：
     *           1. 校验地址归属
     *           2. 更新默认状态
     *           3. 清除用户缓存
     */
    @Override
    @Transactional
    @CacheEvict(key = "'user:' + #userId")
    public boolean setDefaultAddress(Long userId, Long addressId) {
        if (!checkAddressOwnership(userId, addressId)) {
            throw new BusinessException(ResultCode.ADDRESS_ACCESS_DENIED);
        }
        return baseMapper.updateDefaultStatus(userId, addressId) > 0;
    }

    /**
     * 获取默认地址（缓存优化）
     * @param userId 用户ID
     * @return 默认地址
     * @implNote 缓存策略：
     *           1. 缓存键：user:{userId}:default
     *           2. 缓存时间：30分钟
     */
    @Override
    @Cacheable(key = "'user:' + #userId + ':default'")
    public UserAddressDTO getDefaultAddress(Long userId) {
        UserAddress address = baseMapper.selectDefaultByUserId(userId);
        if (address == null) {
            return null;
        }
        return UserAddressDTO.fromEntity(address);
    }

    /**
     * 添加地址（智能默认）
     * @param address 地址信息
     * @return 操作结果
     * @implNote 业务逻辑：
     *           1. 首次添加设为默认
     *           2. 清除列表缓存
     */
    @Override
    @Transactional
    @CacheEvict(key = "'user:' + #address.userId")
    public boolean addAddress(UserAddressDTO address) {
        UserAddress entity = address.toEntity();
        if (countUserAddresses(entity.getUserId()) == 0) {
            entity.setIsDefault(1);
        }
        return save(entity);
    }

    /**
     * 更新地址（安全操作）
     * @param userId 用户ID
     * @param address 地址信息
     * @return 操作结果
     * @implNote 执行逻辑：
     *           1. 校验地址归属
     *           2. 更新地址信息
     *           3. 清除列表缓存
     */
    @Override
    @Transactional
    @CacheEvict(key = "'user:' + #userId + ':list'", beforeInvocation = true)
    public boolean updateAddress(Long userId, UserAddressDTO address) {
        if (!checkAddressOwnership(userId, address.getId())) {
            throw new BusinessException(ResultCode.ADDRESS_ACCESS_DENIED);
        }
        UserAddress entity = address.toEntity();
        return updateById(entity);
    }

    /**
     * 删除地址（安全操作）
     * @param userId 用户ID
     * @param addressId 地址ID
     * @return 操作结果
     * @implNote 执行逻辑：
     *           1. 校验地址归属
     *           2. 删除地址
     *           3. 清除用户缓存
     */
    @Override
    @Transactional
    @CacheEvict(key = "'user:' + #userId")
    public boolean deleteAddress(Long userId, Long addressId) {
        if (!checkAddressOwnership(userId, addressId)) {
            throw new BusinessException(ResultCode.ADDRESS_ACCESS_DENIED);
        }
        return removeById(addressId);
    }

    /**
     * 获取地址列表（缓存优化）
     * @param userId 用户ID
     * @return 地址列表
     * @implNote 缓存策略：
     *           1. 缓存键：user:{userId}:list
     *           2. 缓存时间：1小时
     */
    @Override
    @Cacheable(key = "'user:' + #userId + ':list'")
    public List<UserAddressDTO> listUserAddresses(Long userId) {
        return lambdaQuery()
                .eq(UserAddress::getUserId, userId)
                .orderByDesc(UserAddress::getIsDefault)
                .orderByDesc(UserAddress::getUpdateTime)
                .list()
                .stream()
                .map(UserAddressDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 校验地址归属
     * @param userId 用户ID
     * @param addressId 地址ID
     * @return 是否属于该用户
     */
    private boolean checkAddressOwnership(Long userId, Long addressId) {
        return lambdaQuery()
                .eq(UserAddress::getId, addressId)
                .eq(UserAddress::getUserId, userId)
                .exists();
    }

    /**
     * 统计用户地址数量
     * @param userId 用户ID
     * @return 地址总数
     */
    private Long countUserAddresses(Long userId) {
        return lambdaQuery()
                .eq(UserAddress::getUserId, userId)
                .count();
    }

    /**
     * 校验地址有效性
     * @param addressId 地址ID
     * @return 是否有效
     * @implNote 有效地址需包含省市区和详细地址
     */
    public boolean validateAddress(Long addressId) {
        return lambdaQuery()
                .eq(UserAddress::getId, addressId)
                .isNotNull(UserAddress::getProvince)
                .isNotNull(UserAddress::getCity)
                .isNotNull(UserAddress::getDistrict)
                .isNotNull(UserAddress::getDetailAddress)
                .exists();
    }


}




