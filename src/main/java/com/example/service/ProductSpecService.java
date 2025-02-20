package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.ProductSpec;
import com.example.model.dto.product.ProductSpecDTO;

import java.util.List;

/**
 * 商品规格服务接口
 * 
 * @author 31815
 * @description 提供商品规格管理功能，包含：
 *              1. 规格的批量操作
 *              2. 规格值更新
 *              3. 规格统计与查询
 * @createDate 2025-02-18 23:44:05
 */
public interface ProductSpecService extends IService<ProductSpec> {

    /**
     * 批量创建规格（事务操作）
     * @param productId 商品ID
     * @param specs 规格列表（至少一项）
     * @return 操作是否成功
     * @throws com.example.exception.BusinessException 当规格名称重复时抛出
     */
    boolean batchCreateSpecs(Long productId, List<ProductSpecDTO> specs);

    /**
     * 获取商品规格列表（带缓存）
     * @param productId 商品ID
     * @return 规格列表（按创建时间排序）
     * @implNote 结果缓存优化，有效期30分钟
     */
    List<ProductSpecDTO> getSpecsByProductId(Long productId);

    /**
     * 更新规格值（原子操作）
     * @param productId 商品ID
     * @param specId 规格ID
     * @param specValues 新规格值（JSON格式）
     * @return 操作是否成功
     * @throws com.example.exception.BusinessException 当规格不存在时抛出
     */
    boolean updateSpecValues(Long productId, Long specId, String specValues);

    /**
     * 获取规格数量统计
     * @param productId 商品ID
     * @return 规格总数
     * @implNote 独立缓存计数器，避免全量查询
     */
    Integer getSpecCount(Long productId);
}
