package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.ProductSpec;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * @author 31815
 * @description 针对表【product_spec(商品规格表)】的数据库操作Service
 * @createDate 2025-02-24 12:03:58
 */
public interface ProductSpecService extends IService<ProductSpec> {

    /**
     * 根据商品ID查询商品规格
     * @param productId 商品ID
     * @return 商品规格列表
     */
    List<ProductSpec> selectByProductId(Long productId);

    /**
     * 分页查询商品规格
     * @param page 分页信息
     * @return 商品规格列表
     */
    IPage<ProductSpec> selectPage(IPage<ProductSpec> page);

    /**
     * 根据ID查询商品规格
     * @param id 商品规格ID
     * @return 商品规格信息
     */
    ProductSpec selectById(Long id);

    /**
     * 新增商品规格
     * @param productSpec 商品规格信息
     * @return 插入结果
     */
    boolean insertProductSpec(ProductSpec productSpec);

    /**
     * 更新商品规格信息
     * @param productSpec 商品规格信息
     * @return 更新结果
     */
    boolean updateProductSpec(ProductSpec productSpec);

    /**
     * 根据ID删除商品规格
     * @param id 商品规格ID
     * @return 删除结果
     */
    boolean deleteProductSpec(Long id);
}
