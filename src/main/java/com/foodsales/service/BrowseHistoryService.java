package com.foodsales.service;

import com.foodsales.dto.*;
import com.foodsales.model.*;
import com.foodsales.dto.PageDTO;
import java.util.List;
import java.util.Map;

public interface BrowseHistoryService {
    java.util.List<Product> getUserHistory(Integer userId);
    void addHistory(Integer userId, Integer productId);
    void clearHistory(Integer userId);
}