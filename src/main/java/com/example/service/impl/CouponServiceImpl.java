package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.service.CouponService;
import com.example.model.entity.Coupon;
import com.example.mapper.CouponMapper;
import org.springframework.stereotype.Service;

/**
* @author 31815
* @description 针对表【coupon(优惠券表)】的数据库操作Service实现
* @createDate 2025-02-18 23:44:26
*/
@Service
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon>
    implements CouponService {

}




