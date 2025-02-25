package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.ResultCode;
import com.example.constants.CategoryConstants;
import com.example.exception.BusinessException;
import com.example.mapper.CategoryMapper;
import com.example.model.entity.Category;
import com.example.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 商品分类服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    private final CategoryMapper categoryMapper;

    @Override
    public List<Category> getCategoryTree() {
        return categoryMapper.selectCategoryTree();
    }

    @Override
    public List<Category> getChildrenByParentId(Long parentId) {
        if (parentId == null) {
            return new ArrayList<>();
        }
        return categoryMapper.selectByParentId(parentId);
    }

    @Override
    public List<Category> getListByLevel(Integer level) {
        if (level == null || level < 1 || level > 3) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "分类层级无效");
        }
        return categoryMapper.selectByLevel(level);
    }

    @Override
    public IPage<Category> getCategoryPage(Page<Category> page, Map<String, Object> params) {
        return categoryMapper.selectCategoryPage(page, params);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Category createCategory(Category category) {
        // 验证分类信息
        validateCategory(category);

        // 检查分类名称是否已存在（同一父级下）
        Long parentId = category.getParentId() == null ? 0L : category.getParentId();
        if (checkNameExists(category.getName(), parentId)) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "同一父级下已存在相同名称的分类");
        }

        // 设置层级
        setLevel(category);

        // 设置创建和更新时间
        LocalDateTime now = LocalDateTime.now();
        category.setCreateTime(now);
        category.setUpdateTime(now);

        // 如果状态为空，默认为启用状态
        if (category.getStatus() == null) {
            category.setStatus(CategoryConstants.STATUS_ENABLED);
        }

        // 保存分类
        boolean success = save(category);
        if (!success) {
            throw new BusinessException(ResultCode.FAILED, "创建分类失败");
        }

        return category;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateCategory(Category category) {
        // 验证分类ID
        if (category.getId() == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "分类ID不能为空");
        }

        // 检查分类是否存在
        Category existingCategory = getById(category.getId());
        if (existingCategory == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "分类不存在");
        }

        // 如果更新了分类名称，检查是否重名
        if (StringUtils.hasText(category.getName()) 
                && !category.getName().equals(existingCategory.getName())) {
            Long parentId = existingCategory.getParentId() == null ? 0L : existingCategory.getParentId();
            if (checkNameExists(category.getName(), parentId)) {
                throw new BusinessException(ResultCode.VALIDATE_FAILED, "同一父级下已存在相同名称的分类");
            }
        }

        // 不允许修改层级和父分类
        category.setLevel(null);
        category.setParentId(null);

        // 设置更新时间
        category.setUpdateTime(LocalDateTime.now());

        return updateById(category);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteCategory(Long id) {
        if (id == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "分类ID不能为空");
        }

        // 检查分类是否存在
        Category category = getById(id);
        if (category == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "分类不存在");
        }

        // 检查是否有子分类
        List<Category> children = getChildrenByParentId(id);
        if (!CollectionUtils.isEmpty(children)) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "该分类存在子分类，无法删除");
        }

        // 删除分类
        return removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteCategoryWithChildren(Long id) {
        if (id == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "分类ID不能为空");
        }

        // 检查分类是否存在
        Category category = getById(id);
        if (category == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "分类不存在");
        }

        // 递归删除分类及其子分类
        int rows = categoryMapper.deleteWithChildren(id);
        return rows > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateStatus(List<Long> ids, Integer status) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "分类ID列表不能为空");
        }
        if (status == null || (status != CategoryConstants.STATUS_DISABLED && status != CategoryConstants.STATUS_ENABLED)) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "分类状态无效");
        }

        int rows = categoryMapper.batchUpdateStatus(ids, status);
        return rows > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateCategoryParent(Long id, Long parentId) {
        if (id == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "分类ID不能为空");
        }

        // 检查分类是否存在
        Category category = getById(id);
        if (category == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "分类不存在");
        }

        // 如果父分类ID为空，表示设为一级分类
        if (parentId == null || parentId == 0) {
            parentId = 0L;
            int rows = categoryMapper.updateParent(id, parentId, CategoryConstants.LEVEL_FIRST);
            return rows > 0;
        }

        // 检查父分类是否存在
        Category parentCategory = getById(parentId);
        if (parentCategory == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "父分类不存在");
        }

        // 不能将分类的父级设置为其子分类
        List<Long> childIds = getAllChildCategoryIds(id);
        if (childIds.contains(parentId)) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "不能将分类的父级设置为其子分类");
        }

        // 计算新的层级
        int newLevel;
        if (parentCategory.getLevel() == CategoryConstants.LEVEL_THIRD) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "三级分类下不能添加子分类");
        } else {
            newLevel = parentCategory.getLevel() + 1;
        }

        // 更新父分类和层级
        int rows = categoryMapper.updateParent(id, parentId, newLevel);
        return rows > 0;
    }

    @Override
    public List<Map<String, Object>> countCategoryByLevel() {
        return categoryMapper.countCategoryByLevel();
    }

    @Override
    public List<Category> getCategoryPath(Long categoryId) {
        if (categoryId == null) {
            return new ArrayList<>();
        }
        return categoryMapper.selectCategoryPath(categoryId);
    }

    @Override
    public List<Category> getAllEnabledCategories() {
        return categoryMapper.selectAllEnabled();
    }

    @Override
    public List<Category> getCategoriesWithProducts() {
        return categoryMapper.selectCategoriesWithProducts();
    }

    @Override
    public List<Category> getHotCategories(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = CategoryConstants.DEFAULT_HOT_CATEGORIES_LIMIT; // 默认10个
        }
        return categoryMapper.selectHotCategories(limit);
    }

    @Override
    public boolean checkNameExists(String name, Long parentId) {
        if (!StringUtils.hasText(name)) {
            return false;
        }
        if (parentId == null) {
            parentId = 0L;
        }
        int count = categoryMapper.existsByNameAndParent(name, parentId);
        return count > 0;
    }

    @Override
    public List<Category> getFullCategoryTree() {
        // 查询所有启用状态的分类
        List<Category> allCategories = categoryMapper.selectAllEnabled();
        if (CollectionUtils.isEmpty(allCategories)) {
            return new ArrayList<>();
        }

        // 将所有分类按层级和ID排序
        allCategories.sort((a, b) -> {
            if (!Objects.equals(a.getLevel(), b.getLevel())) {
                return a.getLevel() - b.getLevel();
            }
            return a.getId().compareTo(b.getId());
        });

        // 构建分类树
        List<Category> rootCategories = new ArrayList<>();
        Map<Long, List<Category>> childrenMap = allCategories.stream()
                .filter(c -> c.getParentId() != null && c.getParentId() > 0)
                .collect(Collectors.groupingBy(Category::getParentId));

        // 找出一级分类
        for (Category category : allCategories) {
            if (category.getParentId() == null || category.getParentId() == 0) {
                category.setChildren(childrenMap.getOrDefault(category.getId(), new ArrayList<>()));
                rootCategories.add(category);
            }
        }

        // 为二级分类设置子分类
        for (Category rootCategory : rootCategories) {
            if (rootCategory.getChildren() != null) {
                for (Category secondCategory : rootCategory.getChildren()) {
                    secondCategory.setChildren(childrenMap.getOrDefault(secondCategory.getId(), new ArrayList<>()));
                }
            }
        }

        return rootCategories;
    }

    @Override
    public List<Long> getAllChildCategoryIds(Long categoryId) {
        if (categoryId == null) {
            return new ArrayList<>();
        }

        List<Long> childIds = new ArrayList<>();
        // 获取直接子分类
        List<Category> children = getChildrenByParentId(categoryId);
        if (!CollectionUtils.isEmpty(children)) {
            // 添加直接子分类ID
            List<Long> directChildIds = children.stream().map(Category::getId).collect(Collectors.toList());
            childIds.addAll(directChildIds);

            // 递归获取所有子分类的子分类ID
            for (Long childId : directChildIds) {
                childIds.addAll(getAllChildCategoryIds(childId));
            }
        }
        return childIds;
    }

    /**
     * 根据父分类ID设置分类层级
     *
     * @param category 分类
     */
    private void setLevel(Category category) {
        if (category.getParentId() == null || category.getParentId() == 0) {
            // 根分类，一级分类
            category.setParentId(0L);
            category.setLevel(CategoryConstants.LEVEL_FIRST);
        } else {
            // 查询父分类
            Category parentCategory = getById(category.getParentId());
            if (parentCategory == null) {
                throw new BusinessException(ResultCode.VALIDATE_FAILED, "父分类不存在");
            }

            // 根据父分类层级设置当前分类层级
            if (parentCategory.getLevel() == CategoryConstants.LEVEL_FIRST) {
                category.setLevel(CategoryConstants.LEVEL_SECOND);
            } else if (parentCategory.getLevel() == CategoryConstants.LEVEL_SECOND) {
                category.setLevel(CategoryConstants.LEVEL_THIRD);
            } else {
                throw new BusinessException(ResultCode.VALIDATE_FAILED, "最多支持三级分类");
            }
        }
    }

    /**
     * 验证分类信息
     *
     * @param category 分类信息
     */
    private void validateCategory(Category category) {
        if (category == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "分类信息不能为空");
        }

        if (!StringUtils.hasText(category.getName())) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "分类名称不能为空");
        }

        // 校验分类名称长度
        if (category.getName().length() > CategoryConstants.MAX_NAME_LENGTH) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, 
                    "分类名称长度不能超过" + CategoryConstants.MAX_NAME_LENGTH + "个字符");
        }
    }
} 