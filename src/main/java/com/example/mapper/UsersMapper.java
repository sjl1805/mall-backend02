package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.model.entity.Users;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 31815
 * @description 针对表【users(用户表)】的数据库操作Mapper
 * @createDate 2025-02-24 12:03:41
 * @Entity model.entity.Users
 */
@Mapper
public interface UsersMapper extends BaseMapper<Users> {

    /**
     * 根据用户名模糊查询用户
     * @param username 用户名
     * @return 用户列表
     */
    List<Users> selectByUsernameLike(@Param("username") String username);

    /**
     * 分页查询用户
     * @param page 分页信息
     * @return 用户列表
     */
    IPage<Users> selectPage(IPage<Users> page);

    /**
     * 根据ID查询用户
     * @param id 用户ID
     * @return 用户信息
     */
    Users selectById(@Param("id") Long id);

    /**
     * 插入新用户
     * @param user 用户信息
     * @return 插入结果
     */
    int insertUser(Users user);

    /**
     * 更新用户信息
     * @param user 用户信息
     * @return 更新结果
     */
    int updateUser(Users user);

    /**
     * 根据ID删除用户
     * @param id 用户ID
     * @return 删除结果
     */
    int deleteUser(@Param("id") Long id);
}




