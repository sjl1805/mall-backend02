package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.dto.order.OrderItemDTO;
import com.example.model.entity.OrderItem;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 订单项管理Mapper接口
 * 实现订单项的批量操作、数据统计和状态管理
 * 
 * @author 毕业设计学生
 */
public interface OrderItemMapper extends BaseMapper<OrderItem> {

    /**
     * 批量插入订单项（订单创建时使用）
     * 
     * @param items 订单项DTO列表（包含商品详细信息）
     * @return 成功插入的记录数
     */
    int batchInsert(@Param("items") List<OrderItemDTO> items);

    /**
     * 根据订单ID查询订单明细（用于订单详情展示）
     * 
     * @param orderId 订单ID（必填）
     * @return 订单项列表（按创建时间倒序排列）
     */
    List<OrderItem> selectByOrderId(@Param("orderId") Long orderId);

    /**
     * 统计商品总销量（用于商品详情页展示）
     * 
     * @param productId 商品ID（必填）
     * @return 该商品的历史总销量
     */
    int sumProductSales(@Param("productId") Long productId);

    /**
     * 更新评价状态（用户提交评价后调用）
     * 
     * @param orderId       订单ID（用于定位订单）
     * @param productId     商品ID（用于定位商品）
     * @param commentStatus 新评价状态（0-未评价 1-已评价）
     * @return 影响的行数
     */
    @Update("UPDATE order_item SET comment_status = #{commentStatus} WHERE order_id = #{orderId} AND product_id = #{productId}")
    int updateCommentStatus(@Param("orderId") Long orderId,
                           @Param("productId") Long productId,
                           @Param("commentStatus") Integer commentStatus);

    /**
     * 批量更新商品图片（商品信息变更时同步更新）
     * 
     * @param productId 商品ID（必填）
     * @param imageUrl  新的商品图片URL
     * @return 更新的记录数
     */
    @Update("UPDATE order_item SET product_image = #{imageUrl} WHERE product_id = #{productId}")
    int batchUpdateProductImage(@Param("productId") Long productId,
                               @Param("imageUrl") String imageUrl);
}




