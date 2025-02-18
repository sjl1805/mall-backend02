package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.dto.CartQuery;
import com.example.model.entity.Cart;

import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 购物车管理Mapper接口
 * @author 毕业设计学生
 */
public interface CartMapper extends BaseMapper<Cart> {

    /**
     * 批量更新选中状态
     * @param userId 用户ID
     * @param productIds 商品ID列表
     * @param checked 选中状态
     * @return 影响行数
     */
    int batchUpdateChecked(@Param("userId") Long userId,
                          @Param("productIds") List<Long> productIds,
                          @Param("checked") Integer checked);

    /**
     * 清空选中商品
     * @param userId 用户ID
     * @return 删除数量
     */
    int clearCheckedItems(@Param("userId") Long userId);

    /**
     * 分页查询购物车列表（带条件）
     * @param query 查询条件
     * @return 购物车列表
     */
    List<Cart> selectCartList(@Param("query") CartQuery query);

    /**
     * 统计购物车商品数量
     * @param userId 用户ID
     * @return 商品总数
     */
    int countCartItems(@Param("userId") Long userId);
}




