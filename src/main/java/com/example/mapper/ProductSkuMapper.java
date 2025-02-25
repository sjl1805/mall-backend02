package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.model.entity.ProductSku;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 31815
 * @description 针对表【product_sku(商品SKU表)】的数据库操作Mapper
 * @createDate 2025-02-24 12:04:01
 * @Entity model.entity.ProductSku
 */
@Mapper
public interface ProductSkuMapper extends BaseMapper<ProductSku> {

    /**
     * 根据商品ID查询SKU
     *
     * @param productId 商品ID
     * @return SKU列表
     */
    List<ProductSku> selectByProductId(@Param("productId") Long productId);

    /**
     * 分页查询SKU
     *
     * @param page 分页信息
     * @return SKU列表
     */
    IPage<ProductSku> selectPage(IPage<ProductSku> page);

    /**
     * 根据ID查询SKU
     *
     * @param id SKU ID
     * @return SKU信息
     */
    ProductSku selectById(@Param("id") Long id);

    /**
     * 插入新SKU
     *
     * @param productSku SKU信息
     * @return 插入结果
     */
    int insertProductSku(ProductSku productSku);

    /**
     * 更新SKU信息
     *
     * @param productSku SKU信息
     * @return 更新结果
     */
    int updateProductSku(ProductSku productSku);

    /**
     * 根据ID删除SKU
     *
     * @param id SKU ID
     * @return 删除结果
     */
    int deleteProductSku(@Param("id") Long id);

    /**
     * 根据规格值精确匹配SKU
     *
     * @param productId  商品ID
     * @param specValues 规格值JSON字符串
     * @return 匹配的SKU
     */
    ProductSku selectBySpecValues(@Param("productId") Long productId, @Param("specValues") String specValues);

    /**
     * 查询可售SKU（有库存且上架）
     *
     * @param productId 商品ID
     * @return 可售SKU列表
     */
    List<ProductSku> selectAvailableSkus(@Param("productId") Long productId);

    /**
     * 减少SKU库存
     *
     * @param id       SKU ID
     * @param quantity 减少数量
     * @return 更新结果
     */
    int decreaseStock(@Param("id") Long id, @Param("quantity") Integer quantity);

    /**
     * 增加SKU销量
     *
     * @param id       SKU ID
     * @param quantity 增加数量
     * @return 更新结果
     */
    int increaseSales(@Param("id") Long id, @Param("quantity") Integer quantity);

    /**
     * 批量插入SKU
     *
     * @param skuList SKU列表
     * @return 插入结果
     */
    int batchInsertSkus(@Param("skuList") List<ProductSku> skuList);

    /**
     * 根据价格区间查询SKU
     *
     * @param productId 商品ID
     * @param minPrice  最低价格
     * @param maxPrice  最高价格
     * @return SKU列表
     */
    List<ProductSku> selectByPriceRange(
            @Param("productId") Long productId,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice);
}




