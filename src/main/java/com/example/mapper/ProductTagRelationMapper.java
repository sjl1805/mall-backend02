package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.model.entity.ProductTagRelation;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 商品-标签关联Mapper接口
 */
@Mapper
public interface ProductTagRelationMapper extends BaseMapper<ProductTagRelation> {
    
    /**
     * 根据商品ID查询所有标签关联
     * @param productId 商品ID
     * @return 标签关联列表
     */
    List<ProductTagRelation> selectByProductId(@Param("productId") Long productId);
    
    /**
     * 根据标签ID查询所有商品关联
     * @param tagId 标签ID
     * @return 商品关联列表
     */
    List<ProductTagRelation> selectByTagId(@Param("tagId") Long tagId);
    
    /**
     * 查询商品的高权重标签(权重大于指定值)
     * @param productId 商品ID
     * @param minWeight 最小权重值
     * @return 高权重标签关联列表
     */
    List<ProductTagRelation> selectHighWeightTags(@Param("productId") Long productId, @Param("minWeight") BigDecimal minWeight);
    
    /**
     * 查询标签的高权重商品(权重大于指定值)
     * @param tagId 标签ID
     * @param minWeight 最小权重值
     * @return 高权重商品关联列表
     */
    List<ProductTagRelation> selectHighWeightProducts(@Param("tagId") Long tagId, @Param("minWeight") BigDecimal minWeight);
    
    /**
     * 更新商品标签权重
     * @param productId 商品ID
     * @param tagId 标签ID
     * @param weight 新权重
     * @return 影响行数
     */
    int updateWeight(@Param("productId") Long productId, @Param("tagId") Long tagId, @Param("weight") BigDecimal weight);
    
    /**
     * 批量插入商品标签关联
     * @param list 商品标签关联列表
     * @return 影响行数
     */
    int batchInsert(@Param("list") List<ProductTagRelation> list);
    
    /**
     * 删除商品的所有标签关联
     * @param productId 商品ID
     * @return 影响行数
     */
    int deleteByProductId(@Param("productId") Long productId);
    
    /**
     * 删除特定的商品标签关联
     * @param productId 商品ID
     * @param tagId 标签ID
     * @return 影响行数
     */
    int deleteByProductIdAndTagId(@Param("productId") Long productId, @Param("tagId") Long tagId);
    
    /**
     * 根据标签ID查询热门商品
     * @param tagId 标签ID
     * @param limit 返回数量限制
     * @return 热门商品关联列表
     */
    List<ProductTagRelation> selectHotProductsByTagId(@Param("tagId") Long tagId, @Param("limit") Integer limit);
    
    /**
     * 查询标签的使用统计
     * @return 标签使用统计列表，包含tagId、productCount（商品数量）、avgWeight（平均权重）
     */
    @MapKey("tagId")
    Map<Long, Map<String, Object>> selectTagUsageStats();
    
    /**
     * 分页查询某标签的关联商品
     * @param page 分页参数
     * @param tagId 标签ID
     * @return 分页数据
     */
    IPage<ProductTagRelation> selectProductsByTagIdPage(Page<ProductTagRelation> page, @Param("tagId") Long tagId);
    
    /**
     * 查询商品的标签数量
     * @param productId 商品ID
     * @return 标签数量
     */
    int countByProductId(@Param("productId") Long productId);
    
    /**
     * 查询标签关联的商品数量
     * @param tagId 标签ID
     * @return 商品数量
     */
    int countByTagId(@Param("tagId") Long tagId);
    
    /**
     * 增加标签权重
     * @param productId 商品ID
     * @param tagId 标签ID
     * @param increment 增量值
     * @return 影响行数
     */
    int incrementWeight(@Param("productId") Long productId, @Param("tagId") Long tagId, @Param("increment") BigDecimal increment);
} 