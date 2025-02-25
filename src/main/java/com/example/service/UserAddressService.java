package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.UserAddress;

import java.util.List;

/**
 * @author 31815
 * @description 针对表【user_address(用户收货地址表)】的数据库操作Service
 * @createDate 2025-02-24 12:03:50
 */
public interface UserAddressService extends IService<UserAddress> {

    /**
     * 根据用户ID查询收货地址
     *
     * @param userId 用户ID
     * @return 收货地址列表
     */
    List<UserAddress> selectByUserId(Long userId);

    /**
     * 分页查询收货地址
     *
     * @param page 分页信息
     * @return 收货地址列表
     */
    IPage<UserAddress> selectPage(IPage<UserAddress> page);

    /**
     * 根据ID查询收货地址
     *
     * @param id 收货地址ID
     * @return 收货地址信息
     */
    UserAddress selectById(Long id);

    /**
     * 新增收货地址
     *
     * @param userAddress 收货地址信息
     * @return 插入结果
     */
    boolean insertUserAddress(UserAddress userAddress);

    /**
     * 更新收货地址信息
     *
     * @param userAddress 收货地址信息
     * @return 更新结果
     */
    boolean updateUserAddress(UserAddress userAddress);

    /**
     * 根据ID删除收货地址
     *
     * @param id 收货地址ID
     * @return 删除结果
     */
    boolean deleteUserAddress(Long id);

    /**
     * 获取用户默认收货地址
     *
     * @param userId 用户ID
     * @return 默认收货地址，没有则返回null
     */
    UserAddress getDefaultAddress(Long userId);

    /**
     * 设置默认收货地址
     *
     * @param userId 用户ID
     * @param addressId 地址ID
     * @return 设置结果
     */
    boolean setDefaultAddress(Long userId, Long addressId);

    /**
     * 按地区查询收货地址
     *
     * @param userId 用户ID
     * @param province 省份
     * @param city 城市
     * @return 收货地址列表
     */
    List<UserAddress> selectByRegion(Long userId, String province, String city);

    /**
     * 获取用户常用地址
     * 根据使用频率返回最常用的几个地址
     *
     * @param userId 用户ID
     * @param limit 限制数量
     * @return 常用地址列表
     */
    List<UserAddress> getFrequentlyUsedAddresses(Long userId, Integer limit);

    /**
     * 获取用户最近使用的地址
     *
     * @param userId 用户ID
     * @return 最近使用的地址
     */
    UserAddress getRecentlyUsedAddress(Long userId);

    /**
     * 批量删除收货地址
     *
     * @param ids 地址ID列表
     * @return 删除结果
     */
    boolean batchDeleteAddresses(List<Long> ids);

    /**
     * 检查地址是否属于用户
     *
     * @param userId 用户ID
     * @param addressId 地址ID
     * @return 检查结果
     */
    boolean checkAddressOwnership(Long userId, Long addressId);

    /**
     * 复制地址
     * 复制现有地址为新地址，便于用户快速创建相似地址
     *
     * @param addressId 原地址ID
     * @param newAddressName 新地址收件人姓名
     * @return 新地址
     */
    UserAddress copyAddress(Long addressId, String newAddressName);

    /**
     * 检查地址是否可用
     * 验证地址信息的完整性和有效性
     *
     * @param address 地址实体
     * @return 检查结果
     */
    boolean validateAddress(UserAddress address);
}
