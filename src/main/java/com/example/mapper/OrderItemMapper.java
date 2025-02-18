package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.dto.order.OrderItemDTO;
import com.example.model.entity.OrderItem;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import java.util.List;

/**
 * 订单项管理Mapper接口
 * @author 毕业设计学生
 */
public interface OrderItemMapper extends BaseMapper<OrderItem> {

    /**
     * 批量插入订单项
     * @param items 订单项列表
     * @return 插入数量
     */
    int batchInsert(@Param("items") List<OrderItemDTO> items);

    /**
     * 根据订单ID查询订单项
     * @param orderId 订单ID
     * @return 订单项列表
     */
    List<OrderItem> selectByOrderId(@Param("orderId") Long orderId);

    /**
     * 统计商品销量
     * @param productId 商品ID
     * @return 总销量
     */
    int sumProductSales(@Param("productId") Long productId);

    /**
     * 更新评价状态
     * @param orderId 订单ID
     * @param productId 商品ID
     * @param commentStatus 评价状态
     * @return 影响行数
     */
    @Update("UPDATE order_item SET comment_status = #{commentStatus} WHERE order_id = #{orderId} AND product_id = #{productId}")
    int updateCommentStatus(@Param("orderId") Long orderId,
                           @Param("productId") Long productId,
                           @Param("commentStatus") Integer commentStatus);

    @Update("UPDATE order_item SET product_image = #{imageUrl} WHERE product_id = #{productId}")
    int batchUpdateProductImage(@Param("productId") Long productId,
                               @Param("imageUrl") String imageUrl);
}




