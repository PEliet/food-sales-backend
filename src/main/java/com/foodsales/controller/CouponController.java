package com.foodsales.controller;

import com.foodsales.dto.PageDTO;
import com.foodsales.model.Coupon;
import com.foodsales.model.UserCoupon;
import com.foodsales.service.CouponService;
import com.foodsales.util.ResponseResult;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/coupon")
public class CouponController {

    @Autowired private CouponService couponService;

    @GetMapping("/available")
    public ResponseResult<List<Coupon>> available() {
        return ResponseResult.success(couponService.getAvailableCoupons());
    }

    @GetMapping("/my")
    public ResponseResult<List<UserCoupon>> myCoupons(HttpServletRequest request) {
        return ResponseResult.success(couponService.getUserCoupons((Integer)request.getAttribute("userId")));
    }

    @PostMapping("/claim")
    public ResponseResult<Void> claim(@RequestParam Integer couponId, HttpServletRequest request) {
        try {
            couponService.claimCoupon((Integer)request.getAttribute("userId"), couponId);
            return ResponseResult.success(null);
        } catch (RuntimeException e) {
            return ResponseResult.error(e.getMessage());
        }
    }

    @GetMapping("/list")
    public ResponseResult<PageDTO<Coupon>> list(
            @RequestParam(defaultValue="1") int page,
            @RequestParam(defaultValue="20") int pageSize) {
        return ResponseResult.success(couponService.getAllCoupons(page, pageSize));
    }

    @PostMapping("/add")
    public ResponseResult<Coupon> add(@RequestBody Coupon coupon) {
        return ResponseResult.success(couponService.addCoupon(coupon));
    }

    @PutMapping("/update")
    public ResponseResult<Coupon> update(@RequestBody Coupon coupon) {
        return ResponseResult.success(couponService.updateCoupon(coupon));
    }
}