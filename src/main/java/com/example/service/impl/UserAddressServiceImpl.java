package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.model.entity.UserAddress;
import com.example.service.UserAddressService;
import com.example.mapper.UserAddressMapper;
import org.springframework.stereotype.Service;

/**
* @author 31815
* @description 针对表【user_address(用户收货地址表)】的数据库操作Service实现
* @createDate 2025-02-24 12:03:50
*/
@Service
public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddress>
    implements UserAddressService{

}




