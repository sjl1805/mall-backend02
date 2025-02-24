package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.entity.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 31815
 * @description 针对表【cart(购物车表)】的数据库操作Mapper
 * @createDate 2025-02-24 12:04:25
 * @Entity model.entity.Cart
 */
public interface CartMapper extends BaseMapper<Cart> {

    /**
     * 批量更新选中状态
     * @param userId 用户ID
     * @param productIds 商品ID列表（可选）
     * @param checked 选中状态
     * @return 影响行数
     */
    int updateCheckedStatus(@Param("userId") Long userId,
                           @Param("productIds") List<Long> productIds,
                           @Param("checked") Integer checked);

    /**
     * 修改商品数量
     * @param cartId 购物车项ID
     * @param userId 用户ID
     * @param quantity 新数量
     * @return 影响行数
     */
    int updateQuantity(@Param("cartId") Long cartId,
                      @Param("userId") Long userId,
                      @Param("quantity") Integer quantity);

    /**
     * 查询有效购物车项（关联商品状态和库存）
     * @param userId 用户ID
     * @return 有效购物车项列表
     */
    List<Cart> selectValidCartItems(@Param("userId") Long userId);

    /**
     * 清空已选商品
     * @param userId 用户ID
     * @return 删除行数
     */
    int deleteCheckedItems(@Param("userId") Long userId);

}




