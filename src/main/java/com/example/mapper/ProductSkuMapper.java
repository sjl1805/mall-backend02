package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.model.entity.ProductSku;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 31815
 * @description 针对表【product_sku(商品SKU表)】的数据库操作Mapper
 * @createDate 2025-02-24 12:04:01
 * @Entity model.entity.ProductSku
 */
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
}




