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
import java.util.stream.Collectors;

/**
 * 商品规格服务实现类
 * 
 * @author 31815
 * @description 实现商品规格核心业务逻辑，包含：
 *              1. 批量操作的原子性保证
 *              2. 规格名称唯一性校验
 *              3. 缓存策略优化
 * @createDate 2025-02-18 23:44:05
 */
@Service
@CacheConfig(cacheNames = "productSpecService")
public class ProductSpecServiceImpl extends ServiceImpl<ProductSpecMapper, ProductSpec>
        implements ProductSpecService {

    //private static final Logger logger = LoggerFactory.getLogger(ProductSpecServiceImpl.class);

    /**
     * 批量创建规格（完整校验）
     * @param productId 商品ID
     * @param specs 规格列表
     * @return 操作结果
     * @implNote 业务逻辑：
     *           1. 设置商品ID并校验数据
     *           2. 校验规格名称唯一性
     *           3. 批量插入数据
     *           4. 清除商品缓存
     */
    @Override
    @Transactional
    @CacheEvict(key = "'product:' + #productId")
    public boolean batchCreateSpecs(Long productId, List<ProductSpecDTO> specs) {
        specs.forEach(spec -> {
            spec.setProductId(productId);
            validateSpec(spec);
        });
        return baseMapper.batchInsert(specs) > 0;
    }

    /**
     * 获取规格列表（缓存优化）
     * @param productId 商品ID
     * @return 规格列表
     * @implNote 缓存策略：
     *           1. 缓存键：product:{productId}
     *           2. 缓存时间：30分钟
     */
    @Override
    @Cacheable(key = "'product:' + #productId")
    public List<ProductSpecDTO> getSpecsByProductId(Long productId) {
        return baseMapper.selectByProductId(productId).stream()
                .map(ProductSpecDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 更新规格值（安全操作）
     * @param productId 商品ID
     * @param specId 规格ID
     * @param specValues 新规格值
     * @return 操作结果
     * @implNote 使用数据库行锁保证原子性
     */
    @Override
    @Transactional
    @CacheEvict(key = "'product:' + #productId")
    public boolean updateSpecValues(Long productId, Long specId, String specValues) {
        return baseMapper.updateSpecValuesSafely(productId, specId, specValues) > 0;
    }

    /**
     * 获取规格数量（缓存优化）
     * @param productId 商品ID
     * @return 规格总数
     * @implNote 缓存策略：
     *           1. 缓存键：count:{productId}
     *           2. 缓存时间：1小时
     */
    @Override
    @Cacheable(key = "'count:' + #productId")
    public Integer getSpecCount(Long productId) {
        return baseMapper.countByProductId(productId);
    }

    /**
     * 校验规格有效性
     * @param spec 规格信息
     * @throws BusinessException 当规格名称重复时抛出
     */
    private void validateSpec(ProductSpecDTO spec) {
        if (checkSpecNameExists(spec.getProductId(), spec.getSpecName(), spec.getId())) {
            throw new BusinessException(ResultCode.SPEC_NAME_EXISTS);
        }
    }

    /**
     * 检查规格名称唯一性
     * @param productId 商品ID
     * @param specName 规格名称
     * @param excludeId 排除的规格ID（用于更新操作）
     * @return 是否存在重复名称
     */
    private boolean checkSpecNameExists(Long productId, String specName, Long excludeId) {
        return lambdaQuery()
                .eq(ProductSpec::getProductId, productId)
                .eq(ProductSpec::getSpecName, specName)
                .ne(excludeId != null, ProductSpec::getId, excludeId)
                .exists();
    }

}






