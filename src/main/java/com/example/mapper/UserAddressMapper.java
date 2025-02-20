package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.model.dto.PageDTO;
import com.example.model.dto.UserAddressDTO;
import com.example.model.entity.UserAddress;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 用户地址管理Mapper接口
 * 实现地址的增删改查、默认地址管理和多条件搜索
 * 
 * @author 毕业设计学生
 */
public interface UserAddressMapper extends BaseMapper<UserAddress> {

    /**
     * 分页查询地址（支持多条件过滤）
     * 
     * @param page     分页参数（包含当前页、每页数量）
     * @param queryDTO 查询条件DTO（包含用户ID、是否默认等）
     * @return 分页结果（包含地址列表和分页信息）
     */
    IPage<UserAddress> selectAddressPage(IPage<UserAddress> page,
                                         @Param("query") PageDTO<UserAddressDTO> queryDTO);

    /**
     * 设置用户默认地址（事务操作）
     * 
     * @param userId    用户ID（必填）
     * @param addressId 地址ID（必填）
     * @return 影响的行数（0表示操作失败）
     */
    @Update("UPDATE user_address SET is_default = 1 WHERE id = #{addressId} AND user_id = #{userId}")
    int setDefaultAddress(@Param("userId") Long userId,
                          @Param("addressId") Long addressId);

    /**
     * 清除用户的默认地址（设置新默认地址前调用）
     * 
     * @param userId 用户ID（必填）
     * @return 影响的行数
     */
    @Update("UPDATE user_address SET is_default = 0 WHERE user_id = #{userId}")
    int clearDefaultAddress(@Param("userId") Long userId);

    /**
     * 查询用户默认地址（下单时使用）
     * 
     * @param userId 用户ID（必填）
     * @return 默认地址信息（若无返回null）
     */
    UserAddress selectDefaultByUserId(@Param("userId") Long userId);

    /**
     * 更新默认地址状态（原子操作）
     * 
     * @param userId    用户ID（必填）
     * @param addressId 地址ID（必填）
     * @return 影响的行数（0表示操作失败）
     */
    @Update("UPDATE user_address SET is_default = 1 WHERE user_id = #{userId} AND id = #{addressId}")
    int updateDefaultStatus(@Param("userId") Long userId,
                            @Param("addressId") Long addressId);
}




