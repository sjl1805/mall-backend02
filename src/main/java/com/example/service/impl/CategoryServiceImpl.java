package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.CategoryMapper;
import com.example.model.entity.Category;
import com.example.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品分类服务实现类
 * 
 * 该类实现了商品分类相关的业务逻辑，包括分类的创建、查询、更新和删除等功能。
 * 商品分类是电商系统中重要的基础结构，用于组织和展示商品，形成系统的商品目录树。
 * 良好的分类体系能够帮助用户快速找到所需商品，提升购物体验和转化率。
 * 系统支持多级分类结构（如一级分类、二级分类等），满足不同粒度的商品管理需求。
 * 使用了Spring缓存机制对分类信息进行缓存，提高查询效率，减轻数据库压力。
 * 使用MyBatis-Plus的ServiceImpl简化数据访问操作。
 *
 * @author 31815
 * @description 针对表【category(商品分类表)】的数据库操作Service实现
 * @createDate 2025-02-24 12:04:22
 */
@Service
@CacheConfig(cacheNames = "categories") // 指定该服务类的缓存名称为"categories"
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
        implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 根据名称查询商品分类
     * 
     * 该方法从缓存或数据库获取指定名称的商品分类，
     * 用于前台搜索特定分类或后台管理系统查询
     *
     * @param name 分类名称
     * @return 商品分类列表
     */
    @Override
    @Cacheable(key = "#name") // 缓存分类数据，提高查询效率
    public List<Category> selectByName(String name) {
        return categoryMapper.selectByName(name);
    }

    /**
     * 分页查询商品分类数据
     * 
     * 该方法用于后台管理系统分页查看分类数据，
     * 支持按名称、状态、创建时间等条件筛选，便于管理员全面了解分类情况
     *
     * @param page 分页参数
     * @return 商品分类分页数据
     */
    @Override
    public IPage<Category> selectPage(IPage<Category> page) {
        return categoryMapper.selectPage(page);
    }

    /**
     * 根据ID查询商品分类
     * 
     * 该方法从缓存或数据库获取指定ID的商品分类详情，
     * 用于查看分类详情或获取分类的父子级关系
     *
     * @param id 分类ID
     * @return 商品分类实体
     */
    @Override
    @Cacheable(key = "#id") // 缓存分类详情，提高查询效率
    public Category selectById(Long id) {
        return categoryMapper.selectById(id);
    }

    /**
     * 创建商品分类
     * 
     * 该方法用于后台管理系统创建新的商品分类，
     * 可以设置分类名称、图标、父级分类ID、排序等属性，
     * 用于构建和完善商品分类体系
     *
     * @param category 商品分类实体
     * @return 创建成功返回true，失败返回false
     */
    @Override
    @Transactional
    @CacheEvict(allEntries = true) // 清除所有分类缓存
    public boolean insertCategory(Category category) {
        return categoryMapper.insert(category) > 0;
    }

    /**
     * 更新商品分类
     * 
     * 该方法用于修改商品分类信息，如调整名称、图标、排序或父级分类等，
     * 用于优化分类结构或修正错误信息，
     * 并清除相关缓存，确保数据一致性
     *
     * @param category 商品分类实体
     * @return 更新成功返回true，失败返回false
     */
    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(key = "#category.id"),
        @CacheEvict(key = "'tree'")
    })
    public boolean updateCategory(Category category) {
        return categoryMapper.updateById(category) > 0;
    }

    /**
     * 删除商品分类
     * 
     * 该方法用于删除不再需要的商品分类，
     * 需要注意的是，删除分类前应确保该分类下没有关联的商品，
     * 且没有子级分类，以避免数据不一致问题，
     * 并清除相关缓存
     *
     * @param id 分类ID
     * @return 删除成功返回true，失败返回false
     */
    @Override
    @Transactional
    @CacheEvict(allEntries = true) // 清除所有分类缓存
    public boolean deleteCategory(Long id) {
        return categoryMapper.deleteById(id) > 0;
    }
    
    /**
     * 根据父ID查询子分类
     * 
     * 该方法用于查询某个分类下的所有直接子分类，
     * 用于构建分类导航或分类选择器
     *
     * @param parentId 父分类ID
     * @return 子分类列表
     */
    @Override
    @Cacheable(key = "'parent_'+#parentId")
    public List<Category> selectByParentId(Long parentId) {
        return categoryMapper.selectByParentId(parentId);
    }
    
    /**
     * 根据分类层级查询分类
     * 
     * 该方法用于查询特定层级的所有分类，
     * 如查询所有一级分类、二级分类等
     *
     * @param level 分类层级（1-一级 2-二级 3-三级）
     * @return 分类列表
     */
    @Override
    @Cacheable(key = "'level_'+#level")
    public List<Category> selectByLevel(Integer level) {
        return categoryMapper.selectByLevel(level);
    }
    
    /**
     * 获取分类树结构
     * 
     * 该方法返回完整的分类树结构，
     * 包括所有层级的分类及其子分类，
     * 通常用于前端导航菜单或后台分类管理
     *
     * @return 分类树结构
     */
    @Override
    @Cacheable(key = "'tree'")
    public List<Map<String, Object>> selectCategoryTree() {
        return categoryMapper.selectCategoryTree();
    }
    
    /**
     * 获取子分类及其商品数量
     * 
     * 该方法返回指定分类的子分类，
     * 并统计每个子分类下的商品数量，
     * 用于分类页面展示或数据分析
     *
     * @param parentId 父分类ID
     * @return 子分类及商品数量列表
     */
    @Override
    @Cacheable(key = "'count_'+#parentId")
    public List<Map<String, Object>> selectChildrenWithProductCount(Long parentId) {
        return categoryMapper.selectChildrenWithProductCount(parentId);
    }
    
    /**
     * 更新分类排序
     * 
     * 该方法用于调整分类在同级中的显示顺序，
     * 影响前端分类导航的展示顺序
     *
     * @param id 分类ID
     * @param sort 排序值
     * @return 更新结果
     */
    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public boolean updateSort(Long id, Integer sort) {
        return categoryMapper.updateSort(id, sort) > 0;
    }
    
    /**
     * 更新分类状态
     * 
     * 该方法用于启用或禁用分类，
     * 禁用的分类及其子分类在前端将不可见
     *
     * @param id 分类ID
     * @param status 状态值
     * @return 更新结果
     */
    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public boolean updateStatus(Long id, Integer status) {
        return categoryMapper.updateStatus(id, status) > 0;
    }
    
    /**
     * 获取分类路径（面包屑导航）
     * 
     * 该方法返回从顶级分类到指定分类的完整路径，
     * 用于构建面包屑导航，帮助用户了解当前分类在整体分类体系中的位置
     *
     * @param categoryId 分类ID
     * @return 分类路径
     */
    @Override
    @Cacheable(key = "'path_'+#categoryId")
    public List<Category> selectCategoryPath(Long categoryId) {
        return categoryMapper.selectCategoryPath(categoryId);
    }
    
    /**
     * 批量删除分类
     * 
     * 该方法用于一次性删除多个分类，
     * 适用于后台批量管理操作
     *
     * @param ids 分类ID列表
     * @return 删除结果
     */
    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public boolean batchDeleteCategories(List<Long> ids) {
        return categoryMapper.batchDeleteCategories(ids) > 0;
    }
    
    /**
     * 批量更新分类状态
     * 
     * 该方法用于一次性更新多个分类的状态，
     * 适用于后台批量启用或禁用分类
     *
     * @param ids 分类ID列表
     * @param status 状态值
     * @return 更新结果
     */
    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public boolean batchUpdateStatus(List<Long> ids, Integer status) {
        return categoryMapper.batchUpdateStatus(ids, status) > 0;
    }

    /**
     * 检查分类是否可以安全删除
     * 
     * 在删除分类前，需要检查该分类是否有子分类和关联的商品
     * 只有当分类没有子分类且没有关联商品时，才能安全删除
     *
     * @param categoryId 分类ID
     * @return 如果可以安全删除返回true，否则返回false
     */
    @Override
    public boolean checkCanDelete(Long categoryId) {
        if (categoryId == null) {
            return false;
        }
        
        // 检查是否有子分类
        Integer childCount = categoryMapper.countChildCategories(categoryId);
        if (childCount > 0) {
            return false;
        }
        
        // 检查是否有关联商品
        Integer productCount = categoryMapper.countProductsByCategoryId(categoryId);
        return productCount == 0;
    }
    
    /**
     * 批量导入分类数据
     * 
     * 该方法支持批量导入分类数据，通常用于系统初始化或数据迁移
     * 在导入前会进行数据验证，确保数据的完整性和正确性
     *
     * @param categories 分类数据列表
     * @return 导入成功的数量
     */
    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public int batchImport(List<Category> categories) {
        if (CollectionUtils.isEmpty(categories)) {
            return 0;
        }
        
        int successCount = 0;
        for (Category category : categories) {
            // 数据验证
            if (category == null || !StringUtils.hasText(category.getName()) 
                    || category.getLevel() == null) {
                continue;
            }
            
            // 设置默认值
            if (category.getParentId() == null) {
                category.setParentId(0L); // 默认为顶级分类
            }
            if (category.getSort() == null) {
                category.setSort(0); // 默认排序值
            }
            if (category.getStatus() == null) {
                category.setStatus(1); // 默认启用
            }
            
            // 保存分类
            if (categoryMapper.insert(category) > 0) {
                successCount++;
            }
        }
        
        return successCount;
    }
    
    /**
     * 导出分类数据
     * 
     * 该方法用于导出分类数据，支持按层级筛选
     * 导出的数据可用于备份、报表或数据分析
     *
     * @param level 分类层级（可选）
     * @return 分类数据列表
     */
    @Override
    @Cacheable(key = "'export_'+#level")
    public List<Category> exportCategories(Integer level) {
        if (level != null) {
            return categoryMapper.selectByLevel(level);
        } else {
            // 导出所有分类
            return categoryMapper.selectList(null);
        }
    }
    
    /**
     * 查询热门分类
     * 
     * 该方法返回商品数量最多的分类，通常用于首页推荐或导航
     * 结果包含分类基本信息和商品数量
     *
     * @param limit 查询数量限制
     * @return 热门分类列表
     */
    @Override
    @Cacheable(key = "'hot_'+#limit")
    public List<Map<String, Object>> getHotCategories(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10; // 默认查询10个
        }
        return categoryMapper.selectHotCategories(limit);
    }
    
    /**
     * 统计每个层级的分类数量
     * 
     * 该方法统计每个层级的分类数量，用于数据分析和监控
     * 返回的Map中，key为层级，value为该层级的分类数量
     *
     * @return 层级-数量映射
     */
    @Override
    @Cacheable(key = "'level_count'")
    public Map<Integer, Integer> countCategoriesByLevel() {
        List<Category> allCategories = categoryMapper.selectList(null);
        Map<Integer, Integer> result = new HashMap<>();
        
        for (Category category : allCategories) {
            Integer level = category.getLevel();
            result.put(level, result.getOrDefault(level, 0) + 1);
        }
        
        return result;
    }
    
    /**
     * 根据关键词搜索分类
     * 
     * 该方法根据关键词搜索匹配的分类，支持模糊匹配
     * 通常用于后台管理系统的分类搜索功能
     *
     * @param keyword 搜索关键词
     * @return 匹配的分类列表
     */
    @Override
    public List<Category> searchCategories(String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return new ArrayList<>();
        }
        
        return categoryMapper.searchCategories(keyword);
    }
    
    /**
     * 获取完整的分类树（包含所有层级和商品数量）
     * 
     * 该方法返回完整的分类树结构，包括所有层级的分类及其子分类
     * 同时统计每个分类下的商品数量，方便前端展示
     * 通常用于前台导航菜单或后台分类管理
     *
     * @return 完整分类树结构
     */
    @Override
    @Cacheable(key = "'full_tree'")
    public List<Category> getFullCategoryTree() {
        // 查询所有分类
        List<Category> allCategories = categoryMapper.selectList(null);
        
        // 根据层级和父ID组织分类树
        Map<Long, List<Category>> childrenMap = allCategories.stream()
                .filter(c -> c.getParentId() != null && c.getParentId() > 0)
                .collect(Collectors.groupingBy(Category::getParentId));
        
        // 设置子分类
        for (Category category : allCategories) {
            List<Category> children = childrenMap.get(category.getId());
            if (!CollectionUtils.isEmpty(children)) {
                category.setChildren(children);
            }
            
            // 设置商品数量
            Integer productCount = categoryMapper.countProductsByCategoryId(category.getId());
            category.setProductCount(productCount);
        }
        
        // 返回顶级分类
        return allCategories.stream()
                .filter(c -> c.getParentId() == null || c.getParentId() == 0)
                .collect(Collectors.toList());
    }
}




