package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.model.dto.product.ProductsPageDTO;
import com.example.model.entity.Products;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 商品管理Mapper接口
 * 实现商品的CRUD操作、库存管理和多条件搜索
 * 
 * @author 毕业设计学生
 */
public interface ProductsMapper extends BaseMapper<Products> {

    /**
     * 分页查询商品（支持多条件过滤和动态排序）
     * 
     * @param page     分页参数（包含当前页、每页数量）
     * @param queryDTO 查询条件DTO（包含关键词、分类、价格区间等）
     * @return 分页结果（包含商品列表和分页信息）
     */
    IPage<Products> selectProductPage(IPage<Products> page, @Param("query") ProductsPageDTO queryDTO);

    /**
     * 更新商品状态（上下架操作）
     * 
     * @param productId 商品ID（必填）
     * @param status    新状态（1-上架 0-下架）
     * @return 影响的行数（0表示操作失败）
     */
    int updateStatus(@Param("productId") Long productId, @Param("status") Integer status);

    /**
     * 安全调整库存（防止超卖）
     * 
     * @param productId 商品ID（必填）
     * @param delta     库存变化量（正数增加，负数减少）
     * @return 影响的行数（0表示库存不足或商品未上架）
     */
    int adjustStock(@Param("productId") Long productId, @Param("delta") Integer delta);

    /**
     * 检查商品名称唯一性（用于新增/修改校验）
     * 
     * @param name      商品名称（必填）
     * @param excludeId 需要排除的ID（修改时使用）
     * @return 存在相同名称返回1，否则返回0
     */
    int checkNameUnique(@Param("name") String name,
                        @Param("excludeId") Long excludeId);

    /**
     * 获取新品推荐（按分类查询最新上架商品）
     * 
     * @param categoryId 分类ID（必填）
     * @param limit      最大返回数量（1-20）
     * @return 按创建时间倒序排列的商品列表
     */
    @Select("SELECT * FROM products WHERE category_id = #{categoryId} AND status = 1 ORDER BY create_time DESC LIMIT #{limit}")
    List<Products> selectNewArrivals(@Param("categoryId") Long categoryId,
                                   @Param("limit") Integer limit);
}




