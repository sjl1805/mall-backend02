package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.ResultCode;
import com.example.constants.AddressConstants;
import com.example.exception.BusinessException;
import com.example.mapper.UserAddressMapper;
import com.example.model.entity.UserAddress;
import com.example.service.UserAddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户收货地址服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddress> implements UserAddressService {

    private final UserAddressMapper userAddressMapper;

    @Override
    public List<UserAddress> getUserAddressList(Long userId) {
        if (userId == null) {
            return new ArrayList<>();
        }
        return userAddressMapper.selectByUserId(userId);
    }

    @Override
    public UserAddress getDefaultAddress(Long userId) {
        if (userId == null) {
            return null;
        }
        return userAddressMapper.selectDefaultAddress(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserAddress addUserAddress(UserAddress address) {
        validateAddress(address);

        // 设置创建和更新时间
        LocalDateTime now = LocalDateTime.now();
        address.setCreateTime(now);
        address.setUpdateTime(now);

        // 检查是否设置为默认地址
        if (AddressConstants.DEFAULT_ADDRESS.equals(address.getIsDefault())) {
            // 如果是默认地址，先取消其他默认地址
            userAddressMapper.cancelDefault(address.getUserId());
        } else {
            // 如果用户没有地址，则设置为默认地址
            int count = getUserAddressCount(address.getUserId());
            if (count == 0) {
                address.setIsDefault(AddressConstants.DEFAULT_ADDRESS);
            } else {
                address.setIsDefault(AddressConstants.NOT_DEFAULT_ADDRESS);
            }
        }

        // 保存地址
        boolean success = save(address);
        if (!success) {
            throw new BusinessException(ResultCode.FAILED, "添加收货地址失败");
        }

        return address;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserAddress(UserAddress address) {
        if (address.getId() == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "地址ID不能为空");
        }
        if (address.getUserId() == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "用户ID不能为空");
        }

        // 检查地址是否存在且属于该用户
        UserAddress existingAddress = getAddressDetail(address.getId(), address.getUserId());
        if (existingAddress == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "收货地址不存在或不属于当前用户");
        }

        // 如果设置为默认地址，先取消其他默认地址
        if (AddressConstants.DEFAULT_ADDRESS.equals(address.getIsDefault()) 
                && !AddressConstants.DEFAULT_ADDRESS.equals(existingAddress.getIsDefault())) {
            userAddressMapper.cancelDefault(address.getUserId());
        }

        // 设置更新时间
        address.setUpdateTime(LocalDateTime.now());
        address.setCreateTime(existingAddress.getCreateTime()); // 保留创建时间

        return updateById(address);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUserAddress(Long addressId, Long userId) {
        if (addressId == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "地址ID不能为空");
        }
        if (userId == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "用户ID不能为空");
        }

        // 检查地址是否存在且属于该用户
        UserAddress address = getAddressDetail(addressId, userId);
        if (address == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "收货地址不存在或不属于当前用户");
        }

        // 删除地址
        boolean success = removeById(addressId);
        
        // 如果删除的是默认地址，且用户还有其他地址，则将最新的地址设为默认地址
        if (success && AddressConstants.DEFAULT_ADDRESS.equals(address.getIsDefault())) {
            List<UserAddress> remainingAddresses = getUserAddressList(userId);
            if (!CollectionUtils.isEmpty(remainingAddresses)) {
                // 设置第一个地址为默认地址
                setDefaultAddress(remainingAddresses.get(0).getId(), userId);
            }
        }
        
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setDefaultAddress(Long addressId, Long userId) {
        if (addressId == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "地址ID不能为空");
        }
        if (userId == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "用户ID不能为空");
        }

        // 检查地址是否存在且属于该用户
        UserAddress address = getAddressDetail(addressId, userId);
        if (address == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "收货地址不存在或不属于当前用户");
        }

        // 取消所有默认地址
        userAddressMapper.cancelDefault(userId);

        // 设置为默认地址
        int rows = userAddressMapper.setDefault(addressId, userId);
        return rows > 0;
    }

    @Override
    public int getUserAddressCount(Long userId) {
        if (userId == null) {
            return 0;
        }
        return userAddressMapper.countByUserId(userId);
    }

    @Override
    public UserAddress getAddressDetail(Long addressId, Long userId) {
        if (addressId == null || userId == null) {
            return null;
        }
        
        LambdaQueryWrapper<UserAddress> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserAddress::getId, addressId)
                .eq(UserAddress::getUserId, userId);
                
        return getOne(queryWrapper);
    }

    @Override
    public boolean hasDefaultAddress(Long userId) {
        if (userId == null) {
            return false;
        }
        
        UserAddress defaultAddress = getDefaultAddress(userId);
        return defaultAddress != null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteUserAddresses(List<Long> addressIds, Long userId) {
        if (CollectionUtils.isEmpty(addressIds)) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "地址ID列表不能为空");
        }
        if (userId == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "用户ID不能为空");
        }

        // 检查是否包含默认地址
        boolean containsDefaultAddress = false;
        List<UserAddress> addressesToDelete = new ArrayList<>();
        
        for (Long addressId : addressIds) {
            UserAddress address = getAddressDetail(addressId, userId);
            if (address != null) {
                addressesToDelete.add(address);
                if (AddressConstants.DEFAULT_ADDRESS.equals(address.getIsDefault())) {
                    containsDefaultAddress = true;
                }
            }
        }
        
        if (addressesToDelete.isEmpty()) {
            return false;
        }
        
        // 删除地址
        boolean success = removeByIds(addressIds);
        
        // 如果删除了默认地址，且用户还有其他地址，则将最新的地址设为默认地址
        if (success && containsDefaultAddress) {
            List<UserAddress> remainingAddresses = getUserAddressList(userId);
            if (!CollectionUtils.isEmpty(remainingAddresses)) {
                // 设置第一个地址为默认地址
                setDefaultAddress(remainingAddresses.get(0).getId(), userId);
            }
        }
        
        return success;
    }

    /**
     * 验证收货地址信息
     *
     * @param address 收货地址信息
     */
    private void validateAddress(UserAddress address) {
        if (address == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "收货地址信息不能为空");
        }

        if (address.getUserId() == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "用户ID不能为空");
        }

        if (!StringUtils.hasText(address.getReceiverName())) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "收货人姓名不能为空");
        }

        if (!StringUtils.hasText(address.getReceiverPhone())) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "收货人电话不能为空");
        }

        if (!StringUtils.hasText(address.getProvince())) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "省份不能为空");
        }

        if (!StringUtils.hasText(address.getCity())) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "城市不能为空");
        }

        if (!StringUtils.hasText(address.getDistrict())) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "区/县不能为空");
        }

        if (!StringUtils.hasText(address.getDetailAddress())) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "详细地址不能为空");
        }

        // 校验手机号格式
        if (!address.getReceiverPhone().matches("^1[3-9]\\d{9}$")) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "手机号格式不正确");
        }
        
        // 校验收货人姓名长度
        if (address.getReceiverName().length() > AddressConstants.MAX_RECEIVER_NAME_LENGTH) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, 
                    "收货人姓名长度不能超过" + AddressConstants.MAX_RECEIVER_NAME_LENGTH + "个字符");
        }
        
        // 校验详细地址长度
        if (address.getDetailAddress().length() > AddressConstants.MAX_DETAIL_ADDRESS_LENGTH) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, 
                    "详细地址长度不能超过" + AddressConstants.MAX_DETAIL_ADDRESS_LENGTH + "个字符");
        }

        // 检查用户地址数量是否超出限制
        int count = getUserAddressCount(address.getUserId());
        if (count >= AddressConstants.MAX_ADDRESS_COUNT) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, 
                    "收货地址数量已达上限" + AddressConstants.MAX_ADDRESS_COUNT + "个");
        }
    }
} 