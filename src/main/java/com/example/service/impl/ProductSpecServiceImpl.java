package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.ResultCode;
import com.example.exception.BusinessException;
import com.example.mapper.ProductSpecMapper;
import com.example.model.dto.product.ProductSpecDTO;
import com.example.model.entity.ProductSpec;
import com.example.service.ProductSpecService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 31815
 * @description 针对表【product_spec(商品规格表)】的数据库操作Service实现
 * @createDate 2025-02-18 23:44:05
 */
@Service
@CacheConfig(cacheNames = "productSpecService")
public class ProductSpecServiceImpl extends ServiceImpl<ProductSpecMapper, ProductSpec>
        implements ProductSpecService {

    //private static final Logger logger = LoggerFactory.getLogger(ProductSpecServiceImpl.class);

    @Override
    @Transactional
    @CacheEvict(key = "'product:' + #productId")
    public boolean batchCreateSpecs(Long productId, List<ProductSpecDTO> specs) {
        // 设置商品ID并校验数据
        specs.forEach(spec -> {
            spec.setProductId(productId);
            validateSpec(spec);
        });

        return baseMapper.batchInsert(specs) > 0;
    }

    @Override
    @Cacheable(key = "'product:' + #productId")
    public List<ProductSpec> getSpecsByProductId(Long productId) {
        return baseMapper.selectByProductId(productId);
    }

    @Override
    @Transactional
    @CacheEvict(key = "'product:' + #productId")
    public boolean updateSpecValues(Long productId, Long specId, String specValues) {
        return baseMapper.updateSpecValuesSafely(productId, specId, specValues) > 0;
    }

    @Override
    @Cacheable(key = "'count:' + #productId")
    public Integer getSpecCount(Long productId) {
        return baseMapper.countByProductId(productId);
    }

    private void validateSpec(ProductSpecDTO spec) {
        // 校验规格名称唯一性
        if (checkSpecNameExists(spec.getProductId(), spec.getSpecName(), spec.getId())) {
            throw new BusinessException(ResultCode.SPEC_NAME_EXISTS);
        }
    }

    private boolean checkSpecNameExists(Long productId, String specName, Long excludeId) {
        return lambdaQuery()
                .eq(ProductSpec::getProductId, productId)
                .eq(ProductSpec::getSpecName, specName)
                .ne(excludeId != null, ProductSpec::getId, excludeId)
                .exists();
    }
}




