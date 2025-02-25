package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.ResultCode;
import com.example.constants.UserTagConstants;
import com.example.exception.BusinessException;
import com.example.mapper.UserTagMapper;
import com.example.model.entity.UserTag;
import com.example.service.UserTagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 用户标签服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserTagServiceImpl extends ServiceImpl<UserTagMapper, UserTag> implements UserTagService {

    private final UserTagMapper userTagMapper;

    @Override
    public UserTag getByName(String name) {
        if (!StringUtils.hasText(name)) {
            return null;
        }
        return userTagMapper.selectByName(name);
    }

    @Override
    public List<UserTag> getListByType(Integer type) {
        if (type == null || type < 1 || type > 4) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "标签类型无效");
        }
        return userTagMapper.selectListByType(type);
    }

    @Override
    public IPage<UserTag> getTagPage(Page<UserTag> page, Map<String, Object> params) {
        return userTagMapper.selectTagPage(page, params);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserTag createTag(UserTag userTag) {
        // 验证标签信息
        validateTag(userTag);

        // 检查标签名称是否存在
        if (checkNameExists(userTag.getName())) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "标签名称已存在");
        }

        // 设置创建和更新时间
        LocalDateTime now = LocalDateTime.now();
        userTag.setCreateTime(now);
        userTag.setUpdateTime(now);

        // 保存标签
        boolean success = save(userTag);
        if (!success) {
            throw new BusinessException(ResultCode.FAILED, "创建标签失败");
        }

        return userTag;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateTag(UserTag userTag) {
        // 验证标签ID
        if (userTag.getId() == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "标签ID不能为空");
        }

        // 检查标签是否存在
        UserTag existingTag = getById(userTag.getId());
        if (existingTag == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "标签不存在");
        }

        // 如果标签名称已更改，检查新名称是否存在
        if (StringUtils.hasText(userTag.getName()) 
                && !userTag.getName().equals(existingTag.getName()) 
                && checkNameExists(userTag.getName())) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "标签名称已存在");
        }

        // 设置更新时间
        userTag.setUpdateTime(LocalDateTime.now());

        return updateById(userTag);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTag(Long id) {
        if (id == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "标签ID不能为空");
        }

        // 检查标签是否存在
        UserTag tag = getById(id);
        if (tag == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "标签不存在");
        }

        // 删除标签
        return removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchCreateTags(List<UserTag> tagList) {
        if (tagList == null || tagList.isEmpty()) {
            return false;
        }

        // 设置创建和更新时间
        LocalDateTime now = LocalDateTime.now();
        for (UserTag tag : tagList) {
            validateTag(tag);
            tag.setCreateTime(now);
            tag.setUpdateTime(now);
        }

        // 批量插入标签
        int rows = userTagMapper.batchInsert(tagList);
        return rows > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteTags(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }

        int rows = userTagMapper.batchDeleteByIds(ids);
        return rows > 0;
    }

    @Override
    public List<UserTag> searchByKeyword(String keyword, Integer limit) {
        if (!StringUtils.hasText(keyword)) {
            return List.of();
        }
        
        if (limit == null || limit <= 0) {
            limit = UserTagConstants.DEFAULT_SEARCH_LIMIT; // 默认返回10条
        }
        
        return userTagMapper.searchByKeyword(keyword, limit);
    }

    @Override
    public List<UserTag> getPopularTags(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = UserTagConstants.DEFAULT_POPULAR_TAG_LIMIT; // 默认返回20个常用标签
        }
        
        return userTagMapper.selectPopularTags(limit);
    }

    @Override
    public List<Map<String, Object>> countTagByType() {
        return userTagMapper.countTagByType();
    }

    @Override
    public boolean checkNameExists(String name) {
        if (!StringUtils.hasText(name)) {
            return false;
        }
        
        int count = userTagMapper.existsByName(name);
        return count > 0;
    }

    @Override
    public List<UserTag> getTagsByUserId(Long userId) {
        if (userId == null) {
            return List.of();
        }
        
        return userTagMapper.selectTagsByUserId(userId);
    }
    
    @Override
    public List<UserTag> getRecommendedTags(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = UserTagConstants.DEFAULT_RECOMMENDED_TAG_LIMIT;
        }
        
        return userTagMapper.selectRecommendedTags(limit);
    }
    
    @Override
    public List<Map<String, Object>> countTagUsage(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = UserTagConstants.DEFAULT_USAGE_STAT_LIMIT;
        }
        
        return userTagMapper.countTagUsage(limit);
    }

    /**
     * 验证标签信息
     *
     * @param userTag 标签信息
     */
    private void validateTag(UserTag userTag) {
        if (userTag == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "标签信息不能为空");
        }

        if (!StringUtils.hasText(userTag.getName())) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "标签名称不能为空");
        }

        if (userTag.getType() == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "标签类型不能为空");
        }

        if (userTag.getType() < 1 || userTag.getType() > 4) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "标签类型无效");
        }
    }
} 