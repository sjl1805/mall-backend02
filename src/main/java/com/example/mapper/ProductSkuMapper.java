package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.entity.ProductSku;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author 31815
 * @description 针对表【product_sku(商品SKU表)】的数据库操作Mapper
 * @createDate 2025-02-24 12:04:01
 * @Entity model.entity.ProductSku
 */
public interface ProductSkuMapper extends BaseMapper<ProductSku> {

    /**
     * 批量更新SKU状态
     * @param ids SKU ID列表
     * @param status 新状态
     * @return 影响行数
     */
    int updateSkuStatusBatch(@Param("ids") List<Long> ids, @Param("status") Integer status);

    /**
     * 价格/库存联合查询
     * @param minPrice 最低价格
     * @param maxPrice 最高价格
     * @param hasStock 是否有库存
     * @return SKU列表
     */
    List<ProductSku> selectByPriceAndStock(@Param("minPrice") BigDecimal minPrice, 
                                          @Param("maxPrice") BigDecimal maxPrice,
                                          @Param("hasStock") Boolean hasStock);

    /**
     * 获取销量排行榜
     * @param limit 排名数量
     * @return 包含SKU信息、商品名称和销量的Map列表
     */
    List<Map<String, Object>> selectSalesRank(@Param("limit") int limit);

    /**
     * 更新主图默认值
     * @param productId 商品ID
     * @param defaultImage 默认图片URL
     * @return 影响行数
     */
    int updateMainImageDefault(@Param("productId") Long productId, 
                              @Param("defaultImage") String defaultImage);

}




