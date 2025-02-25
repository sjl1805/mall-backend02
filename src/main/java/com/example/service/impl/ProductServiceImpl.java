package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.ResultCode;
import com.example.constants.ProductConstants;
import com.example.exception.BusinessException;
import com.example.mapper.ProductMapper;
import com.example.model.entity.Product;
import com.example.model.entity.User;
import com.example.service.CategoryService;
import com.example.service.ProductService;
import com.example.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商品服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    private final ProductMapper productMapper;
    private final CategoryService categoryService;
    private final UserService userService;
    private final ObjectMapper objectMapper;

    @Override
    public IPage<Product> getProductPage(Page<Product> page, Map<String, Object> params) {
        return productMapper.selectProductPage(page, params);
    }

    @Override
    public Product getProductDetail(Long id) {
        if (id == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "商品ID不能为空");
        }
        return productMapper.selectProductDetail(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Product createProduct(Product product) {
        // 验证商品信息
        validateProduct(product);

        // 检查分类是否存在
        if (product.getCategoryId() != null && categoryService.getById(product.getCategoryId()) == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "商品分类不存在");
        }

        // 设置创建和更新时间
        LocalDateTime now = LocalDateTime.now();
        product.setCreateTime(now);
        product.setUpdateTime(now);

        // 如果状态为空，默认为下架状态
        if (product.getStatus() == null) {
            product.setStatus(ProductConstants.STATUS_OFF_SHELF);
        }

        // 保存商品
        boolean success = save(product);
        if (!success) {
            throw new BusinessException(ResultCode.FAILED, "创建商品失败");
        }

        return product;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateProduct(Product product) {
        // 验证商品ID
        if (product.getId() == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "商品ID不能为空");
        }

        // 检查商品是否存在
        Product existingProduct = getById(product.getId());
        if (existingProduct == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "商品不存在");
        }

        // 检查分类是否存在
        if (product.getCategoryId() != null && categoryService.getById(product.getCategoryId()) == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "商品分类不存在");
        }

        // 设置更新时间
        product.setUpdateTime(LocalDateTime.now());

        return updateById(product);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteProduct(Long id) {
        if (id == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "商品ID不能为空");
        }

        // 检查商品是否存在
        Product product = getById(id);
        if (product == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "商品不存在");
        }

        // 删除商品
        return removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteProducts(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "商品ID列表不能为空");
        }

        return removeByIds(ids);
    }

    @Override
    public List<Product> getProductsByCategoryId(Long categoryId) {
        if (categoryId == null) {
            return new ArrayList<>();
        }
        return productMapper.selectByCategoryId(categoryId);
    }

    @Override
    public List<Product> getProductsByTagId(Long tagId) {
        if (tagId == null) {
            return new ArrayList<>();
        }
        return productMapper.selectByTagId(tagId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStock(Long productId, Integer count) {
        if (productId == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "商品ID不能为空");
        }
        if (count == 0) {
            return true; // 无需更新
        }

        // 获取商品
        Product product = getById(productId);
        if (product == null) {
            throw new BusinessException(ResultCode.PRODUCT_NOT_FOUND);
        }

        // 减库存时，检查库存是否足够
        if (count < 0 && product.getStock() < Math.abs(count)) {
            throw new BusinessException(ResultCode.PRODUCT_STOCK_ERROR);
        }

        int rows = productMapper.updateStock(productId, count);
        return rows > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateStatus(List<Long> ids, Integer status) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "商品ID列表不能为空");
        }
        if (status == null || (status != ProductConstants.STATUS_ON_SHELF && status != ProductConstants.STATUS_OFF_SHELF)) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "商品状态无效");
        }

        int rows = productMapper.batchUpdateStatus(ids, status);
        return rows > 0;
    }

    @Override
    public List<Product> getHotProducts(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = ProductConstants.DEFAULT_HOT_PRODUCTS_LIMIT; // 默认10个
        }
        return productMapper.selectHotProducts(limit);
    }

    @Override
    public List<Product> getNewProducts(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = ProductConstants.DEFAULT_NEW_PRODUCTS_LIMIT; // 默认10个
        }
        return productMapper.selectNewProducts(limit);
    }

    @Override
    public List<Product> getRecommendProducts(Long categoryId, Long productId, Integer limit) {
        if (categoryId == null || productId == null) {
            return new ArrayList<>();
        }
        if (limit == null || limit <= 0) {
            limit = ProductConstants.DEFAULT_RECOMMEND_PRODUCTS_LIMIT; // 默认6个
        }
        return productMapper.selectRecommendProducts(categoryId, productId, limit);
    }

    @Override
    public List<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        if (minPrice == null) {
            minPrice = BigDecimal.ZERO;
        }
        if (maxPrice == null) {
            maxPrice = new BigDecimal("999999"); // 默认很大的价格
        }
        if (minPrice.compareTo(maxPrice) > 0) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "最低价格不能大于最高价格");
        }
        return productMapper.selectByPriceRange(minPrice, maxPrice);
    }

    @Override
    public List<Map<String, Object>> countProductByCategory() {
        return productMapper.countProductByCategory();
    }

    @Override
    public List<Map<String, Object>> countProductByStatus() {
        return productMapper.countProductByStatus();
    }

    @Override
    public List<Product> getLowStockProducts(Integer threshold) {
        if (threshold == null || threshold <= 0) {
            threshold = ProductConstants.DEFAULT_STOCK_WARNING_THRESHOLD; // 默认10个
        }
        return productMapper.selectLowStockProducts(threshold);
    }

    @Override
    public List<Product> searchProductsByKeyword(String keyword, Integer limit) {
        if (!StringUtils.hasText(keyword)) {
            return new ArrayList<>();
        }
        if (limit == null || limit <= 0) {
            limit = ProductConstants.DEFAULT_SEARCH_LIMIT; // 默认20个
        }
        return productMapper.searchByKeyword(keyword, limit);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateProductTags(Long productId, List<Map<String, Object>> tags) {
        if (productId == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "商品ID不能为空");
        }

        // 检查商品是否存在
        Product product = getById(productId);
        if (product == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "商品不存在");
        }

        try {
            String tagsJson = objectMapper.writeValueAsString(tags);
            int rows = productMapper.updateProductTags(productId, tagsJson);
            return rows > 0;
        } catch (JsonProcessingException e) {
            log.error("更新商品标签失败", e);
            throw new BusinessException(ResultCode.DATA_ERROR, "标签数据格式错误");
        }
    }

    @Override
    public List<Product> getProductsByCategoryIds(List<Long> categoryIds) {
        if (CollectionUtils.isEmpty(categoryIds)) {
            return new ArrayList<>();
        }
        return productMapper.selectByCategoryIds(categoryIds);
    }

    @Override
    public boolean checkStockSufficient(Long productId, Integer quantity) {
        if (productId == null || quantity == null || quantity <= 0) {
            return false;
        }
        
        Product product = getById(productId);
        return product != null && product.getStock() >= quantity;
    }

    @Override
    public List<Product> getPersonalizedRecommendProducts(Long userId, Integer limit) {
        if (userId == null) {
            return getHotProducts(limit); // 未登录用户返回热门商品
        }
        
        if (limit == null || limit <= 0) {
            limit = ProductConstants.DEFAULT_PERSONALIZED_RECOMMEND_LIMIT; // 默认10个
        }
        
        // 获取用户信息
        User user = userService.getById(userId);
        if (user == null) {
            return getHotProducts(limit);
        }
        
        // 获取用户偏好分类
        List<Long> preferredCategories = user.getPreferredCategories();
        if (CollectionUtils.isEmpty(preferredCategories)) {
            return getHotProducts(limit);
        }
        
        // 根据用户偏好分类获取商品
        List<Product> recommendProducts = getProductsByCategoryIds(preferredCategories);
        
        // 如果商品不足，补充热门商品
        if (recommendProducts.size() < limit) {
            List<Product> hotProducts = getHotProducts(limit - recommendProducts.size());
            // 过滤掉已经存在的商品
            List<Long> existingIds = recommendProducts.stream()
                    .map(Product::getId)
                    .collect(Collectors.toList());
            
            hotProducts = hotProducts.stream()
                    .filter(p -> !existingIds.contains(p.getId()))
                    .collect(Collectors.toList());
                    
            recommendProducts.addAll(hotProducts);
        }
        
        // 如果商品超过限制，截取前limit个
        if (recommendProducts.size() > limit) {
            recommendProducts = recommendProducts.subList(0, limit);
        }
        
        return recommendProducts;
    }

    /**
     * 验证商品信息
     *
     * @param product 商品信息
     */
    private void validateProduct(Product product) {
        if (product == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "商品信息不能为空");
        }

        if (!StringUtils.hasText(product.getName())) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "商品名称不能为空");
        }

        if (product.getCategoryId() == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "商品分类不能为空");
        }

        if (product.getPrice() == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "商品价格不能为空");
        }

        if (product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "商品价格必须大于0");
        }

        if (product.getStock() == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "商品库存不能为空");
        }

        if (product.getStock() < 0) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "商品库存不能小于0");
        }

        if (!StringUtils.hasText(product.getImageMain())) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "商品主图不能为空");
        }
    }
} 