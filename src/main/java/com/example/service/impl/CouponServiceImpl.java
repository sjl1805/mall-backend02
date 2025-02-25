package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.CouponMapper;
import com.example.mapper.UserCouponMapper;
import com.example.model.entity.Coupon;
import com.example.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * 优惠券服务实现类
 * 
 * 该类实现了优惠券相关的业务逻辑，包括优惠券的创建、查询、更新和删除等功能。
 * 优惠券是电商系统中重要的营销工具，用于刺激消费、提高转化率和客户忠诚度。
 * 系统支持多种类型的优惠券，如满减券、折扣券、无门槛券等，并可设置使用条件和有效期。
 * 使用了Spring缓存机制对优惠券信息进行缓存，提高查询效率，减轻数据库压力。
 * 使用MyBatis-Plus的ServiceImpl简化数据访问操作。
 *
 * @author 31815
 * @description 针对表【coupon(优惠券表)】的数据库操作Service实现
 * @createDate 2025-02-24 12:04:19
 */
@Service
@CacheConfig(cacheNames = "coupons") // 指定该服务类的缓存名称为"coupons"
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon>
        implements CouponService {

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    /**
     * 根据名称查询优惠券列表
     * 
     * 该方法从缓存或数据库获取指定名称的优惠券，
     * 用于前台展示特定活动的优惠券或后台管理系统查询
     *
     * @param name 优惠券名称
     * @return 优惠券列表
     */
    @Override
    @Cacheable(key = "#name") // 缓存优惠券数据，提高查询效率
    public List<Coupon> selectByName(String name) {
        return couponMapper.selectByName(name);
    }

    /**
     * 分页查询优惠券数据
     * 
     * 该方法用于后台管理系统分页查看优惠券数据，
     * 支持按类型、状态、有效期等条件筛选，便于管理员全面了解优惠券情况
     *
     * @param page 分页参数
     * @return 优惠券分页数据
     */
    @Override
    public IPage<Coupon> selectPage(IPage<Coupon> page) {
        return couponMapper.selectPage(page);
    }

    /**
     * 根据ID查询优惠券
     * 
     * 该方法从缓存或数据库获取指定ID的优惠券详情，
     * 用于查看优惠券详细信息或验证优惠券有效性
     *
     * @param id 优惠券ID
     * @return 优惠券实体
     */
    @Override
    @Cacheable(key = "#id") // 缓存优惠券详情，提高查询效率
    public Coupon selectById(Long id) {
        return couponMapper.selectById(id);
    }

    /**
     * 创建优惠券
     * 
     * 该方法用于后台管理系统创建新的优惠券，
     * 设置优惠券名称、类型、面值、使用条件、有效期等属性，
     * 是优惠券营销活动的基础操作
     *
     * @param coupon 优惠券实体
     * @return 创建成功返回true，失败返回false
     */
    @Override
    @Transactional
    @CacheEvict(allEntries = true) // 清除所有优惠券缓存
    public boolean insertCoupon(Coupon coupon) {
        return couponMapper.insert(coupon) > 0;
    }

    /**
     * 更新优惠券
     * 
     * 该方法用于修改优惠券信息，如调整面值、使用条件、有效期等，
     * 用于优化营销策略或修正错误信息，
     * 并清除相关缓存，确保数据一致性
     *
     * @param coupon 优惠券实体
     * @return 更新成功返回true，失败返回false
     */
    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(key = "#coupon.id"),
        @CacheEvict(key = "'available'")
    })
    public boolean updateCoupon(Coupon coupon) {
        return couponMapper.updateById(coupon) > 0;
    }

    /**
     * 删除优惠券
     * 
     * 该方法用于删除不再需要的优惠券，
     * 通常在营销活动结束后使用，
     * 需要注意的是，已发放给用户的优惠券可能需要特殊处理，
     * 并清除相关缓存
     *
     * @param id 优惠券ID
     * @return 删除成功返回true，失败返回false
     */
    @Override
    @Transactional
    @CacheEvict(allEntries = true) // 清除所有优惠券缓存
    public boolean deleteCoupon(Long id) {
        return couponMapper.deleteById(id) > 0;
    }

    /**
     * 设置优惠券有效期
     * 
     * 该方法用于设置或修改优惠券的有效期，
     * 可以灵活调整营销策略，如延长热门优惠券的有效期或缩短错误发放的优惠券有效期，
     * 是优惠券管理的重要功能
     *
     * @param id 优惠券ID
     * @param startTime 有效期开始时间，格式为"yyyy-MM-dd HH:mm:ss"
     * @param endTime 有效期结束时间，格式为"yyyy-MM-dd HH:mm:ss"
     * @return 设置成功返回true，失败返回false
     * @throws RuntimeException 当优惠券不存在时抛出
     */
    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(key = "#id"),
        @CacheEvict(key = "'available'"),
        @CacheEvict(key = "'expiring'")
    })
    public boolean setCouponValidity(Long id, String startTime, String endTime) {
        Coupon coupon = couponMapper.selectById(id);
        if (coupon == null) {
            throw new RuntimeException("优惠券不存在");
        }
        coupon.setStartTime(LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        coupon.setEndTime(LocalDateTime.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return couponMapper.updateById(coupon) > 0;
    }

    /**
     * 设置优惠券使用条件
     * 
     * 该方法用于设置或修改优惠券的使用门槛，如最低消费金额，
     * 可以根据不同的营销目标调整使用条件，
     * 比如提高高价值商品的销售或促进特定类目的消费
     *
     * @param id 优惠券ID
     * @param minAmount 最低消费金额
     * @return 设置成功返回true，失败返回false
     * @throws RuntimeException 当优惠券不存在时抛出
     */
    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(key = "#id"),
        @CacheEvict(key = "'available'")
    })
    public boolean setCouponConditions(Long id, BigDecimal minAmount) {
        Coupon coupon = couponMapper.selectById(id);
        if (coupon == null) {
            throw new RuntimeException("优惠券不存在");
        }
        coupon.setMinAmount(minAmount);
        return couponMapper.updateById(coupon) > 0;
    }
    
    /**
     * 查询可用优惠券
     * 
     * 该方法根据订单金额查询当前可用的优惠券，
     * 通常用于结算页面向用户推荐可用的优惠券，
     * 提高用户体验和促进转化
     *
     * @param amount 订单金额
     * @return 可用优惠券列表
     */
    @Override
    @Cacheable(key = "'available_'+#amount")
    public List<Coupon> selectAvailableCoupons(BigDecimal amount) {
        return couponMapper.selectAvailableCoupons(new Date(), amount.doubleValue());
    }
    
    /**
     * 查询即将过期的优惠券
     * 
     * 该方法查询指定天数内即将过期的优惠券，
     * 用于向用户发送提醒或在前台显示"即将过期"标签，
     * 促进用户使用优惠券，提高活动效果
     *
     * @param days 天数，如7天内过期
     * @return 即将过期的优惠券列表
     */
    @Override
    @Cacheable(key = "'expiring_'+#days")
    public List<Coupon> selectExpiringSoon(Integer days) {
        Calendar calendar = Calendar.getInstance();
        Date startDate = calendar.getTime();
        
        calendar.add(Calendar.DAY_OF_MONTH, days);
        Date endDate = calendar.getTime();
        
        return couponMapper.selectExpiringSoon(startDate, endDate);
    }
    
    /**
     * 减少优惠券数量
     * 
     * 该方法在用户领取优惠券时调用，
     * 减少优惠券的可领取数量，防止超发，
     * 是优惠券发放环节的关键控制点
     *
     * @param id 优惠券ID
     * @param count 减少数量
     * @return 减少成功返回true，失败返回false
     */
    @Override
    @Transactional
    @CacheEvict(key = "#id")
    public boolean decreaseCouponNum(Long id, Integer count) {
        return couponMapper.decreaseCouponNum(id, count) > 0;
    }
    
    /**
     * 检查优惠券是否可用于指定金额
     * 
     * 该方法在用户选择优惠券时调用，
     * 验证优惠券是否满足使用条件（如最低消费金额），
     * 确保优惠券使用的合规性
     *
     * @param id 优惠券ID
     * @param amount 订单金额
     * @return 可用优惠券，不可用返回null
     */
    @Override
    public Coupon checkCouponAvailable(Long id, BigDecimal amount) {
        return couponMapper.checkCouponAvailable(id, amount.doubleValue(), new Date());
    }
    
    /**
     * 获取优惠券使用统计
     * 
     * 该方法统计指定时间段内优惠券的使用情况，
     * 包括使用次数、优惠总额、关联订单总额等数据，
     * 用于评估营销活动效果，为运营决策提供依据
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 优惠券统计数据
     */
    @Override
    public List<Map<String, Object>> getCouponStatistics(Date startDate, Date endDate) {
        return couponMapper.getCouponStatistics(startDate, endDate);
    }
    
    /**
     * 获取热门优惠券排行
     * 
     * 该方法获取领取量最多的优惠券排行，
     * 同时计算使用率等指标，
     * 用于分析用户偏好和评估优惠券吸引力
     *
     * @param limit 限制数量
     * @return 热门优惠券列表
     */
    @Override
    public List<Map<String, Object>> getPopularCoupons(Integer limit) {
        return couponMapper.getPopularCoupons(limit);
    }
    
    /**
     * 批量更新优惠券状态
     * 
     * 该方法一次性更新多个优惠券的状态，
     * 如批量上线或下线优惠券，
     * 提高管理效率
     *
     * @param ids 优惠券ID列表
     * @param status 状态
     * @return 更新成功返回true，失败返回false
     */
    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public boolean batchUpdateStatus(List<Long> ids, Integer status) {
        return couponMapper.batchUpdateStatus(ids, status) > 0;
    }
    
    /**
     * 批量删除优惠券
     * 
     * 该方法一次性删除多个优惠券，
     * 通常用于清理过期或无效的优惠券，
     * 提高管理效率
     *
     * @param ids 优惠券ID列表
     * @return 删除成功返回true，失败返回false
     */
    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public boolean batchDelete(List<Long> ids) {
        return couponMapper.batchDelete(ids) > 0;
    }

    /**
     * 查询用户未领取的优惠券
     *
     * 该方法查询当前有效且用户尚未领取的优惠券，
     * 用于在前台"领券中心"展示可供用户领取的优惠券
     *
     * @param userId 用户ID
     * @return 未领取的有效优惠券
     */
    @Override
    @Cacheable(key = "'notreceived_'+#userId")
    public List<Coupon> getNotReceivedCoupons(Long userId) {
        return couponMapper.selectNotReceivedCoupons(userId, new Date());
    }

    /**
     * 按条件高级查询优惠券
     *
     * 该方法支持多条件组合查询优惠券，
     * 用于后台管理系统的高级搜索功能
     *
     * @param params 查询参数Map
     * @return 优惠券列表
     */
    @Override
    public List<Coupon> advancedSearch(Map<String, Object> params) {
        if (params == null) {
            params = new HashMap<>();
        }
        return couponMapper.advancedSearch(params);
    }

    /**
     * 计算优惠券发放效果
     *
     * 该方法统计优惠券的发放效果，包括发放量、使用量、使用率等，
     * 用于评估营销活动的效果，为后续优惠策略调整提供数据支持
     *
     * @param couponId 优惠券ID
     * @return 统计数据
     */
    @Override
    @Cacheable(key = "'effect_'+#couponId")
    public Map<String, Object> calculateCouponEffectiveness(Long couponId) {
        return couponMapper.calculateCouponEffectiveness(couponId);
    }

    /**
     * 自动失效过期优惠券
     *
     * 该方法将已过期但状态仍为有效的优惠券状态更新为失效，
     * 通常由定时任务调用，确保系统中的优惠券状态准确
     *
     * @return 更新的记录数
     */
    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public int expireOutdatedCoupons() {
        return couponMapper.expireCoupons(new Date());
    }

    /**
     * 为用户推荐优惠券
     *
     * 该方法基于用户的历史订单和浏览记录，推荐合适的优惠券，
     * 提高用户的购买意愿和转化率
     *
     * @param userId 用户ID
     * @param limit 推荐数量
     * @return 推荐的优惠券列表
     */
    @Override
    @Cacheable(key = "'recommend_'+#userId+'_'+#limit")
    public List<Coupon> recommendCouponsForUser(Long userId, Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 3; // 默认推荐3张
        }
        
        // 可以根据用户的历史订单和浏览记录进行个性化推荐
        // 这里简化为获取通用的热门优惠券
        List<Map<String, Object>> popularCoupons = getPopularCoupons(limit);
        
        // 将Map转换为Coupon对象
        List<Coupon> recommendCoupons = new ArrayList<>();
        for (Map<String, Object> map : popularCoupons) {
            Long couponId = (Long) map.get("id");
            Coupon coupon = selectById(couponId);
            if (coupon != null && coupon.getStatus() == 1) {
                recommendCoupons.add(coupon);
            }
        }
        
        return recommendCoupons;
    }

    /**
     * 批量发放优惠券给用户
     *
     * 该方法批量发放指定优惠券给多个用户，
     * 适用于批量营销活动，如新用户注册奖励、会员福利等
     *
     * @param couponId 优惠券ID
     * @param userIds 用户ID列表
     * @return 发放成功的用户数
     */
    @Override
    @Transactional
    public int batchIssueCoupons(Long couponId, List<Long> userIds) {
        if (couponId == null || userIds == null || userIds.isEmpty()) {
            return 0;
        }
        
        // 获取优惠券信息
        Coupon coupon = selectById(couponId);
        if (coupon == null || coupon.getStatus() != 1 || coupon.getNum() < userIds.size()) {
            return 0;
        }
        
        // 优惠券减少数量
        if (!decreaseCouponNum(couponId, userIds.size())) {
            return 0;
        }
        
        // 为用户发放优惠券（假设有userCouponMapper）
        int successCount = 0;
        for (Long userId : userIds) {
            try {
                 userCouponMapper.insertUserCoupon(userId, couponId);
                successCount++;
            } catch (Exception e) {
                // 记录日志
                continue;
            }
        }
        
        return successCount;
    }
}



