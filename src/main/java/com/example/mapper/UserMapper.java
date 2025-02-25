package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 用户数据访问层接口
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    
    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户对象
     */
    @Select("SELECT * FROM users WHERE username = #{username}")
    User findByUsername(@Param("username") String username);
    
    /**
     * 查询指定角色的用户数量
     *
     * @param role 角色ID
     * @return 用户数量
     */
    @Select("SELECT COUNT(*) FROM users WHERE role = #{role}")
    int countByRole(@Param("role") Integer role);
} 