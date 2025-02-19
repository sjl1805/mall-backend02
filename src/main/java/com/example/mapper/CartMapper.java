package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.model.dto.cart.CartPageDTO;
import com.example.model.entity.Cart;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 购物车管理Mapper接口
 * 实现购物车商品管理、选中状态操作、批量处理等功能
 * 
 * @author 毕业设计学生
 */
public interface CartMapper extends BaseMapper<Cart> {

    /**
     * 批量更新选中状态（支持多商品操作）
     * 
     * @param userId     用户ID（必填）
     * @param productIds 商品ID列表（至少包含1个元素）
     * @param checked    选中状态（0-未选中 1-已选中）
     * @return 成功更新的记录数
     */
    int batchUpdateChecked(@Param("userId") Long userId,
                           @Param("productIds") List<Long> productIds,
                           @Param("checked") Integer checked);

    /**
     * 清空用户已选中的购物车商品（下单后调用）
     * 
     * @param userId 用户ID（必填）
     * @return 删除的记录数
     */
    int clearCheckedItems(@Param("userId") Long userId);

    /**
     * 分页查询购物车列表（带多条件过滤）
     * 
     * @param page    分页参数（包含当前页、每页数量）
     * @param queryDTO 查询条件DTO（包含用户ID、商品ID等）
     * @return 分页结果（包含购物车商品列表和分页信息）
     */
    IPage<Cart> selectCartPage(IPage<Cart> page,
                               @Param("query") CartPageDTO queryDTO);

    /**
     * 调整商品数量（增加或减少）
     * 
     * @param userId  用户ID（用于校验权限）
     * @param cartId  购物车记录ID
     * @param delta   数量变化值（正数增加，负数减少）
     * @return 影响的行数（0表示操作失败）
     */
    @Update("UPDATE cart SET quantity = quantity + #{delta} WHERE id = #{cartId} AND user_id = #{userId}")
    int adjustQuantity(@Param("userId") Long userId,
                      @Param("cartId") Long cartId,
                      @Param("delta") Integer delta);

    /**
     * 检查购物车商品是否存在（防止重复添加）
     * 
     * @param userId    用户ID
     * @param productId 商品ID
     * @return 存在返回1，否则返回0
     */
    int checkCartItemExists(@Param("userId") Long userId,
                           @Param("productId") Long productId);

    /**
     * 批量删除购物车商品（支持多选删除）
     * 
     * @param cartIds 购物车记录ID列表
     * @return 成功删除的记录数
     */
    int batchDelete(@Param("cartIds") List<Long> cartIds);

    /**
     * 统计用户购物车商品总数（用于显示角标）
     * 
     * @param userId 用户ID
     * @return 当前用户的购物车商品总数
     */
    int countCartItems(@Param("userId") Long userId);

    /**
     * 更新单个商品的选中状态
     * 
     * @param userId  用户ID（用于权限校验）
     * @param cartId  购物车记录ID
     * @param checked 新选中状态（0/1）
     * @return 影响的行数
     */
    @Update("UPDATE cart SET checked = #{checked} WHERE user_id = #{userId} AND id = #{cartId}")
    int updateCheckedStatus(@Param("userId") Long userId,
                           @Param("cartId") Long cartId,
                           @Param("checked") Integer checked);
}




