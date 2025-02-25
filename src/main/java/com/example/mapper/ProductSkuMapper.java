package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.model.entity.ProductSku;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 商品SKU Mapper接口
 */
@Mapper
public interface ProductSkuMapper extends BaseMapper<ProductSku> {
    
    /**
     * 根据商品ID查询所有SKU
     * @param productId 商品ID
     * @return SKU列表
     */
    List<ProductSku> selectByProductId(@Param("productId") Long productId);
    
    /**
     * 根据SKU编码查询
     * @param skuCode SKU编码
     * @return SKU信息
     */
    ProductSku selectBySkuCode(@Param("skuCode") String skuCode);
    
    /**
     * 更新SKU库存
     * @param id SKU ID
     * @param stock 新库存数量
     * @return 影响行数
     */
    int updateStock(@Param("id") Long id, @Param("stock") Integer stock);
    
    /**
     * 批量更新SKU库存
     * @param skuStockMap SKU库存映射(key是SKU ID，value是库存量)
     * @return 影响行数
     */
    int batchUpdateStock(@Param("skuStockMap") Map<Long, Integer> skuStockMap);
    
    /**
     * 增加或减少SKU库存
     * @param id SKU ID
     * @param amount 变动数量(正数增加，负数减少)
     * @return 影响行数
     */
    int incrementStock(@Param("id") Long id, @Param("amount") Integer amount);
    
    /**
     * 查询库存低于指定值的SKU
     * @param threshold 库存阈值
     * @return 低库存SKU列表
     */
    List<ProductSku> selectLowStock(@Param("threshold") Integer threshold);
    
    /**
     * 更新SKU价格
     * @param id SKU ID
     * @param price 新价格
     * @return 影响行数
     */
    int updatePrice(@Param("id") Long id, @Param("price") BigDecimal price);
    
    /**
     * 批量插入SKU
     * @param skuList SKU列表
     * @return 影响行数
     */
    int batchInsert(@Param("list") List<ProductSku> skuList);
    
    /**
     * 根据商品ID查询SKU，分页
     * @param page 分页参数
     * @param productId 商品ID
     * @return 分页结果
     */
    IPage<ProductSku> selectPageByProductId(Page<ProductSku> page, @Param("productId") Long productId);
    
    /**
     * 获取商品的价格范围
     * @param productId 商品ID
     * @return 包含最低价(minPrice)和最高价(maxPrice)的Map
     */
    @MapKey("productId")
    Map<String, BigDecimal> selectPriceRange(@Param("productId") Long productId);
    
    /**
     * 根据多个商品ID批量查询SKU
     * @param productIds 商品ID列表
     * @return SKU列表
     */
    List<ProductSku> selectByProductIds(@Param("productIds") List<Long> productIds);
    
    /**
     * 查询商品的可用SKU数量
     * @param productId 商品ID
     * @return 可用SKU数量
     */
    int countAvailableSkus(@Param("productId") Long productId);
} 