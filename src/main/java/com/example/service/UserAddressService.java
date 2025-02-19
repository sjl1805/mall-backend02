package com.example.service;

import com.example.model.entity.UserAddress;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
* @author 31815
* @description 针对表【user_address(用户收货地址表)】的数据库操作Service
* @createDate 2025-02-18 23:43:56
*/
public interface UserAddressService extends IService<UserAddress> {
    boolean setDefaultAddress(Long userId, Long addressId);
    UserAddress getDefaultAddress(Long userId);
    boolean addAddress(UserAddress address);
    boolean updateAddress(Long userId, UserAddress address);
    boolean deleteAddress(Long userId, Long addressId);
    List<UserAddress> listUserAddresses(Long userId);
}
