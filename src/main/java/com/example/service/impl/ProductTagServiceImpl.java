package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.ResultCode;
import com.example.exception.BusinessException;
import com.example.mapper.ProductTagMapper;
import com.example.model.entity.ProductTag;
import com.example.service.ProductTagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 商品标签服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductTagServiceImpl extends ServiceImpl<ProductTagMapper, ProductTag> implements ProductTagService {

    private final ProductTagMapper productTagMapper;

    @Override
    public ProductTag getByName(String name) {
        if (!StringUtils.hasText(name)) {
            return null;
        }
        return productTagMapper.selectByName(name);
    }

    @Override
    public List<ProductTag> getListByType(Integer type) {
        if (type == null || type < 1 || type > 4) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "标签类型无效");
        }
        return productTagMapper.selectListByType(type);
    }

    @Override
    public IPage<ProductTag> getTagPage(Page<ProductTag> page, Map<String, Object> params) {
        return productTagMapper.selectTagPage(page, params);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductTag createTag(ProductTag productTag) {
        // 验证标签信息
        validateTag(productTag);

        // 检查标签名称是否存在
        if (checkNameExists(productTag.getName())) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "标签名称已存在");
        }

        // 设置创建和更新时间
        LocalDateTime now = LocalDateTime.now();
        productTag.setCreateTime(now);
        productTag.setUpdateTime(now);

        // 保存标签
        boolean success = save(productTag);
        if (!success) {
            throw new BusinessException(ResultCode.FAILED, "创建标签失败");
        }

        return productTag;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateTag(ProductTag productTag) {
        // 验证标签ID
        if (productTag.getId() == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "标签ID不能为空");
        }

        // 检查标签是否存在
        ProductTag existingTag = getById(productTag.getId());
        if (existingTag == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "标签不存在");
        }

        // 如果标签名称已更改，检查新名称是否存在
        if (StringUtils.hasText(productTag.getName()) 
                && !productTag.getName().equals(existingTag.getName()) 
                && checkNameExists(productTag.getName())) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "标签名称已存在");
        }

        // 设置更新时间
        productTag.setUpdateTime(LocalDateTime.now());

        return updateById(productTag);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTag(Long id) {
        if (id == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "标签ID不能为空");
        }

        // 检查标签是否存在
        ProductTag tag = getById(id);
        if (tag == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "标签不存在");
        }

        // 删除标签
        return removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchCreateTags(List<ProductTag> tagList) {
        if (tagList == null || tagList.isEmpty()) {
            return false;
        }

        // 设置创建和更新时间
        LocalDateTime now = LocalDateTime.now();
        for (ProductTag tag : tagList) {
            validateTag(tag);
            tag.setCreateTime(now);
            tag.setUpdateTime(now);
        }

        // 批量插入标签
        int rows = productTagMapper.batchInsert(tagList);
        return rows > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteTags(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }

        int rows = productTagMapper.batchDeleteByIds(ids);
        return rows > 0;
    }

    @Override
    public List<ProductTag> searchByKeyword(String keyword, Integer limit) {
        if (!StringUtils.hasText(keyword)) {
            return List.of();
        }
        
        if (limit == null || limit <= 0) {
            limit = 10; // 默认返回10条
        }
        
        return productTagMapper.searchByKeyword(keyword, limit);
    }

    @Override
    public List<ProductTag> getHotTags(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 20; // 默认返回20个热门标签
        }
        
        return productTagMapper.selectHotTags(limit);
    }

    @Override
    public List<Map<String, Object>> countTagByType() {
        return productTagMapper.countTagByType();
    }

    @Override
    public boolean checkNameExists(String name) {
        if (!StringUtils.hasText(name)) {
            return false;
        }
        
        int count = productTagMapper.existsByName(name);
        return count > 0;
    }

    @Override
    public List<ProductTag> getTagsByProductId(Long productId) {
        if (productId == null) {
            return List.of();
        }
        
        return productTagMapper.selectTagsByProductId(productId);
    }

    /**
     * 验证标签信息
     *
     * @param productTag 标签信息
     */
    private void validateTag(ProductTag productTag) {
        if (productTag == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "标签信息不能为空");
        }

        if (!StringUtils.hasText(productTag.getName())) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "标签名称不能为空");
        }

        if (productTag.getType() == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "标签类型不能为空");
        }

        if (productTag.getType() < 1 || productTag.getType() > 4) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "标签类型无效");
        }
    }
} 