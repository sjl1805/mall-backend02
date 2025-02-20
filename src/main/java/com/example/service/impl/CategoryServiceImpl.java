package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.ResultCode;
import com.example.exception.BusinessException;
import com.example.mapper.CategoryMapper;
import com.example.model.dto.category.CategoryDTO;
import com.example.model.dto.category.CategoryPageDTO;
import com.example.model.entity.Category;
import com.example.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品分类服务实现类
 * 
 * @author 31815
 * @description 实现商品分类的完整业务逻辑，包含：
 *              1. 分类的增删改查基础操作
 *              2. 树形结构展示
 *              3. 级联状态管理
 *              4. 业务规则校验
 *              5. 缓存优化处理
 * @createDate 2025-02-18 23:44:29
 */
@Service
@CacheConfig(cacheNames = "categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
        implements CategoryService {

    private static final Logger log = LoggerFactory.getLogger(CategoryServiceImpl.class);

    /**
     * 分页查询分类（带树形结构）
     * @param queryDTO 分页查询参数：
     *                 - page: 当前页码
     *                 - size: 每页条数
     *                 - name: 分类名称模糊查询
     *                 - status: 状态过滤
     * @return 分页结果（自动构建树形结构）
     * @implNote 使用MP分页插件，查询结果自动缓存
     */
    @Override
    @Cacheable(key = "'page:' + #queryDTO.hashCode()")
    public IPage<Category> listCategoryPage(CategoryPageDTO queryDTO) {
        Page<Category> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        return baseMapper.selectCategoryPage(page, queryDTO);
    }

    /**
     * 添加分类（完整业务校验）
     * @param categoryDTO 分类数据传输对象，必须包含：
     *                    - name: 分类名称
     *                    - parentId: 父分类ID（0表示一级分类）
     *                    - level: 分类层级（由前端计算或后端校验）
     * @return 操作结果
     * @throws BusinessException 校验失败时抛出：
     *                           - CATEGORY_PARENT_INVALID: 父分类不存在
     *                           - CATEGORY_PARENT_DISABLED: 父分类已禁用
     *                           - CATEGORY_LEVEL_LIMIT: 超过三级分类限制
     *                           - CATEGORY_NAME_EXISTS: 同级分类名称重复
     * @implNote 使用Spring事务管理，操作成功后清除所有缓存
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
     * 更新分类（全字段校验）
     * @param categoryDTO 分类数据传输对象，必须包含有效ID
     * @return 操作结果
     * @throws BusinessException 校验失败时抛出：
     *                           - CATEGORY_NOT_FOUND: 分类不存在
     *                           - CATEGORY_LEVEL_CHANGE: 试图修改分类层级
     *                           - CATEGORY_NAME_EXISTS: 同级分类名称重复
     * @implNote 更新操作会检查数据版本（乐观锁），确保数据一致性
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
     * @param id 要删除的分类ID
     * @return 操作结果
     * @apiNote 删除逻辑：
     *          1. 查找所有子分类（递归查询）
     *          2. 批量删除当前分类及其所有子分类
     *          3. 自动处理关联商品数据的分类信息（需在Mapper层实现）
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
     * 获取全量分类树（缓存优化）
     * @return 树形结构分类列表，结构示例：
     *         - 家电（level=1）
     *           - 大家电（level=2）
     *             - 空调（level=3）
     * @implNote 使用Redis缓存树形结构数据，有效降低数据库压力
     */
    @Override
    @Cacheable(key = "'tree'", unless = "#result == null || #result.isEmpty()")
    public List<Category> getCategoryTree(Long parentId) {
        log.debug("Building category tree from parentId: {}", parentId);
        List<Category> tree = baseMapper.selectCategoryTree(parentId);
        log.debug("Category tree nodes count: {}", tree.size());
        return tree;
    }

    /**
     * 切换分类状态（级联操作）
     * @param id 目标分类ID
     * @param status 新状态（1-启用，0-禁用）
     * @return 操作结果
     * @implNote 状态变更逻辑：
     *           1. 更新当前分类状态
     *           2. 递归更新所有子分类状态
     *           3. 自动更新缓存
     * @throws BusinessException 当目标分类不存在时抛出CATEGORY_NOT_FOUND
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
     * 校验父分类合法性（内部方法）
     * @param parentId 父分类ID
     * @throws BusinessException 当出现以下情况时抛出异常：
     *                           1. 父分类不存在
     *                           2. 父分类已禁用
     *                           3. 父分类层级超过限制（>=3）
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
     * 检查分类名称唯一性（内部方法）
     * @param name 待检查名称
     * @param parentId 父分类ID（用于确定同级）
     * @param excludeId 需要排除的ID（用于更新操作）
     * @return 是否存在重复名称
     * @implNote SQL逻辑：统计同一父分类下同名且ID不同的记录数
     */
    private boolean checkNameExists(String name, Long parentId, Long excludeId) {
        return baseMapper.checkNameUnique(name, parentId, excludeId) > 0;
    }
}




