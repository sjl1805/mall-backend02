package com.example.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.UsersMapper;
import com.example.model.dto.users.UserPageDTO;
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
import com.example.model.dto.users.UserRegisterDTO;
import com.example.model.dto.users.UserLoginDTO;

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
    public IPage<Users> listUsersByPage(UserPageDTO queryDTO) {
        Page<Users> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        return usersMapper.selectUserPage(page, queryDTO);
    }

    @Override
    @Cacheable(key = "#loginDTO.account")
    public Users login(UserLoginDTO loginDTO) {
        Users user = usersMapper.selectByUsernameOrPhone(loginDTO.getAccount());
        if (user == null || !passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }
        return user;
    }

    @Override
    @CacheEvict(allEntries = true) // 注册时清空缓存
    public Map<String, Object> registerUser(UserRegisterDTO registerDTO) {
        // 检查用户名是否已存在
        if (lambdaQuery().eq(Users::getUsername, registerDTO.getUsername()).exists()) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 加密密码
        registerDTO.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        //save(registerDTO);

        // 生成token
        String token = "100";

        // 返回用户信息和token
        Map<String, Object> result = new HashMap<>();
        result.put("user", registerDTO);
        result.put("token", token);
        return result;
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




