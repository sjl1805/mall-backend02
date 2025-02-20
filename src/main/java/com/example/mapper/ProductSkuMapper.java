package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.model.dto.PageDTO;
import com.example.model.dto.ProductSkuDTO;
import com.example.model.entity.ProductSku;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * 商品SKU管理Mapper接口
 * 实现SKU的库存管理、价格调整和批量操作
 * 
 * @author 毕业设计学生
 */
public interface ProductSkuMapper extends BaseMapper<ProductSku> {

    /**
     * 批量插入SKU（商品发布时使用）
     * 
     * @param skus SKU实体列表（包含规格、价格等信息）
     * @return 成功插入的记录数
     */
    int batchInsert(@Param("skus") List<ProductSkuDTO> skus);

    /**
     * 根据商品ID查询SKU（按价格升序排列）
     * 
     * @param productId 商品ID（必填）
     * @return SKU列表（包含库存和价格信息）
     */
    List<ProductSku> selectByProductId(@Param("productId") Long productId);

    /**
     * 安全更新库存（防止超卖）
     * 
     * @param skuId    SKU ID（必填）
     * @param quantity 变化数量（正数增加，负数减少）
     * @return 影响的行数（0表示库存不足）
     */
    int updateStock(@Param("skuId") Long skuId,
                   @Param("quantity") Integer quantity);

    /**
     * 分页查询SKU（支持多条件过滤）
     * 
     * @param page     分页参数（包含当前页、每页数量）
     * @param queryDTO 查询条件DTO（包含价格范围、状态等）
     * @return 分页结果（包含SKU列表和分页信息）
     */
    IPage<ProductSku> selectSkuPage(IPage<ProductSku> page,
                                   @Param("query") PageDTO<ProductSkuDTO> queryDTO);

    /**
     * 增加销量（订单完成时调用）
     * 
     * @param skuId    SKU ID（必填）
     * @param quantity 销售数量（正数）
     * @return 影响的行数
     */
    int increaseSales(@Param("skuId") Long skuId,
                     @Param("quantity") Integer quantity);

    /**
     * 统计SKU状态分布（用于商品管理页）
     * 
     * @param productId 商品ID（必填）
     * @return 各状态数量统计结果
     */
    List<Map<String, Object>> countSkuStatus(@Param("productId") Long productId);

    /**
     * 安全更新库存（带商品ID校验）
     * 
     * @param productId 商品ID（用于校验）
     * @param skuId     SKU ID（必填）
     * @param quantity  变化数量（需保证库存不为负）
     * @return 影响的行数（0表示操作失败）
     */
    @Update("UPDATE product_sku SET stock = stock + #{quantity} " +
            "WHERE id = #{skuId} AND product_id = #{productId} " +
            "AND stock + #{quantity} >= 0")
    int updateStockSafely(@Param("productId") Long productId,
                         @Param("skuId") Long skuId,
                         @Param("quantity") Integer quantity);

    /**
     * 批量更新主图（商品信息变更时同步更新）
     * 
     * @param productId 商品ID（必填）
     * @param oldImage  原图片路径（用于匹配）
     * @param imageUrl  新图片路径
     * @return 更新的记录数
     */
    @Update("UPDATE product_sku SET main_image = #{imageUrl} " +
            "WHERE product_id = #{productId} AND main_image = #{oldImage}")
    int batchUpdateMainImage(@Param("productId") Long productId,
                            @Param("oldImage") String oldImage,
                            @Param("imageUrl") String imageUrl);
}




