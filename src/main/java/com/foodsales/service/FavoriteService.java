package com.foodsales.service;

import com.foodsales.dto.*;
import com.foodsales.model.*;
import com.foodsales.dto.PageDTO;
import java.util.List;
import java.util.Map;

public interface FavoriteService {
    java.util.List<Product> getUserFavorites(Integer userId);
    void addFavorite(Integer userId, Integer productId);
    void removeFavorite(Integer userId, Integer productId);
    boolean isFavorited(Integer userId, Integer productId);
}