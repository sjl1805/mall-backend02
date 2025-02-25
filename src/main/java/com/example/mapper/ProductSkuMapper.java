package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.entity.ProductSku;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 商品SKU数据访问层接口
 */
@Mapper
public interface ProductSkuMapper extends BaseMapper<ProductSku> {
    
    /**
     * 根据商品ID查询SKU列表
     *
     * @param productId 商品ID
     * @return SKU列表
     */
    @Select("SELECT * FROM product_sku WHERE product_id = #{productId} AND status = 1")
    List<ProductSku> findByProductId(@Param("productId") Long productId);
    
    /**
     * 根据SKU编码查询SKU
     *
     * @param skuCode SKU编码
     * @return SKU对象
     */
    @Select("SELECT * FROM product_sku WHERE sku_code = #{skuCode}")
    ProductSku findBySkuCode(@Param("skuCode") String skuCode);
    
    /**
     * 减少SKU库存
     *
     * @param skuId SKU ID
     * @param quantity 减少数量
     * @return 影响行数
     */
    @Update("UPDATE product_sku SET stock = stock - #{quantity} WHERE id = #{skuId} AND stock >= #{quantity}")
    int decreaseStock(@Param("skuId") Long skuId, @Param("quantity") Integer quantity);
} 