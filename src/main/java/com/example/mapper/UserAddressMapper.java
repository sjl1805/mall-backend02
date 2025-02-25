package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.model.entity.UserAddress;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 31815
 * @description 针对表【user_address(用户收货地址表)】的数据库操作Mapper
 * @createDate 2025-02-24 12:03:50
 * @Entity model.entity.UserAddress
 */
@Mapper
public interface UserAddressMapper extends BaseMapper<UserAddress> {

    /**
     * 根据用户ID查询收货地址
     *
     * @param userId 用户ID
     * @return 用户收货地址列表
     */
    List<UserAddress> selectByUserId(@Param("userId") Long userId);

    /**
     * 分页查询用户收货地址
     *
     * @param page 分页信息
     * @return 用户收货地址列表
     */
    IPage<UserAddress> selectPage(IPage<UserAddress> page);

    /**
     * 根据ID查询用户收货地址
     *
     * @param id 收货地址ID
     * @return 用户收货地址信息
     */
    UserAddress selectById(@Param("id") Long id);

    /**
     * 插入新用户收货地址
     *
     * @param userAddress 用户收货地址信息
     * @return 插入结果
     */
    int insertUserAddress(UserAddress userAddress);

    /**
     * 更新用户收货地址信息
     *
     * @param userAddress 用户收货地址信息
     * @return 更新结果
     */
    int updateUserAddress(UserAddress userAddress);

    /**
     * 根据ID删除用户收货地址
     *
     * @param id 收货地址ID
     * @return 删除结果
     */
    int deleteUserAddress(@Param("id") Long id);

    /**
     * 查询用户默认地址
     *
     * @param userId 用户ID
     * @return 默认地址信息，如果没有则返回null
     */
    UserAddress selectDefaultAddress(@Param("userId") Long userId);

    /**
     * 按地区查询用户地址
     *
     * @param userId 用户ID
     * @param province 省份
     * @param city 城市
     * @return 地址列表
     */
    List<UserAddress> selectByRegion(
            @Param("userId") Long userId,
            @Param("province") String province,
            @Param("city") String city);

    /**
     * 更新默认地址状态
     * 将指定地址设为默认，同时取消该用户其他地址的默认状态
     *
     * @param userId 用户ID
     * @param addressId 要设为默认的地址ID
     * @return 影响行数
     */
    int updateDefaultStatus(@Param("userId") Long userId, @Param("addressId") Long addressId);

    /**
     * 批量删除用户收货地址
     *
     * @param ids 地址ID列表
     * @return 删除结果
     */
    int batchDeleteAddresses(@Param("ids") List<Long> ids);

    /**
     * 查询用户收货地址数量
     *
     * @param userId 用户ID
     * @return 地址数量
     */
    int countByUserId(@Param("userId") Long userId);

    /**
     * 查询最近创建的地址
     *
     * @param userId 用户ID
     * @return 最近创建的地址
     */
    UserAddress selectLatestAddress(@Param("userId") Long userId);

    /**
     * 查询用户在指定城市的地址数量
     *
     * @param userId 用户ID
     * @param city 城市
     * @return 地址数量
     */
    int countByCity(@Param("userId") Long userId, @Param("city") String city);
}




