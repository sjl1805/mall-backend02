package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.UserAddressMapper;
import com.example.model.entity.UserAddress;
import com.example.service.UserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 31815
 * @description 针对表【user_address(用户收货地址表)】的数据库操作Service实现
 * @createDate 2025-02-24 12:03:50
 */
@Service
public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddress>
        implements UserAddressService {

    @Autowired
    private UserAddressMapper userAddressMapper;

    @Override
    public List<UserAddress> selectByUserId(Long userId) {
        return userAddressMapper.selectByUserId(userId);
    }

    @Override
    public IPage<UserAddress> selectPage(IPage<UserAddress> page) {
        return userAddressMapper.selectPage(page);
    }

    @Override
    public UserAddress selectById(Long id) {
        return userAddressMapper.selectById(id);
    }

    @Override
    public boolean insertUserAddress(UserAddress userAddress) {
        return userAddressMapper.insert(userAddress) > 0;
    }

    @Override
    public boolean updateUserAddress(UserAddress userAddress) {
        return userAddressMapper.updateById(userAddress) > 0;
    }

    @Override
    public boolean deleteUserAddress(Long id) {
        return userAddressMapper.deleteById(id) > 0;
    }
}




