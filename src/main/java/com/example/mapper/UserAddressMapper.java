package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.model.entity.UserAddress;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 31815
 * @description 针对表【user_address(用户收货地址表)】的数据库操作Mapper
 * @createDate 2025-02-24 12:03:50
 * @Entity model.entity.UserAddress
 */
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
}




