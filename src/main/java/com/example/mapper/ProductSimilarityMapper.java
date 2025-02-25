package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.entity.ProductSimilarity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 商品相似度数据访问层接口
 */
@Mapper
public interface ProductSimilarityMapper extends BaseMapper<ProductSimilarity> {
    
    /**
     * 获取与指定商品相似度最高的商品列表
     *
     * @param productId 商品ID
     * @param limit 限制数量
     * @return 商品相似度列表
     */
    @Select("SELECT * FROM product_similarity WHERE product_id_a = #{productId} " +
            "ORDER BY similarity DESC LIMIT #{limit}")
    List<ProductSimilarity> findSimilarProducts(@Param("productId") Long productId, @Param("limit") Integer limit);
    
    /**
     * 获取两个商品之间的相似度
     *
     * @param productIdA 商品A ID
     * @param productIdB 商品B ID
     * @return 商品相似度
     */
    @Select("SELECT * FROM product_similarity WHERE " +
            "(product_id_a = #{productIdA} AND product_id_b = #{productIdB}) OR " +
            "(product_id_a = #{productIdB} AND product_id_b = #{productIdA}) LIMIT 1")
    ProductSimilarity findBetweenProducts(@Param("productIdA") Long productIdA, @Param("productIdB") Long productIdB);
} 