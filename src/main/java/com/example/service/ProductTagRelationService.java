package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.ProductTagRelation;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 商品-标签关联服务接口
 */
public interface ProductTagRelationService extends IService<ProductTagRelation> {
    
    /**
     * 根据商品ID查询所有标签关联
     * @param productId 商品ID
     * @return 标签关联列表
     */
    List<ProductTagRelation> getByProductId(Long productId);
    
    /**
     * 根据标签ID查询所有商品关联
     * @param tagId 标签ID
     * @return 商品关联列表
     */
    List<ProductTagRelation> getByTagId(Long tagId);
    
    /**
     * 查询商品的高权重标签
     * @param productId 商品ID
     * @param minWeight 最小权重值
     * @return 高权重标签关联列表
     */
    List<ProductTagRelation> getHighWeightTags(Long productId, BigDecimal minWeight);
    
    /**
     * 查询标签的高权重商品
     * @param tagId 标签ID
     * @param minWeight 最小权重值
     * @return 高权重商品关联列表
     */
    List<ProductTagRelation> getHighWeightProducts(Long tagId, BigDecimal minWeight);
    
    /**
     * 更新商品标签权重
     * @param productId 商品ID
     * @param tagId 标签ID
     * @param weight 新权重
     * @return 是否更新成功
     */
    boolean updateWeight(Long productId, Long tagId, BigDecimal weight);
    
    /**
     * 批量保存商品标签关联
     * @param relations 关联列表
     * @return 是否保存成功
     */
    boolean batchSaveRelations(List<ProductTagRelation> relations);
    
    /**
     * 删除商品的所有标签关联
     * @param productId 商品ID
     * @return 是否删除成功
     */
    boolean removeByProductId(Long productId);
    
    /**
     * 删除特定的商品标签关联
     * @param productId 商品ID
     * @param tagId 标签ID
     * @return 是否删除成功
     */
    boolean removeRelation(Long productId, Long tagId);
    
    /**
     * 根据标签ID查询热门商品
     * @param tagId 标签ID
     * @param limit 限制数量
     * @return 热门商品关联列表
     */
    List<ProductTagRelation> getHotProductsByTagId(Long tagId, Integer limit);
    
    /**
     * 查询标签的使用统计
     * @return 标签统计信息
     */
    List<Map<String, Object>> getTagUsageStats();
    
    /**
     * 分页查询某标签的关联商品
     * @param tagId 标签ID
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<ProductTagRelation> getProductsByTagIdPage(Long tagId, Page<ProductTagRelation> page);
    
    /**
     * 查询商品的标签数量
     * @param productId 商品ID
     * @return 标签数量
     */
    int countByProductId(Long productId);
    
    /**
     * 查询标签关联的商品数量
     * @param tagId 标签ID
     * @return 商品数量
     */
    int countByTagId(Long tagId);
    
    /**
     * 增加标签权重
     * @param productId 商品ID
     * @param tagId 标签ID
     * @param increment 增加的权重值
     * @return 是否增加成功
     */
    boolean incrementWeight(Long productId, Long tagId, BigDecimal increment);
    
    /**
     * 为商品添加标签
     * @param productId 商品ID
     * @param tagId 标签ID
     * @param weight 权重值
     * @return 是否添加成功
     */
    boolean addTagToProduct(Long productId, Long tagId, BigDecimal weight);
} 