package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.service.UserBehaviorService;
import com.example.model.entity.UserBehavior;
import com.example.mapper.UserBehaviorMapper;
import org.springframework.stereotype.Service;

/**
* @author 31815
* @description 针对表【user_behavior(用户行为记录表)】的数据库操作Service实现
* @createDate 2025-02-18 23:43:52
*/
@Service
public class UserBehaviorServiceImpl extends ServiceImpl<UserBehaviorMapper, UserBehavior>
    implements UserBehaviorService {

}




