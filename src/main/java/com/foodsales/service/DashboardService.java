package com.foodsales.service;

import com.foodsales.dto.*;
import com.foodsales.model.*;
import com.foodsales.dto.PageDTO;
import java.util.List;
import java.util.Map;

public interface DashboardService {
    java.util.Map<String, Object> getDashboardStats();
    java.util.List<java.util.Map<String, Object>> getSalesTrend(String period);
    java.util.List<java.util.Map<String, Object>> getTopProducts(int limit);
    java.util.Map<String, Object> getOrderStatusDistribution();
    java.util.Map<String, Object> getSalesSummary(String startDate, String endDate);
}