package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.ProductSpec;

import java.util.List;
import java.util.Map;

/**
 * @author 31815
 * @description 针对表【product_spec(商品规格表)】的数据库操作Service
 * @createDate 2025-02-24 12:03:58
 */
public interface ProductSpecService extends IService<ProductSpec> {

    /**
     * 根据商品ID查询商品规格
     *
     * @param productId 商品ID
     * @return 商品规格列表
     */
    List<ProductSpec> selectByProductId(Long productId);

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
    ProductSpec selectById(Long id);

    /**
     * 新增商品规格
     *
     * @param productSpec 商品规格信息
     * @return 插入结果
     */
    boolean insertProductSpec(ProductSpec productSpec);

    /**
     * 更新商品规格信息
     *
     * @param productSpec 商品规格信息
     * @return 更新结果
     */
    boolean updateProductSpec(ProductSpec productSpec);

    /**
     * 根据ID删除商品规格
     *
     * @param id 商品规格ID
     * @return 删除结果
     */
    boolean deleteProductSpec(Long id);

    /**
     * 批量插入商品规格
     *
     * @param specList 商品规格列表
     * @return 成功插入的数量
     */
    int batchInsertProductSpecs(List<ProductSpec> specList);

    /**
     * 批量删除商品规格
     *
     * @param ids 规格ID列表
     * @return 成功删除的数量
     */
    int batchDeleteProductSpecs(List<Long> ids);

    /**
     * 根据规格值查询商品规格
     *
     * @param productId 商品ID
     * @param specValue 规格值
     * @return 商品规格列表
     */
    List<ProductSpec> selectBySpecValue(Long productId, String specValue);

    /**
     * 生成规格组合
     * 根据多个规格及其值，生成所有可能的规格组合
     *
     * @param productId 商品ID
     * @param specList  规格列表
     * @return 规格组合列表，每个组合为一个Map
     */
    List<Map<String, String>> generateSpecCombinations(Long productId, List<ProductSpec> specList);

    /**
     * 检查规格是否被SKU引用
     *
     * @param specId 规格ID
     * @return 如果被引用返回true，否则返回false
     */
    boolean isSpecUsedBySku(Long specId);
}
