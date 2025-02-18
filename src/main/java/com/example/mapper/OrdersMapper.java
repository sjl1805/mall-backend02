package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.model.dto.order.OrdersPageDTO;
import com.example.model.entity.Orders;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import java.math.BigDecimal;

/**
* @author 31815
* @description 针对表【orders(订单表)】的数据库操作Mapper
* @createDate 2025-02-18 23:44:18
* @Entity model.entity.Orders
*/
public interface OrdersMapper extends BaseMapper<Orders> {

    /**
     * 分页查询订单列表（带条件）
     * @param page 分页参数
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    IPage<Orders> selectOrderPage(IPage<Orders> page, 
                                @Param("query") OrdersPageDTO queryDTO);

    /**
     * 更新订单状态
     * @param userId 用户ID
     * @param orderId 订单ID
     * @param status 新状态
     * @return 影响行数
     */
    @Update("UPDATE orders SET status = #{status}, update_time = NOW() WHERE id = #{orderId} AND user_id = #{userId}")
    int updateStatusByUser(@Param("userId") Long userId,
                         @Param("orderId") Long orderId,
                         @Param("status") Integer status);

    /**
     * 更新物流信息
     * @param orderId 订单ID
     * @param logisticsCompany 物流公司
     * @param trackingNumber 运单号
     * @return 影响行数
     */
    @Update("UPDATE orders SET logistics_company = #{logisticsCompany}, tracking_number = #{trackingNumber}, status = 2, delivery_time = NOW() WHERE id = #{orderId}")
    int updateDeliveryInfo(@Param("orderId") Long orderId,
                          @Param("logisticsCompany") String logisticsCompany,
                          @Param("trackingNumber") String trackingNumber);

    /**
     * 自动取消超时订单
     * @param timeoutMinutes 超时时间（分钟）
     * @return 取消数量
     */
    @Update("UPDATE orders SET status = 4 WHERE status = 0 AND TIMESTAMPDIFF(MINUTE, create_time, NOW()) > #{timeoutMinutes}")
    int autoCancelOrders(@Param("timeoutMinutes") Integer timeoutMinutes);

    /**
     * 标记订单已评论
     * @param userId 用户ID
     * @param orderId 订单ID
     * @return 影响行数
     */
    @Update("UPDATE orders SET comment_status = 1 WHERE id = #{orderId} AND user_id = #{userId}")
    int markCommented(@Param("userId") Long userId,
                     @Param("orderId") Long orderId);

    /**
     * 统计订单金额
     * @param query 统计条件
     * @return 统计结果
     */
    BigDecimal sumOrderAmount(@Param("query") OrdersPageDTO queryDTO);
}




