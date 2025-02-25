package com.example.model.dto;

import com.example.model.entity.Coupon;
import com.example.model.entity.UserCoupon;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户优惠券数据传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCouponDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 用户优惠券ID
     */
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 优惠券ID
     */
    private Long couponId;
    
    /**
     * 优惠券名称
     */
    private String couponName;
    
    /**
     * 优惠券类型
     */
    private Integer couponType;
    
    /**
     * 优惠金额
     */
    private BigDecimal amount;
    
    /**
     * 使用门槛
     */
    private BigDecimal minAmount;
    
    /**
     * 状态：0-未使用 1-已使用 2-已过期
     */
    private Integer status;
    
    /**
     * 使用时间
     */
    private LocalDateTime useTime;
    
    /**
     * 使用订单ID
     */
    private Long orderId;
    
    /**
     * 开始时间
     */
    private LocalDateTime startTime;
    
    /**
     * 结束时间
     */
    private LocalDateTime endTime;
    
    /**
     * 创建时间（领取时间）
     */
    private LocalDateTime createTime;
    
    /**
     * 是否即将过期
     */
    private Boolean soonToExpire;
    
    /**
     * 剩余天数
     */
    private Integer remainDays;
    
    /**
     * 从实体类转换为DTO
     * @param userCoupon 用户优惠券实体
     * @param coupon 优惠券实体
     * @return DTO对象
     */
    public static UserCouponDTO fromEntity(UserCoupon userCoupon, Coupon coupon) {
        if (userCoupon == null) {
            return null;
        }
        
        UserCouponDTO dto = new UserCouponDTO();
        dto.setId(userCoupon.getId());
        dto.setUserId(userCoupon.getUserId());
        dto.setCouponId(userCoupon.getCouponId());
        dto.setStatus(userCoupon.getStatus());
        dto.setUseTime(userCoupon.getUseTime());
        dto.setOrderId(userCoupon.getOrderId());
        dto.setCreateTime(userCoupon.getCreateTime());
        
        if (coupon != null) {
            dto.setCouponName(coupon.getName());
            dto.setCouponType(coupon.getType());
            dto.setAmount(coupon.getAmount());
            dto.setMinAmount(coupon.getMinAmount());
            dto.setStartTime(coupon.getStartTime());
            dto.setEndTime(coupon.getEndTime());
            
            // 计算剩余天数
            if (coupon.getEndTime() != null) {
                LocalDateTime now = LocalDateTime.now();
                if (now.isBefore(coupon.getEndTime())) {
                    long days = java.time.Duration.between(now, coupon.getEndTime()).toDays();
                    dto.setRemainDays((int) days);
                    dto.setSoonToExpire(days <= 3); // 3天内即将过期
                } else {
                    dto.setRemainDays(0);
                    dto.setSoonToExpire(false);
                }
            }
        }
        
        return dto;
    }
} 