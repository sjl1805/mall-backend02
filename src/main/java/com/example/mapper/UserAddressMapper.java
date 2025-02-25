package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.entity.UserAddress;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户地址Mapper接口
 */
@Mapper
public interface UserAddressMapper extends BaseMapper<UserAddress> {
    
    /**
     * 获取用户所有收货地址
     * @param userId 用户ID
     * @return 地址列表
     */
    List<UserAddress> selectByUserId(@Param("userId") Long userId);
    
    /**
     * 获取用户默认收货地址
     * @param userId 用户ID
     * @return 默认地址，如果没有则返回null
     */
    UserAddress selectDefaultAddress(@Param("userId") Long userId);
    
    /**
     * 将指定地址设为默认地址
     * @param addressId 地址ID
     * @param userId 用户ID
     * @return 影响行数
     */
    int setDefault(@Param("addressId") Long addressId, @Param("userId") Long userId);
    
    /**
     * 取消该用户所有默认地址
     * @param userId 用户ID
     * @return 影响行数
     */
    int cancelDefault(@Param("userId") Long userId);
    
    /**
     * 统计用户地址数量
     * @param userId 用户ID
     * @return 地址数量
     */
    int countByUserId(@Param("userId") Long userId);
} 