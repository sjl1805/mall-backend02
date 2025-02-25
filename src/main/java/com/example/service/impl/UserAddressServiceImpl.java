package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.UserAddressMapper;
import com.example.model.entity.UserAddress;
import com.example.service.UserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户地址服务实现类
 * 
 * 该类实现了用户收货地址相关的业务逻辑，包括地址的添加、修改、删除和查询等功能。
 * 用户地址是电商系统中重要的个人信息，用于订单配送和发票开具。
 * 使用了Spring缓存机制对用户地址信息进行缓存，提高查询效率。
 * 使用MyBatis-Plus的ServiceImpl简化数据访问操作。
 * 
 * @author 31815
 * @description 针对表【user_address(用户收货地址表)】的数据库操作Service实现
 * @createDate 2025-02-24 12:03:50
 */
@Service
@CacheConfig(cacheNames = "userAddresses") // 指定该服务类的缓存名称为"userAddresses"
public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddress>
        implements UserAddressService {

    @Autowired
    private UserAddressMapper userAddressMapper;

    /**
     * 根据用户ID查询用户收货地址列表
     * 
     * 该方法从缓存或数据库获取指定用户的所有收货地址信息，
     * 用于用户选择收货地址和管理地址列表
     *
     * @param userId 用户ID
     * @return 用户收货地址列表
     */
    @Override
    @Cacheable(value = "userAddresses", key = "#userId") // 缓存用户地址信息，提高查询效率
    public List<UserAddress> selectByUserId(Long userId) {
        return userAddressMapper.selectByUserId(userId);
    }

    /**
     * 分页查询用户地址数据
     * 
     * 该方法用于后台管理系统分页查看用户地址数据
     *
     * @param page 分页参数
     * @return 用户地址分页数据
     */
    @Override
    public IPage<UserAddress> selectPage(IPage<UserAddress> page) {
        return userAddressMapper.selectPage(page);
    }

    /**
     * 根据ID查询用户地址
     *
     * @param id 地址ID
     * @return 用户地址实体
     */
    @Override
    public UserAddress selectById(Long id) {
        return userAddressMapper.selectById(id);
    }

    /**
     * 添加用户收货地址
     * 
     * 该方法用于用户添加新的收货地址，
     * 并清除相关用户的地址缓存，确保数据一致性。
     * 需要注意的是，如果设置了默认地址，可能需要更新其他地址的默认状态。
     *
     * @param userAddress 用户地址实体
     * @return 添加成功返回true，失败返回false
     */
    @Override
    @Transactional
    @CacheEvict(value = "userAddresses", key = "#userAddress.userId") // 清除用户地址缓存
    public boolean insertUserAddress(UserAddress userAddress) {
        return userAddressMapper.insert(userAddress) > 0;
    }

    /**
     * 更新用户收货地址
     * 
     * 该方法用于用户修改收货地址信息，
     * 并清除相关用户的地址缓存，确保数据一致性。
     * 如果修改了默认地址状态，可能需要同时更新其他地址的默认状态。
     *
     * @param userAddress 用户地址实体
     * @return 更新成功返回true，失败返回false
     */
    @Override
    @Transactional
    @CacheEvict(value = "userAddresses", key = "#userAddress.userId") // 清除用户地址缓存
    public boolean updateUserAddress(UserAddress userAddress) {
        return userAddressMapper.updateById(userAddress) > 0;
    }

    /**
     * 删除用户收货地址
     * 
     * 该方法用于用户删除不需要的收货地址，
     * 并清除相关缓存，确保数据一致性。
     * 如果删除的是默认地址，可能需要选择新的默认地址。
     *
     * @param id 地址ID
     * @return 删除成功返回true，失败返回false
     */
    @Override
    @Transactional
    @CacheEvict(value = "userAddresses", key = "#id") // 清除被删除地址的缓存
    public boolean deleteUserAddress(Long id) {
        return userAddressMapper.deleteById(id) > 0;
    }
}




