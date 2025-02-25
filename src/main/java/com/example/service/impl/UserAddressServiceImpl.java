package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.exception.BusinessException;
import com.example.mapper.UserAddressMapper;
import com.example.model.entity.UserAddress;
import com.example.service.UserAddressService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户地址服务实现类
 */
@Service
public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddress> implements UserAddressService {

    @Override
    public List<UserAddress> getUserAddresses(Long userId) {
        return baseMapper.findByUserId(userId);
    }

    @Override
    public UserAddress getDefaultAddress(Long userId) {
        return baseMapper.findDefaultByUserId(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserAddress addAddress(UserAddress address) {
        // 检查是否设置为默认地址
        boolean isDefault = address.getIsDefault() != null && address.getIsDefault() == 1;
        
        // 如果设置为默认地址，需要将该用户的其他地址设为非默认
        if (isDefault) {
            baseMapper.clearDefault(address.getUserId());
        }
        
        // 如果是用户的第一个地址，自动设为默认地址
        long addressCount = count(new LambdaQueryWrapper<UserAddress>()
                .eq(UserAddress::getUserId, address.getUserId()));
        if (addressCount == 0) {
            address.setIsDefault(1);
        } else if (address.getIsDefault() == null) {
            address.setIsDefault(0);
        }
        
        // 设置时间
        address.setCreateTime(LocalDateTime.now());
        address.setUpdateTime(LocalDateTime.now());
        
        // 保存地址
        save(address);
        
        return address;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateAddress(UserAddress address) {
        if (address == null || address.getId() == null) {
            throw new BusinessException("地址ID不能为空");
        }
        
        // 获取原地址信息
        UserAddress original = getById(address.getId());
        if (original == null || !original.getUserId().equals(address.getUserId())) {
            throw new BusinessException("地址不存在或无权操作");
        }
        
        // 如果设置为默认地址，需要将该用户的其他地址设为非默认
        boolean isDefault = address.getIsDefault() != null && address.getIsDefault() == 1;
        if (isDefault && (original.getIsDefault() == null || original.getIsDefault() != 1)) {
            baseMapper.clearDefault(address.getUserId());
        }
        
        // 设置更新时间
        address.setCreateTime(null); // 创建时间不允许修改
        address.setUpdateTime(LocalDateTime.now());
        
        return updateById(address);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setDefaultAddress(Long userId, Long addressId) {
        // 检查地址是否存在且属于该用户
        UserAddress address = getById(addressId);
        if (address == null || !address.getUserId().equals(userId)) {
            throw new BusinessException("地址不存在或无权操作");
        }
        
        // 将该用户的所有地址设为非默认
        baseMapper.clearDefault(userId);
        
        // 将指定地址设为默认
        LambdaUpdateWrapper<UserAddress> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserAddress::getId, addressId)
                .set(UserAddress::getIsDefault, 1)
                .set(UserAddress::getUpdateTime, LocalDateTime.now());
        
        return update(updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteAddress(Long userId, Long addressId) {
        // 检查地址是否存在且属于该用户
        UserAddress address = getById(addressId);
        if (address == null || !address.getUserId().equals(userId)) {
            throw new BusinessException("地址不存在或无权操作");
        }
        
        // 删除地址
        boolean result = removeById(addressId);
        
        // 如果删除的是默认地址，且用户还有其他地址，则设置一个新的默认地址
        if (result && address.getIsDefault() == 1) {
            LambdaQueryWrapper<UserAddress> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(UserAddress::getUserId, userId)
                    .orderByDesc(UserAddress::getUpdateTime)
                    .last("LIMIT 1");
            
            UserAddress newDefault = getOne(queryWrapper);
            if (newDefault != null) {
                setDefaultAddress(userId, newDefault.getId());
            }
        }
        
        return result;
    }
} 