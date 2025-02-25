package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.exception.BusinessException;
import com.example.mapper.ProductTagMapper;
import com.example.model.entity.ProductTag;
import com.example.service.ProductTagService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品标签服务实现类
 */
@Service
public class ProductTagServiceImpl extends ServiceImpl<ProductTagMapper, ProductTag> implements ProductTagService {

    @Override
    public List<ProductTag> getTagsByType(Integer type) {
        return baseMapper.findByType(type);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductTag addTag(ProductTag tag) {
        // 设置默认值
        tag.setCreateTime(LocalDateTime.now());
        tag.setUpdateTime(LocalDateTime.now());
        
        // 保存标签
        save(tag);
        
        return tag;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateTag(ProductTag tag) {
        if (tag == null || tag.getId() == null) {
            throw new BusinessException("标签ID不能为空");
        }
        
        // 不允许修改的字段设为null
        tag.setCreateTime(null);
        
        tag.setUpdateTime(LocalDateTime.now());
        
        return updateById(tag);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTag(Long tagId) {
        // 后续可以添加标签使用检查逻辑，防止删除已使用的标签
        return removeById(tagId);
    }
} 