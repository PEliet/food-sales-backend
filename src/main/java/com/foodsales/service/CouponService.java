package com.foodsales.service;

import com.foodsales.dto.*;
import com.foodsales.model.*;
import com.foodsales.dto.PageDTO;
import java.util.List;
import java.util.Map;

public interface CouponService {
    java.util.List<Coupon> getAvailableCoupons();
    java.util.List<UserCoupon> getUserCoupons(Integer userId);
    void claimCoupon(Integer userId, Integer couponId);
    PageDTO<Coupon> getAllCoupons(int page, int pageSize);
    Coupon addCoupon(Coupon coupon);
    Coupon updateCoupon(Coupon coupon);
}