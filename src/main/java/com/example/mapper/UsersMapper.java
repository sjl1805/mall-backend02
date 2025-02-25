package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.model.entity.Users;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * @author 31815
 * @description 针对表【users(用户表)】的数据库操作Mapper
 * @createDate 2025-02-24 12:03:41
 * @Entity model.entity.Users
 */
@Mapper
@Repository
public interface UsersMapper extends BaseMapper<Users> {

    /**
     * 根据用户名模糊查询用户
     *
     * @param username 用户名
     * @return 用户列表
     */
    List<Users> selectByUsernameLike(@Param("username") String username);


    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    @Select("select * from users where username = #{username}")
    Users selectByUsername(@Param("username") String username);

    /**
     * 根据电话号码查询用户
     *
     * @param phone 电话号码
     * @return 用户信息
     */
    Users selectByPhone(@Param("phone") String phone);

    /**
     * 根据邮箱查询用户
     *
     * @param email 邮箱
     * @return 用户信息
     */
    Users selectByEmail(@Param("email") String email);

    /**
     * 根据状态查询用户列表
     *
     * @param status 用户状态
     * @return 用户列表
     */
    List<Users> selectByStatus(@Param("status") Integer status);

    /**
     * 高级查询用户列表
     *
     * @param username 用户名(可选)
     * @param phone 手机号(可选)
     * @param status 状态(可选)
     * @param page 分页参数
     * @return 分页用户数据
     */
    IPage<Users> selectUsersByCondition(
            @Param("username") String username,
            @Param("phone") String phone,
            @Param("status") Integer status,
            Page<Users> page);

    /**
     * 修改用户状态
     *
     * @param id 用户ID
     * @param status 新状态
     * @return 更新结果
     */
    int updateUserStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 批量删除用户
     *
     * @param ids 用户ID列表
     * @return 删除结果
     */
    int batchDeleteUsers(@Param("ids") List<Long> ids);

    /**
     * 检查用户名是否存在
     *
     * @param username 用户名
     * @return 存在数量
     */
    int checkUsernameExists(@Param("username") String username);

    /**
     * 分页查询用户
     *
     * @param page 分页信息
     * @return 用户列表
     */
    IPage<Users> selectPage(IPage<Users> page);

    /**
     * 根据ID查询用户
     *
     * @param id 用户ID
     * @return 用户信息
     */
    @Override
    @Select("select * from users where user_id = #{id}")
    Users selectById(Serializable id);

    /**
     * 插入新用户
     *
     * @param user 用户信息
     * @return 插入结果
     */
    int insertUser(Users user);

    /**
     * 更新用户信息
     *
     * @param user 用户信息
     * @return 更新结果
     */
    int updateUser(Users user);

    /**
     * 根据ID删除用户
     *
     * @param id 用户ID
     * @return 删除结果
     */
    int deleteUser(@Param("id") Long id);
}




