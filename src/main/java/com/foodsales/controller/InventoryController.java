package com.foodsales.controller;

import com.foodsales.model.Inventory;
import com.foodsales.service.InventoryService;
import com.foodsales.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired private InventoryService inventoryService;

    @GetMapping("/list")
    public ResponseResult<List<Inventory>> list() {
        return ResponseResult.success(inventoryService.getAllInventories());
    }

    @GetMapping("/warning")
    public ResponseResult<List<Inventory>> warningList() {
        return ResponseResult.success(inventoryService.getWarningList());
    }

    @PutMapping("/update")
    public ResponseResult<Void> update(@RequestParam Integer productId, @RequestParam Integer quantity) {
        inventoryService.updateInventory(productId, quantity);
        return ResponseResult.success(null);
    }

    @PutMapping("/warning")
    public ResponseResult<Void> setWarning(@RequestParam Integer productId, @RequestParam Integer warning) {
        inventoryService.setWarning(productId, warning);
        return ResponseResult.success(null);
    }

    @PostMapping("/adjust")
    public ResponseResult<Void> adjust(@RequestParam Integer productId, @RequestParam Integer delta) {
        inventoryService.adjustStock(productId, delta);
        return ResponseResult.success(null);
    }
}