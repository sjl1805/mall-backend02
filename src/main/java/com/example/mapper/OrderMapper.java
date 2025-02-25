package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.model.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 订单数据访问层接口
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {
    
    /**
     * 获取用户的订单列表
     *
     * @param userId 用户ID
     * @param page 分页参数
     * @return 分页订单列表
     */
    @Select("SELECT * FROM orders WHERE user_id = #{userId} ORDER BY create_time DESC")
    IPage<Order> findByUserId(Page<Order> page, @Param("userId") Long userId);
    
    /**
     * 根据订单号查询订单
     *
     * @param orderNo 订单号
     * @return 订单
     */
    @Select("SELECT * FROM orders WHERE order_no = #{orderNo}")
    Order findByOrderNo(@Param("orderNo") String orderNo);
    
    /**
     * 更新订单状态
     *
     * @param id 订单ID
     * @param status 订单状态
     * @return 影响行数
     */
    @Update("UPDATE orders SET status = #{status}, update_time = NOW() WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
    
    /**
     * 获取指定状态的订单列表
     *
     * @param status 订单状态
     * @return 订单列表
     */
    @Select("SELECT * FROM orders WHERE status = #{status} ORDER BY create_time DESC")
    List<Order> findByStatus(@Param("status") Integer status);
} 