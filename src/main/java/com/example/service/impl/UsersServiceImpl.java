package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.UsersMapper;
import com.example.model.entity.Users;
import com.example.service.UsersService;
import org.springframework.stereotype.Service;

/**
 * @author 31815
 * @description 针对表【users(用户表)】的数据库操作Service实现
 * @createDate 2025-02-24 12:03:41
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users>
        implements UsersService {

}




