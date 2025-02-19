package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.ResultCode;
import com.example.exception.BusinessException;
import com.example.mapper.UserAddressMapper;
import com.example.model.entity.UserAddress;
import com.example.service.UserAddressService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 31815
 * @description 针对表【user_address(用户收货地址表)】的数据库操作Service实现
 * @createDate 2025-02-18 23:43:56
 */
@Service
@CacheConfig(cacheNames = "userAddressService")
public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddress>
        implements UserAddressService {

    @Override
    @Transactional
    @CacheEvict(key = "'user:' + #userId")
    public boolean setDefaultAddress(Long userId, Long addressId) {
        // 校验地址归属
        if (!checkAddressOwnership(userId, addressId)) {
            throw new BusinessException(ResultCode.ADDRESS_ACCESS_DENIED);
        }

        return baseMapper.updateDefaultStatus(userId, addressId) > 0;
    }

    @Override
    @Cacheable(key = "'user:' + #userId + ':default'")
    public UserAddress getDefaultAddress(Long userId) {
        return baseMapper.selectDefaultByUserId(userId);
    }

    @Override
    @Transactional
    @CacheEvict(key = "'user:' + #userId")
    public boolean addAddress(UserAddress address) {
        // 首次添加设为默认地址
        if (countUserAddresses(address.getUserId()) == 0) {
            address.setIsDefault(1);
        }
        return save(address);
    }

    @Override
    @Transactional
    @CacheEvict(key = "'user:' + #userId + ':list'", beforeInvocation = true)
    public boolean updateAddress(Long userId, UserAddress address) {
        // 校验地址归属
        if (!checkAddressOwnership(userId, address.getId())) {
            throw new BusinessException(ResultCode.ADDRESS_ACCESS_DENIED);
        }
        return updateById(address);
    }

    @Override
    @Transactional
    @CacheEvict(key = "'user:' + #userId")
    public boolean deleteAddress(Long userId, Long addressId) {
        // 校验地址归属
        if (!checkAddressOwnership(userId, addressId)) {
            throw new BusinessException(ResultCode.ADDRESS_ACCESS_DENIED);
        }
        return removeById(addressId);
    }

    @Override
    @Cacheable(key = "'user:' + #userId + ':list'")
    public List<UserAddress> listUserAddresses(Long userId) {
        return lambdaQuery()
                .eq(UserAddress::getUserId, userId)
                .orderByDesc(UserAddress::getIsDefault)
                .orderByDesc(UserAddress::getUpdateTime)
                .list();
    }

    private boolean checkAddressOwnership(Long userId, Long addressId) {
        return lambdaQuery()
                .eq(UserAddress::getId, addressId)
                .eq(UserAddress::getUserId, userId)
                .exists();
    }

    private Long countUserAddresses(Long userId) {
        return lambdaQuery()
                .eq(UserAddress::getUserId, userId)
                .count();
    }

    // private void checkAddressLimit(Long userId) {
    //     Long count = countUserAddresses(userId);
    //     if (count >= 20) { // 最多允许20个地址
    //         throw new BusinessException(ResultCode.ADDRESS_LIMIT_EXCEEDED);
    //     }
    // }

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




