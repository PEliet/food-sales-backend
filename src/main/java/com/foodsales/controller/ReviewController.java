package com.foodsales.controller;

import com.foodsales.dto.PageDTO;
import com.foodsales.model.Review;
import com.foodsales.service.ReviewService;
import com.foodsales.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/review")
public class ReviewController {

    @Autowired private ReviewService reviewService;

    @GetMapping("/product")
    public ResponseResult<java.util.List<Review>> productReviews(@RequestParam Integer productId) {
        return ResponseResult.success(reviewService.getProductReviews(productId));
    }

    @GetMapping("/list")
    public ResponseResult<PageDTO<Review>> list(
            @RequestParam(defaultValue="1") int page,
            @RequestParam(defaultValue="20") int pageSize) {
        return ResponseResult.success(reviewService.getAllReviews(page, pageSize));
    }

    @PutMapping("/status")
    public ResponseResult<Void> toggleStatus(@RequestParam Integer reviewId, @RequestParam Integer status) {
        reviewService.toggleReviewStatus(reviewId, status);
        return ResponseResult.success(null);
    }
}