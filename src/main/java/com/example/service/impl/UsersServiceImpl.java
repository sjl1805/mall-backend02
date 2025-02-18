package com.example.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.UsersMapper;
import com.example.model.dto.UserQuery;
import com.example.model.entity.Users;
import com.example.service.UsersService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
* @author 31815
* @description 针对表【users(用户表)】的数据库操作Service实现
* @createDate 2025-02-18 23:43:44
*/
@Service
@CacheConfig(cacheNames = "userCache") // 统一缓存配置
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users>
    implements UsersService {

    private final PasswordEncoder passwordEncoder;

    private final UsersMapper usersMapper;

    public UsersServiceImpl(PasswordEncoder passwordEncoder, 
                           UsersMapper usersMapper) {
        this.passwordEncoder = passwordEncoder;
        this.usersMapper = usersMapper;
    }

    @Override
    public IPage<Users> listUsersByPage(UserQuery query) {
        Page<Users> page = new Page<>(query.getPage(), query.getSize());
        return usersMapper.selectUserPage(page, query);
    }

    @Override
    @CacheEvict(allEntries = true) // 注册时清空缓存
    public Users registerUser(Users user) {
        // 检查用户名是否已存在
        if (lambdaQuery().eq(Users::getUsername, user.getUsername()).exists()) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        save(user);
        return user;
    }

    @Override
    @CacheEvict(key = "#userId") // 更新状态时清除该用户缓存
    public boolean updateUserStatus(Long userId, Integer status) {
        return lambdaUpdate()
                .eq(Users::getId, userId)
                .set(Users::getStatus, status)
                .update();
    }

    @Override
    @Cacheable(key = "'statistics'") // 缓存统计结果
    public Map<String, Object> getUserStatistics() {
        Map<String, Object> stats = new HashMap<>();
        // 总用户数
        stats.put("totalUsers", count());
        // 状态分布
        stats.put("statusDistribution", usersMapper.countUserStatus());
        return stats;
    }

    @Override
    @Cacheable(key = "#id") // 按ID缓存用户信息
    public Users getById(Long id) {
        return super.getById(id);
    }

    @Override
    @CacheEvict(key = "#user.id") // 更新时清除缓存
    public boolean updateById(Users user) {
        return super.updateById(user);
    }
}




