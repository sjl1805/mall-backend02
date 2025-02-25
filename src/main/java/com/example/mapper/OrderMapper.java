package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.model.entity.Order;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 订单Mapper接口
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {
    
    /**
     * 根据订单号查询订单
     * @param orderNo 订单号
     * @return 订单信息
     */
    Order selectByOrderNo(@Param("orderNo") String orderNo);
    
    /**
     * 根据用户ID查询订单列表
     * @param userId 用户ID
     * @return 订单列表
     */
    List<Order> selectByUserId(@Param("userId") Long userId);
    
    /**
     * 查询用户指定状态的订单
     * @param userId 用户ID
     * @param status 订单状态
     * @return 订单列表
     */
    List<Order> selectByUserIdAndStatus(@Param("userId") Long userId, @Param("status") Integer status);
    
    /**
     * 分页查询用户订单
     * @param page 分页参数
     * @param userId 用户ID
     * @param status 订单状态，可选参数
     * @return 分页订单数据
     */
    IPage<Order> selectUserOrderPage(Page<Order> page, @Param("userId") Long userId, @Param("status") Integer status);
    
    /**
     * 更新订单状态
     * @param orderNo 订单号
     * @param oldStatus 原状态
     * @param newStatus 新状态
     * @return 影响行数
     */
    int updateStatus(@Param("orderNo") String orderNo, @Param("oldStatus") Integer oldStatus, @Param("newStatus") Integer newStatus);
    
    /**
     * 更新订单支付信息
     * @param orderNo 订单号
     * @param paymentTime 支付时间
     * @return 影响行数
     */
    int updatePayment(@Param("orderNo") String orderNo, @Param("paymentTime") LocalDateTime paymentTime);
    
    /**
     * 统计用户各状态订单数量
     * @param userId 用户ID
     * @return 各状态订单数量Map
     */
    @MapKey("status")
    Map<String, Integer> countByStatus(@Param("userId") Long userId);
    
    /**
     * 查找超时未支付订单
     * @param timeoutMinutes 超时分钟数
     * @return 超时订单列表
     */
    List<Order> selectTimeoutOrders(@Param("timeoutMinutes") int timeoutMinutes);
    
    /**
     * 根据时间范围和状态查询订单
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param status 订单状态
     * @return 订单列表
     */
    List<Order> selectByTimeRangeAndStatus(
        @Param("startTime") LocalDateTime startTime, 
        @Param("endTime") LocalDateTime endTime, 
        @Param("status") Integer status
    );
} 