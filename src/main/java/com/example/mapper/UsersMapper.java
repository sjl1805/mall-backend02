package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.entity.Users;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author 31815
 * @description 针对表【users(用户表)】的数据库操作Mapper
 * @createDate 2025-02-24 12:03:41
 * @Entity model.entity.Users
 */
@Mapper
public interface UsersMapper extends BaseMapper<Users> {

    /**
     * 根据登录ID（用户名/手机/邮箱）查询用户
     * @param loginId 登录标识
     * @return 用户实体
     */
    Users selectByLoginId(@Param("loginId") String loginId);

    /**
     * 更新用户状态
     * @param userId 用户ID
     * @param status 新状态（0-禁用，1-启用）
     * @return 影响行数
     */
    int updateUserStatus(@Param("userId") Long userId, @Param("status") Integer status);

    /**
     * 动态更新用户信息
     * @param user 用户实体（非空字段才会更新）
     * @return 影响行数
     */
    int updateUserInfo(Users user);

}




