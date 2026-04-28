package com.foodsales.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.foodsales.dto.PageDTO;
import com.foodsales.mapper.CouponMapper;
import com.foodsales.mapper.UserCouponMapper;
import com.foodsales.model.Coupon;
import com.foodsales.model.UserCoupon;
import com.foodsales.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CouponServiceImpl implements CouponService {

    @Autowired private CouponMapper couponMapper;
    @Autowired private UserCouponMapper userCouponMapper;

    @Override
    public List<Coupon> getAvailableCoupons() {
        return couponMapper.selectList(new LambdaQueryWrapper<Coupon>()
                .eq(Coupon::getStatus, 1).gt(Coupon::getEndTime, LocalDateTime.now())
                .apply("total_count > used_count"));
    }

    @Override
    public List<UserCoupon> getUserCoupons(Integer userId) {
        return userCouponMapper.selectList(new LambdaQueryWrapper<UserCoupon>().eq(UserCoupon::getUserId, userId));
    }

    @Override
    public void claimCoupon(Integer userId, Integer couponId) {
        Coupon c = couponMapper.selectById(couponId);
        if (c == null || c.getStatus() != 1) throw new RuntimeException("优惠券不可领取");
        UserCoupon uc = new UserCoupon();
        uc.setUserId(userId);
        uc.setCouponId(couponId);
        uc.setStatus(0);
        uc.setCreateTime(LocalDateTime.now());
        userCouponMapper.insert(uc);
        c.setUsedCount(c.getUsedCount() + 1);
        couponMapper.updateById(c);
    }

    @Override
    public PageDTO<Coupon> getAllCoupons(int page, int pageSize) {
        Page<Coupon> p = new Page<>(page, pageSize);
        couponMapper.selectPage(p, new LambdaQueryWrapper<Coupon>().orderByDesc(Coupon::getCreateTime));
        return PageDTO.of(p.getTotal(), page, pageSize, p.getRecords());
    }

    @Override
    public Coupon addCoupon(Coupon coupon) {
        coupon.setCreateTime(LocalDateTime.now());
        couponMapper.insert(coupon);
        return coupon;
    }

    @Override
    public Coupon updateCoupon(Coupon coupon) {
        couponMapper.updateById(coupon);
        return coupon;
    }
}