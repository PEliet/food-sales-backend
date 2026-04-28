package com.foodsales.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.foodsales.dto.PageDTO;
import com.foodsales.mapper.ReviewMapper;
import com.foodsales.model.Review;
import com.foodsales.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired private ReviewMapper reviewMapper;

    @Override
    public void addReview(Integer userId, Integer orderId, Integer productId, Integer rating, String content, String images) {
        Review r = new Review();
        r.setUserId(userId);
        r.setOrderId(orderId);
        r.setProductId(productId);
        r.setRating(rating);
        r.setContent(content);
        r.setImages(images);
        r.setStatus(1);
        r.setCreateTime(LocalDateTime.now());
        reviewMapper.insert(r);
    }

    @Override
    public List<Review> getProductReviews(Integer productId) {
        return reviewMapper.selectList(new LambdaQueryWrapper<Review>()
                .eq(Review::getProductId, productId).eq(Review::getStatus, 1).orderByDesc(Review::getCreateTime));
    }

    @Override
    public PageDTO<Review> getAllReviews(int page, int pageSize) {
        Page<Review> p = new Page<>(page, pageSize);
        reviewMapper.selectPage(p, new LambdaQueryWrapper<Review>().orderByDesc(Review::getCreateTime));
        return PageDTO.of(p.getTotal(), page, pageSize, p.getRecords());
    }

    @Override
    public void toggleReviewStatus(Integer reviewId, Integer status) {
        Review r = reviewMapper.selectById(reviewId);
        if (r != null) { r.setStatus(status); reviewMapper.updateById(r); }
    }
}