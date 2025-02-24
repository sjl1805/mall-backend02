package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.model.entity.UserAddress;
import org.apache.ibatis.annotations.Param;

/**
 * @author 31815
 * @description 针对表【user_address(用户收货地址表)】的数据库操作Mapper
 * @createDate 2025-02-24 12:03:50
 * @Entity model.entity.UserAddress
 */
public interface UserAddressMapper extends BaseMapper<UserAddress> {

    /**
     * 取消原有默认地址
     * @param userId 用户ID
     * @return 影响行数
     */
    int unsetDefaultAddress(@Param("userId") Long userId);

    /**
     * 设置新默认地址
     * @param userId 用户ID
     * @param addressId 地址ID
     * @return 影响行数
     */
    int setDefaultAddress(@Param("userId") Long userId, 
                         @Param("addressId") Long addressId);

    /**
     * 分页查询地址
     * @param page 分页参数
     * @param userId 用户ID
     * @return 分页结果
     */
    Page<UserAddress> selectAddressPage(Page<UserAddress> page, 
                                       @Param("userId") Long userId);

    /**
     * 地址有效性验证
     * @param userId 用户ID
     * @param addressId 地址ID
     * @return 存在返回1，否则0
     */
    int validateAddress(@Param("userId") Long userId,
                       @Param("addressId") Long addressId);

    /**
     * 删除用户地址
     * @param userId 用户ID
     * @param addressId 地址ID
     * @return 影响行数
     */
    int deleteAddress(@Param("userId") Long userId,
                     @Param("addressId") Long addressId);

    /**
     * 动态更新地址信息
     * @param address 地址实体
     * @return 影响行数
     */
    int updateAddressInfo(UserAddress address);

}




