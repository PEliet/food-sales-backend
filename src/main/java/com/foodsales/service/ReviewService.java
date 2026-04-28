package com.foodsales.service;

import com.foodsales.dto.*;
import com.foodsales.model.*;
import com.foodsales.dto.PageDTO;
import java.util.List;
import java.util.Map;

public interface ReviewService {
    void addReview(Integer userId, Integer orderId, Integer productId, Integer rating, String content, String images);
    java.util.List<Review> getProductReviews(Integer productId);
    PageDTO<Review> getAllReviews(int page, int pageSize);
    void toggleReviewStatus(Integer reviewId, Integer status);
}