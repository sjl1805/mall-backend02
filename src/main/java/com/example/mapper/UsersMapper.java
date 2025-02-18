package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.model.entity.Users;
import com.example.model.query.UserQuery;
import org.apache.ibatis.annotations.Param;

/**
 * 用户管理Mapper接口
 * @author 毕业设计学生
 */
public interface UsersMapper extends BaseMapper<Users> {

    /**
     * 分页查询用户列表（带条件）
     * @param page 分页参数
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<Users> selectUserPage(IPage<Users> page, @Param("query") UserQuery query);

    /**
     * 根据用户名或手机号查询用户（用于登录）
     * @param account 用户名/手机号
     * @return 用户实体
     */
    Users selectByUsernameOrPhone(@Param("account") String account);

    /**
     * 更新用户状态
     * @param userId 用户ID
     * @param status 新状态
     * @return 影响行数
     */
    int updateUserStatus(@Param("userId") Long userId, @Param("status") Integer status);

    /**
     * 检查字段唯一性
     * @param field 字段名
     * @param value 字段值
     * @param excludeId 排除的ID
     * @return 存在的记录数
     */
    int checkFieldUnique(@Param("field") String field, 
                        @Param("value") String value,
                        @Param("excludeId") Long excludeId);
}




