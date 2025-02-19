package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.model.dto.users.UserAddressPageDTO;
import com.example.model.entity.UserAddress;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 用户地址管理Mapper接口
 *
 * @author 毕业设计学生
 */
public interface UserAddressMapper extends BaseMapper<UserAddress> {

    IPage<UserAddress> selectAddressPage(IPage<UserAddress> page,
                                         @Param("query") UserAddressPageDTO queryDTO);

    /**
     * 设置用户默认地址
     *
     * @param userId    用户ID
     * @param addressId 地址ID
     * @return 影响行数
     */
    @Update("UPDATE user_address SET is_default = 1 WHERE id = #{addressId} AND user_id = #{userId}")
    int setDefaultAddress(@Param("userId") Long userId,
                          @Param("addressId") Long addressId);

    /**
     * 清除用户的默认地址
     *
     * @param userId 用户ID
     * @return 影响行数
     */
    @Update("UPDATE user_address SET is_default = 0 WHERE user_id = #{userId}")
    int clearDefaultAddress(@Param("userId") Long userId);

    /**
     * 根据用户ID查询默认地址
     *
     * @param userId 用户ID
     * @return 地址信息
     */
    UserAddress selectDefaultByUserId(@Param("userId") Long userId);

    @Update("UPDATE user_address SET is_default = 1 WHERE user_id = #{userId} AND id = #{addressId}")
    int updateDefaultStatus(@Param("userId") Long userId,
                            @Param("addressId") Long addressId);
}




