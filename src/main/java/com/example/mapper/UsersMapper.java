package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.model.dto.users.UserPageDTO;
import com.example.model.entity.Users;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 用户管理Mapper接口
 * 使用MyBatis Plus增强功能实现基础CRUD操作
 * 自定义方法需配合XML映射文件实现复杂查询
 * @author 毕业设计学生
 */
public interface UsersMapper extends BaseMapper<Users> {

    /**
     * 分页查询用户列表（支持多条件动态查询）
     * @param page 分页参数对象，包含当前页、每页数量
     * @param queryDTO 查询条件传输对象，包含用户名、手机号、状态等过滤条件
     * @return 分页结果对象，包含数据列表和分页信息
     */
    IPage<Users> selectUserPage(IPage<Users> page, @Param("query") UserPageDTO queryDTO);

    /**
     * 根据手机号查询用户
     * @param phone 手机号
     * @return 用户信息
     */
    Users selectByPhone(@Param("phone") String phone);

    /**
     * 统计用户状态分布
     * @return 状态统计结果列表
     */
    List<Map<String, Object>> countUserStatus();

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
     * 检查字段唯一性（用于注册和更新时的重复校验）
     * @param field 需要检查的字段名（如username、phone、email）
     * @param value 字段值
     * @param excludeId 需要排除的ID（用于更新操作时排除当前记录）
     * @return 存在重复记录的数量
     */
    int checkFieldUnique(@Param("field") String field, 
                        @Param("value") String value,
                        @Param("excludeId") Long excludeId);

    @Select("SELECT COUNT(*) FROM users WHERE ${field} = #{value} AND id != #{excludeId}")
    int checkUnique(@Param("field") String field, 
                   @Param("value") String value,
                   @Param("excludeId") Long excludeId);

    @Select("SELECT * FROM users WHERE phone = #{phone} AND status = 1")
    Users selectActiveUserByPhone(@Param("phone") String phone);
}




