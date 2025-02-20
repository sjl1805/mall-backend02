package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.example.model.dto.UserDTO;
import com.example.model.entity.Users;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 用户管理Mapper接口
 * 实现用户的增删改查、状态管理和多条件搜索
 * 
 * @author 毕业设计学生
 */
public interface UsersMapper extends BaseMapper<Users> {

    /**
     * 分页查询用户（支持多条件动态查询）
     * 
     * @param page     分页参数（包含当前页、每页数量）
     * @param queryDTO 查询条件DTO（包含用户名、手机号、状态等）
     * @return 分页结果（包含用户列表和分页信息）
     */
    IPage<Users> selectUserPage(IPage<Users> page, @Param("query") UserDTO queryDTO);

    /**
     * 根据手机号查询用户（精确匹配）
     * 
     * @param phone 手机号（必填）
     * @return 用户信息（若无返回null）
     */
    Users selectByPhone(@Param("phone") String phone);

    /**
     * 统计用户状态分布（用于数据看板）
     * 
     * @return 各状态用户数量统计结果
     */
    List<Map<String, Object>> countUserStatus();

    /**
     * 用户登录查询（支持用户名/手机号登录）
     * 
     * @param account 用户名或手机号（必填）
     * @return 用户实体（仅返回有效用户）
     */
    Users selectByUsernameOrPhone(@Param("account") String account);

    /**
     * 更新用户状态（管理员操作）
     * 
     * @param userId 用户ID（必填）
     * @param status 新状态（1-正常 0-禁用）
     * @return 影响的行数（0表示操作失败）
     */
    int updateUserStatus(@Param("userId") Long userId, @Param("status") Integer status);

    /**
     * 检查字段唯一性（注册/更新时校验）
     * 
     * @param field     字段名（username/phone/email）
     * @param value     字段值
     * @param excludeId 需要排除的ID（更新时使用）
     * @return 存在重复返回1，否则返回0
     */
    int checkFieldUnique(@Param("field") String field,
                        @Param("value") String value,
                        @Param("excludeId") Long excludeId);

    /**
     * 查询有效用户（通过手机号）
     * 
     * @param phone 手机号（必填）
     * @return 有效用户信息（仅状态为1的用户）
     */
    @Select("SELECT * FROM users WHERE phone = #{phone} AND status = 1")
    Users selectActiveUserByPhone(@Param("phone") String phone);
}




