package com.foodsales.service;

import com.foodsales.dto.*;
import com.foodsales.model.*;
import com.foodsales.dto.PageDTO;
import java.util.List;
import java.util.Map;

public interface CartService {
    Cart getCartByUserId(Integer userId);
    java.util.List<java.util.Map<String, Object>> getCartItems(Integer userId);
    void addItem(Integer userId, Integer productId, Integer quantity);
    void updateItemQuantity(Integer userId, Integer cartItemId, Integer quantity);
    void deleteItem(Integer userId, Integer cartItemId);
    void toggleCheck(Integer userId, Integer cartItemId, Integer checked);
    void toggleAllCheck(Integer userId, Integer checked);
    void clearCart(Integer userId);
}