package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.service.CategoryService;
import com.example.model.entity.Category;
import com.example.mapper.CategoryMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.stream.Collectors;
import com.example.model.dto.category.CategoryPageDTO;
import com.example.model.dto.category.CategoryDTO;
import com.example.exception.BusinessException;
import com.example.common.ResultCode;

/**
* @author 31815
* @description 针对表【category(商品分类表)】的数据库操作Service实现
* @createDate 2025-02-18 23:44:29
*/
@Service
@CacheConfig(cacheNames = "categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService {

    //private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    /**
     * 分页查询分类（带树形结构）
     * @param queryDTO 查询参数
     * @return 分页结果（包含子分类）
     */
    @Override
    @Cacheable(key = "'page:' + #queryDTO.hashCode()")
    public IPage<Category> listCategoryPage(CategoryPageDTO queryDTO) {
        Page<Category> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        return baseMapper.selectCategoryPage(page, queryDTO);
    }

    /**
     * 添加分类（带业务校验）
     * @param categoryDTO 分类数据
     * @return 操作结果
     * @throws BusinessException 业务异常
     */
    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public boolean addCategory(CategoryDTO categoryDTO) {
        // 校验父分类
        validateParentCategory(categoryDTO.getParentId());
        
        // 检查名称唯一性
        if (checkNameExists(categoryDTO.getName(), categoryDTO.getParentId(), null)) {
            throw new BusinessException(ResultCode.CATEGORY_NAME_EXISTS);
        }

        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        return save(category);
    }

    /**
     * 更新分类（带完整校验）
     * @param categoryDTO 分类数据
     * @return 操作结果
     * @throws BusinessException 业务异常
     */
    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public boolean updateCategory(CategoryDTO categoryDTO) {
        Category existing = getById(categoryDTO.getId());
        if (existing == null) {
            throw new BusinessException(ResultCode.CATEGORY_NOT_FOUND);
        }

        // 校验层级变更
        if (!existing.getLevel().equals(categoryDTO.getLevel())) {
            throw new BusinessException(ResultCode.CATEGORY_LEVEL_CHANGE);
        }

        // 检查名称唯一性
        if (checkNameExists(categoryDTO.getName(), categoryDTO.getParentId(), categoryDTO.getId())) {
            throw new BusinessException(ResultCode.CATEGORY_NAME_EXISTS);
        }

        BeanUtils.copyProperties(categoryDTO, existing);
        return updateById(existing);
    }

    /**
     * 删除分类（级联删除子分类）
     * @param id 分类ID
     * @return 操作结果
     */
    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public boolean deleteCategory(Long id) {
        // 查找所有子分类
        List<Category> children = baseMapper.selectChildren(id);
        if (!children.isEmpty()) {
            List<Long> ids = children.stream().map(Category::getId).collect(Collectors.toList());
            ids.add(id);
            return removeBatchByIds(ids);
        }
        return removeById(id);
    }

    /**
     * 获取分类树（缓存优化）
     * @return 树形结构分类列表
     */
    @Override
    @Cacheable(key = "'tree'")
    public List<Category> getCategoryTree() {
        return baseMapper.selectCategoryTree();
    }

    /**
     * 切换分类状态（级联操作）
     * @param id 分类ID
     * @param status 新状态
     * @return 操作结果
     */
    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public boolean switchStatus(Long id, Integer status) {
        // 更新当前分类
        boolean success = lambdaUpdate()
                .eq(Category::getId, id)
                .set(Category::getStatus, status)
                .update();

        // 级联更新子分类
        if (success) {
            baseMapper.selectChildren(id).forEach(child -> 
                switchStatus(child.getId(), status)
            );
        }
        return success;
    }

    /**
     * 校验父分类合法性
     * @param parentId 父分类ID
     * @throws BusinessException 业务异常
     */
    private void validateParentCategory(Long parentId) {
        if (parentId != 0) {
            Category parent = getById(parentId);
            if (parent == null) {
                throw new BusinessException(ResultCode.CATEGORY_PARENT_INVALID);
            }
            if (parent.getStatus() == 0) {
                throw new BusinessException(ResultCode.CATEGORY_PARENT_DISABLED);
            }
            if (parent.getLevel() >= 3) {
                throw new BusinessException(ResultCode.CATEGORY_LEVEL_LIMIT);
            }
        }
    }

    /**
     * 检查分类名称唯一性
     */
    private boolean checkNameExists(String name, Long parentId, Long excludeId) {
        return baseMapper.checkNameUnique(name, parentId, excludeId) > 0;
    }
}




