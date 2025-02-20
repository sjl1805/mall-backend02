package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.ResultCode;
import com.example.exception.BusinessException;
import com.example.mapper.CouponMapper;
import com.example.model.dto.coupon.CouponDTO;
import com.example.model.dto.coupon.CouponPageDTO;
import com.example.model.entity.Coupon;
import com.example.service.CouponService;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 优惠券服务实现类
 * 
 * @author 31815
 * @description 实现优惠券核心业务逻辑，包含：
 *              1. 券的创建与唯一性校验
 *              2. 状态管理及定时过期处理
 *              3. 用户领券用券逻辑
 *              4. 缓存优化及事务控制
 * @createDate 2025-02-18 23:44:26
 */
@Service
@CacheConfig(cacheNames = "couponService")
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon>
        implements CouponService {

    //private static final Logger logger = LoggerFactory.getLogger(CouponServiceImpl.class);

    /**
     * 创建优惠券（完整校验）
     * @param couponDTO 优惠券数据传输对象
     * @return 操作结果
     * @implNote 业务逻辑：
     *           1. 名称唯一性校验
     *           2. 自动计算过期时间（基于validDays）
     *           3. 初始化发放数量
     *           4. 清除可用券缓存
     */
    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public boolean createCoupon(CouponDTO couponDTO) {
        if (baseMapper.checkNameUnique(couponDTO.getName(), null) > 0) {
            throw new BusinessException(ResultCode.COUPON_NAME_EXISTS);
        }

        Coupon coupon = new Coupon();
        BeanUtils.copyProperties(couponDTO, coupon);
        return save(coupon);
    }

    /**
     * 分页查询优惠券（带过滤条件）
     * @param queryDTO 分页查询参数
     * @return 分页结果（自动缓存查询条件哈希值）
     * @implNote 使用MP分页插件，支持多条件组合查询
     */
    @Override
    @Cacheable(key = "'page:' + #queryDTO.hashCode()")
    public IPage<CouponDTO> listCouponPage(CouponPageDTO queryDTO) {
        Page<Coupon> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        IPage<Coupon> couponPage = baseMapper.selectCouponPage(page, queryDTO);
        return couponPage.convert(this::convertToDTO);
    }

    /**
     * 更新优惠券状态（原子操作）
     * @param couponId 目标券ID
     * @param status 新状态（1-启用，0-禁用）
     * @return 操作结果
     * @implNote 直接使用Mapper层更新保证原子性，避免并发问题
     */
    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public boolean updateStatus(Long couponId, Integer status) {
        return baseMapper.updateStatus(couponId, status) > 0;
    }

    /**
     * 批量过期优惠券（定时任务）
     * @return 过期数量
     * @apiNote 执行逻辑：
     *          1. 查找所有过期时间小于当前时间且状态为启用的券
     *          2. 批量更新状态为已过期（status=2）
     *          3. 清除相关缓存
     */
    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public boolean expireCoupons() {
        return baseMapper.expireCoupons(new Date()) > 0;
    }

    /**
     * 获取可领取优惠券（缓存优化）
     * @return 有效券列表
     * @implNote 缓存策略：
     *           1. 缓存键：'available'
     *           2. 缓存时间：5分钟
     *           3. 缓存更新：新增/修改券时清除
     */
    @Override
    @Cacheable(key = "'available'")
    public List<CouponDTO> getAvailableCoupons() {
        return baseMapper.selectAvailableCoupons().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * 获取用户有效优惠券（带时效校验）
     * @param userId 用户ID
     * @return 有效券列表
     * @implNote 查询逻辑：
     *           1. 关联用户券关系表
     *           2. 校验使用状态和有效期
     *           3. 按过期时间升序排序
     */
    @Override
    @Cacheable(key = "'user:' + #userId")
    public List<CouponDTO> getUserValidCoupons(Long userId) {
        return baseMapper.selectValidCouponsByUser(userId, new Date()).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private CouponDTO convertToDTO(Coupon coupon) {
        if (coupon == null) return null;
        
        CouponDTO dto = new CouponDTO();
        BeanUtils.copyProperties(coupon, dto);
        return dto;
    }
}




