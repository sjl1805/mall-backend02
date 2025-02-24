package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.UsersMapper;
import com.example.model.dto.UserLoginDTO;
import com.example.model.dto.UserRegisterDTO;
import com.example.model.dto.UserDTO;
import com.example.model.entity.Users;
import com.example.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CacheConfig;
/**
 * @author 31815
 * @description 针对表【users(用户表)】的数据库操作Service实现
 * @createDate 2025-02-24 12:03:41
 */
@Service
@CacheConfig(cacheNames = "users")
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users>
        implements UsersService {

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean register(UserRegisterDTO userRegisterDTO) {
        // 检查用户是否已存在
        Users existingUser = usersMapper.selectByUsername(userRegisterDTO.getUsername());
        if (existingUser != null) {
            return false; // 用户已存在
        }
        // 创建新用户
        Users newUser = new Users();
        newUser.setUsername(userRegisterDTO.getUsername());
        newUser.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword())); // 密码应加密存储
        return usersMapper.insert(newUser) > 0;
    }

    @Override
    public Users login(UserLoginDTO userLoginDTO) {
        Users user = usersMapper.selectByUsernameAndPassword(userLoginDTO.getUsername(), userLoginDTO.getPassword());
        if (user == null) {
            throw new RuntimeException("用户名或密码错误");
        } else if (passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())) {
            return user;
        } else {
            throw new RuntimeException("用户名或密码错误");
        }
    }

    @Override
    @Cacheable(value = "users", key = "#userId")
    public Users getUserById(Long userId) {
        return usersMapper.selectById(userId);
    }

    @Override
    public IPage<Users> selectPage(IPage<Users> page) {
        return usersMapper.selectPage(page);
    }

    @Override
    @CacheEvict(value = "users", key = "#userId")
    public boolean updateUserInfo(Long userId, UserDTO userDTO) {
        Users user = usersMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        return usersMapper.updateById(user) > 0;
    }

    @Override
    @CacheEvict(value = "users", key = "#userId")
    public boolean deleteUser(Long userId) {
        return usersMapper.deleteById(userId) > 0;
    }

}




