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
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * 获取用户默认收货地址
     * 
     * 该方法从缓存或数据库获取用户设置的默认收货地址，
     * 用于订单创建时自动选择默认地址，提高用户便利性
     *
     * @param userId 用户ID
     * @return 默认收货地址，没有则返回null
     */
    @Override
    @Cacheable(value = "userAddresses", key = "'default_' + #userId")
    public UserAddress getDefaultAddress(Long userId) {
        return userAddressMapper.selectDefaultAddress(userId);
    }

    /**
     * 设置默认收货地址
     * 
     * 该方法将指定地址设置为默认地址，同时取消其他地址的默认状态，
     * 用于用户修改默认收货地址的场景
     *
     * @param userId 用户ID
     * @param addressId 地址ID
     * @return 设置结果
     */
    @Override
    @Transactional
    @CacheEvict(value = "userAddresses", allEntries = true)
    public boolean setDefaultAddress(Long userId, Long addressId) {
        // 检查地址是否属于该用户
        if (!checkAddressOwnership(userId, addressId)) {
            return false;
        }
        return userAddressMapper.updateDefaultStatus(userId, addressId) > 0;
    }

    /**
     * 按地区查询收货地址
     * 
     * 该方法根据省份、城市筛选用户的收货地址，
     * 适用于大城市用户拥有多个地址时，按区域快速筛选的场景
     *
     * @param userId 用户ID
     * @param province 省份
     * @param city 城市
     * @return 收货地址列表
     */
    @Override
    @Cacheable(value = "userAddresses", key = "#userId + '_' + #province + '_' + #city")
    public List<UserAddress> selectByRegion(Long userId, String province, String city) {
        return userAddressMapper.selectByRegion(userId, province, city);
    }

    /**
     * 获取用户常用地址
     * 
     * 该方法根据地址使用频率返回用户的常用地址，
     * 用于下单时智能推荐地址，提升用户体验
     * 
     * 注意：需要添加地址使用频率记录功能
     *
     * @param userId 用户ID
     * @param limit 限制数量
     * @return 常用地址列表
     */
    @Override
    @Cacheable(value = "userAddresses", key = "'frequent_' + #userId + '_' + #limit")
    public List<UserAddress> getFrequentlyUsedAddresses(Long userId, Integer limit) {
        // 此处需要实际实现，可以基于订单历史分析地址使用频率
        // 简化实现，使用默认地址和最新添加的地址
        List<UserAddress> addresses = selectByUserId(userId);
        // 按照默认地址优先，然后按创建时间倒序排序
        addresses.sort((a1, a2) -> {
            if (a1.getIsDefault() != null && a1.getIsDefault() == 1) return -1;
            if (a2.getIsDefault() != null && a2.getIsDefault() == 1) return 1;
            // 如果都不是默认地址，则按创建时间倒序
            return a2.getCreateTime().compareTo(a1.getCreateTime());
        });
        
        // 返回指定数量的地址
        return addresses.stream().limit(limit == null ? 3 : limit).collect(Collectors.toList());
    }

    /**
     * 获取用户最近使用的地址
     * 
     * 该方法返回用户最近一次下单使用的收货地址，
     * 用于快速复用上次使用的地址，提高用户便利性
     * 
     * 注意：需要记录订单使用的地址信息
     *
     * @param userId 用户ID
     * @return 最近使用的地址
     */
    @Override
    @Cacheable(value = "userAddresses", key = "'recent_' + #userId")
    public UserAddress getRecentlyUsedAddress(Long userId) {
        // 此处需要实际实现，可以查询用户最近订单的收货地址
        // 简化实现，返回默认地址
        return getDefaultAddress(userId);
    }

    /**
     * 批量删除收货地址
     * 
     * 该方法批量删除指定的收货地址，
     * 适用于用户批量管理地址的场景
     *
     * @param ids 地址ID列表
     * @return 删除结果
     */
    @Override
    @Transactional
    @CacheEvict(value = "userAddresses", allEntries = true)
    public boolean batchDeleteAddresses(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        
        // 批量删除
        int deletedCount = 0;
        for (Long id : ids) {
            if (userAddressMapper.deleteById(id) > 0) {
                deletedCount++;
            }
        }
        
        return deletedCount > 0;
    }

    /**
     * 检查地址是否属于用户
     * 
     * 该方法验证指定地址是否属于该用户，
     * 用于防止越权操作，确保用户只能操作自己的地址
     *
     * @param userId 用户ID
     * @param addressId 地址ID
     * @return 检查结果
     */
    @Override
    public boolean checkAddressOwnership(Long userId, Long addressId) {
        UserAddress address = userAddressMapper.selectById(addressId);
        return address != null && address.getUserId().equals(userId);
    }

    /**
     * 复制地址
     * 
     * 该方法复制现有地址为新地址，便于用户快速创建相似地址，
     * 用于用户需要添加多个相似地址的场景，如同一小区不同楼栋
     *
     * @param addressId 原地址ID
     * @param newAddressName 新地址收件人姓名
     * @return 新地址
     */
    @Override
    @Transactional
    @CacheEvict(value = "userAddresses", allEntries = true)
    public UserAddress copyAddress(Long addressId, String newAddressName) {
        // 获取原地址
        UserAddress sourceAddress = userAddressMapper.selectById(addressId);
        if (sourceAddress == null) {
            return null;
        }
        
        // 创建新地址
        UserAddress newAddress = new UserAddress();
        newAddress.setUserId(sourceAddress.getUserId());
        newAddress.setIsDefault(0); // 非默认地址
        newAddress.setReceiverName(newAddressName);
        newAddress.setReceiverPhone(sourceAddress.getReceiverPhone());
        newAddress.setProvince(sourceAddress.getProvince());
        newAddress.setCity(sourceAddress.getCity());
        newAddress.setDistrict(sourceAddress.getDistrict());
        newAddress.setDetailAddress(sourceAddress.getDetailAddress());
        
        // 保存新地址
        if (insertUserAddress(newAddress)) {
            return newAddress;
        }
        return null;
    }

    /**
     * 检查地址是否可用
     * 
     * 该方法验证地址信息的完整性和有效性，
     * 用于在保存地址前进行基本验证，确保地址可用
     *
     * @param address 地址实体
     * @return 检查结果
     */
    @Override
    public boolean validateAddress(UserAddress address) {
        // 检查基本字段是否为空
        if (address == null || address.getUserId() == null ||
                StringUtils.isEmpty(address.getReceiverName()) ||
                StringUtils.isEmpty(address.getReceiverPhone()) ||
                StringUtils.isEmpty(address.getProvince()) ||
                StringUtils.isEmpty(address.getCity()) ||
                StringUtils.isEmpty(address.getDistrict()) ||
                StringUtils.isEmpty(address.getDetailAddress())) {
            return false;
        }
        
        // 检查手机号格式
        String phoneRegex = "^1[3-9]\\d{9}$";
        if (!address.getReceiverPhone().matches(phoneRegex)) {
            return false;
        }
        
        // 更多验证规则可以根据实际需求添加
        
        return true;
    }
}




