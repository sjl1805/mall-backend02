package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.entity.Cart;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 购物车Mapper接口
 */
@Mapper
public interface CartMapper extends BaseMapper<Cart> {
    
    /**
     * 根据用户ID查询购物车列表
     * @param userId 用户ID
     * @return 购物车列表
     */
    List<Cart> selectByUserId(@Param("userId") Long userId);
    
    /**
     * 根据用户ID和商品ID查询购物车项
     * @param userId 用户ID
     * @param productId 商品ID
     * @param skuId SKU ID
     * @return 购物车项
     */
    Cart selectByUserIdAndProductId(
        @Param("userId") Long userId, 
        @Param("productId") Long productId, 
        @Param("skuId") Long skuId
    );
    
    /**
     * 更新购物车商品数量
     * @param id 购物车ID
     * @param quantity 新数量
     * @return 影响行数
     */
    int updateQuantity(@Param("id") Long id, @Param("quantity") Integer quantity);
    
    /**
     * 更新购物车项选中状态
     * @param id 购物车ID
     * @param checked 选中状态
     * @return 影响行数
     */
    int updateCheckedStatus(@Param("id") Long id, @Param("checked") Integer checked);
    
    /**
     * 批量更新购物车项选中状态
     * @param userId 用户ID
     * @param checked 选中状态
     * @return 影响行数
     */
    int updateAllCheckedStatus(@Param("userId") Long userId, @Param("checked") Integer checked);
    
    /**
     * 删除购物车项
     * @param id 购物车ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 清空用户购物车
     * @param userId 用户ID
     * @return 影响行数
     */
    int clearCart(@Param("userId") Long userId);
    
    /**
     * 查询用户购物车商品总数
     * @param userId 用户ID
     * @return 购物车商品总数
     */
    int countItems(@Param("userId") Long userId);
    
    /**
     * 查询用户购物车已选中的商品项
     * @param userId 用户ID
     * @return 已选中的购物车项列表
     */
    List<Cart> selectCheckedItems(@Param("userId") Long userId);
    
    /**
     * 批量插入购物车项
     * @param cartList 购物车项列表
     * @return 影响行数
     */
    int batchInsert(@Param("list") List<Cart> cartList);
    
    /**
     * 从购物车查询用户选中的商品总价
     * @param userId 用户ID
     * @return 选中的商品总价
     */
    @MapKey("productId")
    Map<String, Object> selectCartAmount(@Param("userId") Long userId);
    
    /**
     * 查询购物车项是否存在
     * @param userId 用户ID
     * @param productId 商品ID
     * @param skuId SKU ID
     * @return 是否存在
     */
    int existsCartItem(
        @Param("userId") Long userId, 
        @Param("productId") Long productId, 
        @Param("skuId") Long skuId
    );
    
    /**
     * 删除已选中的购物车项
     * @param userId 用户ID
     * @return 影响行数
     */
    int deleteCheckedItems(@Param("userId") Long userId);
    
    /**
     * 合并购物车
     * @param fromUserId 源用户ID
     * @param toUserId 目标用户ID
     * @return 影响行数
     */
    int mergeCart(@Param("fromUserId") Long fromUserId, @Param("toUserId") Long toUserId);
} 