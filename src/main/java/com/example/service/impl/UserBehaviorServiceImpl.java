package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.UserBehaviorMapper;
import com.example.model.entity.UserBehavior;
import com.example.service.UserBehaviorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户行为服务实现类
 * 
 * 该类实现了用户行为相关的业务逻辑，包括记录、查询、更新和删除用户行为数据。
 * 用户行为数据用于分析用户偏好、推荐商品和个性化营销等功能。
 * 使用了Spring缓存机制对用户行为数据进行缓存，提高查询效率。
 * 使用MyBatis-Plus的ServiceImpl简化数据访问操作。
 *
 * @author 31815
 * @description 针对表【user_behavior(用户行为记录表)】的数据库操作Service实现
 * @createDate 2025-02-24 12:03:47
 */
@Service
@CacheConfig(cacheNames = "userBehaviors") // 指定该服务类的缓存名称为"userBehaviors"
public class UserBehaviorServiceImpl extends ServiceImpl<UserBehaviorMapper, UserBehavior>
        implements UserBehaviorService {

    @Autowired
    private UserBehaviorMapper userBehaviorMapper;

    /**
     * 根据用户ID查询用户行为列表
     * 
     * 该方法从缓存或数据库获取指定用户的所有行为数据，
     * 可用于分析用户兴趣偏好和行为习惯
     *
     * @param userId 用户ID
     * @return 用户行为列表
     */
    @Override
    @Cacheable(value = "userBehaviors", key = "#userId") // 缓存用户行为数据，提高查询效率
    public List<UserBehavior> selectByUserId(Long userId) {
        return userBehaviorMapper.selectByUserId(userId);
    }

    /**
     * 分页查询用户行为数据
     * 
     * 该方法用于后台管理系统分页查看用户行为数据
     *
     * @param page 分页参数
     * @return 用户行为分页数据
     */
    @Override
    public IPage<UserBehavior> selectPage(IPage<UserBehavior> page) {
        return userBehaviorMapper.selectPage(page);
    }

    /**
     * 根据ID查询用户行为记录
     *
     * @param id 用户行为ID
     * @return 用户行为实体
     */
    @Override
    public UserBehavior selectById(Long id) {
        return userBehaviorMapper.selectById(id);
    }

    /**
     * 添加用户行为记录
     * 
     * 该方法用于记录用户的行为数据，如浏览商品、收藏商品、购买商品等，
     * 并清除相关用户的行为缓存，确保数据一致性
     *
     * @param userBehavior 用户行为实体
     * @return 添加成功返回true，失败返回false
     */
    @Override
    @Transactional
    @CacheEvict(value = "userBehaviors", key = "#userBehavior.userId") // 清除用户行为缓存
    public boolean insertUserBehavior(UserBehavior userBehavior) {
        return userBehaviorMapper.insert(userBehavior) > 0;
    }

    /**
     * 更新用户行为记录
     * 
     * 该方法用于更新用户行为数据，如修改行为权重、停留时长等，
     * 并清除相关用户的行为缓存，确保数据一致性
     *
     * @param userBehavior 用户行为实体
     * @return 更新成功返回true，失败返回false
     */
    @Override
    @Transactional
    @CacheEvict(value = "userBehaviors", key = "#userBehavior.userId") // 清除用户行为缓存
    public boolean updateUserBehavior(UserBehavior userBehavior) {
        return userBehaviorMapper.updateById(userBehavior) > 0;
    }

    /**
     * 删除用户行为记录
     * 
     * 该方法用于删除用户行为数据，
     * 并清除相关缓存，确保数据一致性
     *
     * @param id 用户行为ID
     * @return 删除成功返回true，失败返回false
     */
    @Override
    @Transactional
    @CacheEvict(value = "userBehaviors", key = "#id") // 清除被删除行为的缓存
    public boolean deleteUserBehavior(Long id) {
        return userBehaviorMapper.deleteById(id) > 0;
    }
}




