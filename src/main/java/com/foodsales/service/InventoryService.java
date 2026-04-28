package com.foodsales.service;

import com.foodsales.dto.*;
import com.foodsales.model.*;
import com.foodsales.dto.PageDTO;
import java.util.List;
import java.util.Map;

public interface InventoryService {
    Inventory getInventoryByProductId(Integer productId);
    java.util.List<Inventory> getAllInventories();
    void updateInventory(Integer productId, Integer quantity);
    void setWarning(Integer productId, Integer warning);
    java.util.List<Inventory> getWarningList();
    void adjustStock(Integer productId, Integer delta);
}