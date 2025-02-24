package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.UserAddress;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * @author 31815
 * @description 针对表【user_address(用户收货地址表)】的数据库操作Service
 * @createDate 2025-02-24 12:03:50
 */
public interface UserAddressService extends IService<UserAddress> {

    /**
     * 根据用户ID查询收货地址
     * @param userId 用户ID
     * @return 收货地址列表
     */
    List<UserAddress> selectByUserId(Long userId);

    /**
     * 分页查询收货地址
     * @param page 分页信息
     * @return 收货地址列表
     */
    IPage<UserAddress> selectPage(IPage<UserAddress> page);

    /**
     * 根据ID查询收货地址
     * @param id 收货地址ID
     * @return 收货地址信息
     */
    UserAddress selectById(Long id);

    /**
     * 新增收货地址
     * @param userAddress 收货地址信息
     * @return 插入结果
     */
    boolean insertUserAddress(UserAddress userAddress);

    /**
     * 更新收货地址信息
     * @param userAddress 收货地址信息
     * @return 更新结果
     */
    boolean updateUserAddress(UserAddress userAddress);

    /**
     * 根据ID删除收货地址
     * @param id 收货地址ID
     * @return 删除结果
     */
    boolean deleteUserAddress(Long id);
}
