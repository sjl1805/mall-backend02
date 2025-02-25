package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.ProductTagRelationMapper;
import com.example.model.entity.ProductTagRelation;
import com.example.service.ProductTagRelationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 商品-标签关联服务实现类
 */
@Service
public class ProductTagRelationServiceImpl extends ServiceImpl<ProductTagRelationMapper, ProductTagRelation> implements ProductTagRelationService {

    @Override
    public List<ProductTagRelation> getByProductId(Long productId) {
        return baseMapper.selectByProductId(productId);
    }

    @Override
    public List<ProductTagRelation> getByTagId(Long tagId) {
        return baseMapper.selectByTagId(tagId);
    }

    @Override
    public List<ProductTagRelation> getHighWeightTags(Long productId, BigDecimal minWeight) {
        return baseMapper.selectHighWeightTags(productId, minWeight);
    }

    @Override
    public List<ProductTagRelation> getHighWeightProducts(Long tagId, BigDecimal minWeight) {
        return baseMapper.selectHighWeightProducts(tagId, minWeight);
    }

    @Override
    public boolean updateWeight(Long productId, Long tagId, BigDecimal weight) {
        return baseMapper.updateWeight(productId, tagId, weight) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchSaveRelations(List<ProductTagRelation> relations) {
        if (relations == null || relations.isEmpty()) {
            return false;
        }
        return baseMapper.batchInsert(relations) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByProductId(Long productId) {
        return baseMapper.deleteByProductId(productId) >= 0;
    }

    @Override
    public boolean removeRelation(Long productId, Long tagId) {
        return baseMapper.deleteByProductIdAndTagId(productId, tagId) > 0;
    }

    @Override
    public List<ProductTagRelation> getHotProductsByTagId(Long tagId, Integer limit) {
        return baseMapper.selectHotProductsByTagId(tagId, limit);
    }

    @Override
    public Map<Long, Map<String, Object>> getTagUsageStats() {
        return baseMapper.selectTagUsageStats();
    }

    @Override
    public IPage<ProductTagRelation> getProductsByTagIdPage(Long tagId, Page<ProductTagRelation> page) {
        return baseMapper.selectProductsByTagIdPage(page, tagId);
    }

    @Override
    public int countByProductId(Long productId) {
        return baseMapper.countByProductId(productId);
    }

    @Override
    public int countByTagId(Long tagId) {
        return baseMapper.countByTagId(tagId);
    }

    @Override
    public boolean incrementWeight(Long productId, Long tagId, BigDecimal increment) {
        return baseMapper.incrementWeight(productId, tagId, increment) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addTagToProduct(Long productId, Long tagId, BigDecimal weight) {
        // 判断关联是否已存在
        LambdaQueryWrapper<ProductTagRelation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProductTagRelation::getProductId, productId)
                    .eq(ProductTagRelation::getTagId, tagId);
        
        ProductTagRelation relation = baseMapper.selectOne(queryWrapper);
        
        if (relation != null) {
            // 关联已存在，更新权重
            return updateWeight(productId, tagId, weight);
        } else {
            // 新建关联
            ProductTagRelation newRelation = ProductTagRelation.builder()
                    .productId(productId)
                    .tagId(tagId)
                    .weight(weight)
                    .build();
            
            List<ProductTagRelation> relations = new ArrayList<>();
            relations.add(newRelation);
            return batchSaveRelations(relations);
        }
    }
} 