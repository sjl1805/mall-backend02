package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.entity.Cart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 购物车数据访问层接口
 */
@Mapper
public interface CartMapper extends BaseMapper<Cart> {
    
    /**
     * 获取用户的购物车列表
     *
     * @param userId 用户ID
     * @return 购物车列表
     */
    @Select("SELECT * FROM cart WHERE user_id = #{userId} ORDER BY create_time DESC")
    List<Cart> findByUserId(@Param("userId") Long userId);
    
    /**
     * 获取用户已选中的购物车列表
     *
     * @param userId 用户ID
     * @return 已选中的购物车列表
     */
    @Select("SELECT * FROM cart WHERE user_id = #{userId} AND checked = 1 ORDER BY create_time DESC")
    List<Cart> findCheckedByUserId(@Param("userId") Long userId);
    
    /**
     * 查询特定商品是否在购物车中
     *
     * @param userId 用户ID
     * @param productId 商品ID
     * @param skuId SKU ID
     * @return 购物车项
     */
    @Select("SELECT * FROM cart WHERE user_id = #{userId} AND product_id = #{productId} AND sku_id = #{skuId} LIMIT 1")
    Cart findExistCart(@Param("userId") Long userId, @Param("productId") Long productId, @Param("skuId") Long skuId);
    
    /**
     * 更新购物车商品数量
     *
     * @param id 购物车ID
     * @param quantity 商品数量
     * @return 影响行数
     */
    @Update("UPDATE cart SET quantity = #{quantity}, update_time = NOW() WHERE id = #{id}")
    int updateQuantity(@Param("id") Long id, @Param("quantity") Integer quantity);
    
    /**
     * 全选/取消全选购物车
     *
     * @param userId 用户ID
     * @param checked 选中状态：0-未选中 1-已选中
     * @return 影响行数
     */
    @Update("UPDATE cart SET checked = #{checked}, update_time = NOW() WHERE user_id = #{userId}")
    int updateCheckedByUserId(@Param("userId") Long userId, @Param("checked") Integer checked);
} 