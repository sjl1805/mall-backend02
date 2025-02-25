package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.model.entity.ProductSpec;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 31815
 * @description 针对表【product_spec(商品规格表)】的数据库操作Mapper
 * @createDate 2025-02-24 12:03:58
 * @Entity model.entity.ProductSpec
 */
@Mapper
public interface ProductSpecMapper extends BaseMapper<ProductSpec> {

    /**
     * 根据商品ID查询规格
     *
     * @param productId 商品ID
     * @return 商品规格列表
     */
    List<ProductSpec> selectByProductId(@Param("productId") Long productId);

    /**
     * 分页查询商品规格
     *
     * @param page 分页信息
     * @return 商品规格列表
     */
    IPage<ProductSpec> selectPage(IPage<ProductSpec> page);

    /**
     * 根据ID查询商品规格
     *
     * @param id 商品规格ID
     * @return 商品规格信息
     */
    ProductSpec selectById(@Param("id") Long id);

    /**
     * 插入新商品规格
     *
     * @param productSpec 商品规格信息
     * @return 插入结果
     */
    int insertProductSpec(ProductSpec productSpec);

    /**
     * 更新商品规格信息
     *
     * @param productSpec 商品规格信息
     * @return 更新结果
     */
    int updateProductSpec(ProductSpec productSpec);

    /**
     * 根据ID删除商品规格
     *
     * @param id 商品规格ID
     * @return 删除结果
     */
    int deleteProductSpec(@Param("id") Long id);

    /**
     * 批量插入商品规格
     *
     * @param specList 商品规格列表
     * @return 插入结果
     */
    int batchInsertProductSpecs(@Param("specList") List<ProductSpec> specList);

    /**
     * 批量删除商品规格
     *
     * @param ids 规格ID列表
     * @return 删除结果
     */
    int batchDeleteProductSpecs(@Param("ids") List<Long> ids);

    /**
     * 根据规格值查询商品规格
     *
     * @param productId 商品ID
     * @param specValue 规格值
     * @return 商品规格列表
     */
    List<ProductSpec> selectBySpecValue(@Param("productId") Long productId, @Param("specValue") String specValue);
}




