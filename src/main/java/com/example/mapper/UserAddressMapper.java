package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.entity.UserAddress;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 用户地址数据访问层接口
 */
@Mapper
public interface UserAddressMapper extends BaseMapper<UserAddress> {
    
    /**
     * 获取用户的地址列表
     *
     * @param userId 用户ID
     * @return 地址列表
     */
    @Select("SELECT * FROM user_address WHERE user_id = #{userId} ORDER BY is_default DESC, id DESC")
    List<UserAddress> findByUserId(@Param("userId") Long userId);
    
    /**
     * 获取用户的默认地址
     *
     * @param userId 用户ID
     * @return 默认地址
     */
    @Select("SELECT * FROM user_address WHERE user_id = #{userId} AND is_default = 1 LIMIT 1")
    UserAddress findDefaultByUserId(@Param("userId") Long userId);
    
    /**
     * 将用户的所有地址设置为非默认
     *
     * @param userId 用户ID
     * @return 影响行数
     */
    @Update("UPDATE user_address SET is_default = 0 WHERE user_id = #{userId}")
    int clearDefault(@Param("userId") Long userId);
} 