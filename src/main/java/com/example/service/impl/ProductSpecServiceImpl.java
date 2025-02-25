package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.ProductSpecMapper;
import com.example.model.entity.ProductSpec;
import com.example.service.ProductSpecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商品规格服务实现类
 * 
 * 该类实现了商品规格相关的业务逻辑，包括规格的添加、修改、删除和查询等功能。
 * 商品规格用于定义商品的各种属性和选项（如颜色、尺寸、材质等），是实现商品SKU的基础。
 * 使用了Spring缓存机制对商品规格信息进行缓存，提高查询效率。
 * 使用MyBatis-Plus的ServiceImpl简化数据访问操作。
 *
 * @author 31815
 * @description 针对表【product_spec(商品规格表)】的数据库操作Service实现
 * @createDate 2025-02-24 12:03:58
 */
@Service
@CacheConfig(cacheNames = "productSpecs") // 指定该服务类的缓存名称为"productSpecs"
public class ProductSpecServiceImpl extends ServiceImpl<ProductSpecMapper, ProductSpec>
        implements ProductSpecService {

    @Autowired
    private ProductSpecMapper productSpecMapper;

    /**
     * 根据商品ID查询商品规格列表
     * 
     * 该方法从缓存或数据库获取指定商品的所有规格信息，
     * 用于商品详情页展示规格选项和SKU生成
     *
     * @param productId 商品ID
     * @return 商品规格列表
     */
    @Override
    @Cacheable(value = "productSpecs", key = "#productId") // 缓存商品规格信息，提高查询效率
    public List<ProductSpec> selectByProductId(Long productId) {
        return productSpecMapper.selectByProductId(productId);
    }

    /**
     * 分页查询商品规格数据
     * 
     * 该方法用于后台管理系统分页查看商品规格数据
     *
     * @param page 分页参数
     * @return 商品规格分页数据
     */
    @Override
    public IPage<ProductSpec> selectPage(IPage<ProductSpec> page) {
        return productSpecMapper.selectPage(page);
    }

    /**
     * 根据ID查询商品规格
     *
     * @param id 规格ID
     * @return 商品规格实体
     */
    @Override
    @Cacheable(value = "productSpecs", key = "#id") // 缓存规格详情，提高查询效率
    public ProductSpec selectById(Long id) {
        return productSpecMapper.selectById(id);
    }

    /**
     * 添加商品规格
     * 
     * 该方法用于后台管理系统添加新的商品规格，
     * 通常在商品创建或编辑过程中使用，
     * 并清除相关缓存，确保数据一致性
     *
     * @param productSpec 商品规格实体
     * @return 添加成功返回true，失败返回false
     */
    @Override
    @Transactional
    @CacheEvict(value = "productSpecs", key = "#productSpec.id") // 清除规格缓存
    public boolean insertProductSpec(ProductSpec productSpec) {
        return productSpecMapper.insert(productSpec) > 0;
    }

    /**
     * 更新商品规格
     * 
     * 该方法用于后台管理系统更新商品规格信息，
     * 修改规格名称、规格值等，
     * 并清除相关缓存，确保数据一致性
     *
     * @param productSpec 商品规格实体
     * @return 更新成功返回true，失败返回false
     */
    @Override
    @Transactional
    @CacheEvict(value = "productSpecs", key = "#productSpec.id") // 清除规格缓存
    public boolean updateProductSpec(ProductSpec productSpec) {
        return productSpecMapper.updateById(productSpec) > 0;
    }

    /**
     * 删除商品规格
     * 
     * 该方法用于后台管理系统删除商品规格，
     * 需要注意的是，删除规格可能会影响相关的SKU数据，
     * 所以应当谨慎操作，并清除相关缓存
     *
     * @param id 规格ID
     * @return 删除成功返回true，失败返回false
     */
    @Override
    @Transactional
    @CacheEvict(value = "productSpecs", key = "#id") // 清除被删除规格的缓存
    public boolean deleteProductSpec(Long id) {
        return productSpecMapper.deleteById(id) > 0;
    }
}




