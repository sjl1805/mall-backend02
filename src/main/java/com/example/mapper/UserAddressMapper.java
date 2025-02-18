package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.entity.UserAddress;
import org.apache.ibatis.annotations.Param;

/**
 * 用户地址管理Mapper接口
 * @author 毕业设计学生
 */
public interface UserAddressMapper extends BaseMapper<UserAddress> {

    /**
     * 设置用户默认地址
     * @param userId 用户ID
     * @param addressId 地址ID
     * @return 影响行数
     */
    int setDefaultAddress(@Param("userId") Long userId, 
                         @Param("addressId") Long addressId);

    /**
     * 清除用户的默认地址
     * @param userId 用户ID
     * @return 影响行数
     */
    int clearDefaultAddress(@Param("userId") Long userId);

    /**
     * 根据用户ID查询默认地址
     * @param userId 用户ID
     * @return 地址信息
     */
    UserAddress selectDefaultByUserId(@Param("userId") Long userId);
}




