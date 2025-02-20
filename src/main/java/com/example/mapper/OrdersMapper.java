package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.model.dto.OrdersDTO;
import com.example.model.dto.PageDTO;
import com.example.model.entity.Orders;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

/**
 * 订单管理Mapper接口
 * 实现订单的全生命周期管理，包括创建、支付、发货、完成等状态流转
 * 
 * @author 毕业设计学生
 */
public interface OrdersMapper extends BaseMapper<Orders> {

    /**
     * 分页查询订单（支持多条件过滤和动态排序）
     * 
     * @param page     分页参数（包含当前页、每页数量）
     * @param queryDTO 查询条件DTO（包含订单号、状态等）
     * @return 分页结果（包含订单列表和分页信息）
     */
    IPage<Orders> selectOrderPage(IPage<Orders> page,
                                 @Param("query") PageDTO<OrdersDTO> queryDTO);

    /**
     * 用户更新订单状态（如取消订单）
     * 
     * @param userId  用户ID（用于权限校验）
     * @param orderId 订单ID（必填）
     * @param status  新状态（需符合状态流转规则）
     * @return 影响的行数（0表示操作失败）
     */
    @Update("UPDATE orders SET status = #{status}, update_time = NOW() WHERE id = #{orderId} AND user_id = #{userId}")
    int updateStatusByUser(@Param("userId") Long userId,
                          @Param("orderId") Long orderId,
                          @Param("status") Integer status);

    /**
     * 更新物流信息（商家发货时调用）
     * 
     * @param orderId          订单ID（必填）
     * @param logisticsCompany 物流公司名称
     * @param trackingNumber   物流单号
     * @return 成功更新的记录数
     */
    @Update("UPDATE orders SET logistics_company = #{logisticsCompany}, tracking_number = #{trackingNumber}, status = 2, delivery_time = NOW() WHERE id = #{orderId}")
    int updateDeliveryInfo(@Param("orderId") Long orderId,
                         @Param("logisticsCompany") String logisticsCompany,
                         @Param("trackingNumber") String trackingNumber);

    /**
     * 自动取消超时未支付订单（定时任务调用）
     * 
     * @param timeoutMinutes 超时时间（分钟）
     * @return 成功取消的订单数量
     */
    @Update("UPDATE orders SET status = 4 WHERE status = 0 AND TIMESTAMPDIFF(MINUTE, create_time, NOW()) > #{timeoutMinutes}")
    int autoCancelOrders(@Param("timeoutMinutes") Integer timeoutMinutes);

    /**
     * 标记订单已评价（防止重复评价）
     * 
     * @param userId  用户ID（用于权限校验）
     * @param orderId 订单ID（必填）
     * @return 影响的行数（0表示操作失败）
     */
    @Update("UPDATE orders SET comment_status = 1 WHERE id = #{orderId} AND user_id = #{userId}")
    int markCommented(@Param("userId") Long userId,
                     @Param("orderId") Long orderId);

    /**
     * 统计订单金额（用于生成销售报表）
     * 
     * @param queryDTO 统计条件DTO（包含时间范围、状态等）
     * @return 符合条件的订单总金额（BigDecimal类型保证精度）
     */
    BigDecimal sumOrderAmount(@Param("query") PageDTO<OrdersDTO> queryDTO);
}




