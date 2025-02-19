package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.model.dto.product.ProductSkuPageDTO;
import com.example.model.entity.ProductSku;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * @author 31815
 * @description 针对表【product_sku(商品SKU表)】的数据库操作Mapper
 * @createDate 2025-02-18 23:44:08
 * @Entity model.entity.ProductSku
 */
public interface ProductSkuMapper extends BaseMapper<ProductSku> {

    /**
     * 批量插入SKU
     *
     * @param skus SKU列表
     * @return 插入数量
     */
    int batchInsert(@Param("skus") List<ProductSku> skus);

    /**
     * 根据商品ID查询SKU
     *
     * @param productId 商品ID
     * @return SKU列表
     */
    List<ProductSku> selectByProductId(@Param("productId") Long productId);

    /**
     * 更新SKU库存
     *
     * @param skuId    SKU ID
     * @param quantity 变化数量（正数增加，负数减少）
     * @return 影响行数
     */
    int updateStock(@Param("skuId") Long skuId,
                    @Param("quantity") Integer quantity);

    /**
     * 分页查询SKU列表
     *
     * @param page  分页参数
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<ProductSku> selectSkuPage(IPage<ProductSku> page,
                                    @Param("query") ProductSkuPageDTO queryDTO);

    /**
     * 增加销量
     *
     * @param skuId    SKU ID
     * @param quantity 销售数量
     * @return 影响行数
     */
    int increaseSales(@Param("skuId") Long skuId,
                      @Param("quantity") Integer quantity);

    /**
     * 统计SKU状态数量
     *
     * @param productId 商品ID
     * @return 各状态数量
     */
    List<Map<String, Object>> countSkuStatus(@Param("productId") Long productId);

    @Update("UPDATE product_sku SET stock = stock + #{quantity} " +
            "WHERE id = #{skuId} AND product_id = #{productId} " +
            "AND stock + #{quantity} >= 0")
    int updateStockSafely(@Param("productId") Long productId,
                          @Param("skuId") Long skuId,
                          @Param("quantity") Integer quantity);

    @Update("UPDATE product_sku SET main_image = #{imageUrl} " +
            "WHERE product_id = #{productId} AND main_image = #{oldImage}")
    int batchUpdateMainImage(@Param("productId") Long productId,
                             @Param("oldImage") String oldImage,
                             @Param("imageUrl") String imageUrl);
}




