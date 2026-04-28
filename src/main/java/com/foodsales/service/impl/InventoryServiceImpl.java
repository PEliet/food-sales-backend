package com.foodsales.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.foodsales.mapper.InventoryMapper;
import com.foodsales.mapper.ProductMapper;
import com.foodsales.model.Inventory;
import com.foodsales.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired private InventoryMapper inventoryMapper;
    @Autowired private ProductMapper productMapper;

    @Override
    public Inventory getInventoryByProductId(Integer productId) {
        return inventoryMapper.selectOne(new LambdaQueryWrapper<Inventory>().eq(Inventory::getProductId, productId));
    }

    @Override
    public List<Inventory> getAllInventories() {
        return inventoryMapper.selectList(null);
    }

    @Override
    public void updateInventory(Integer productId, Integer quantity) {
        Inventory inv = inventoryMapper.selectOne(new LambdaQueryWrapper<Inventory>().eq(Inventory::getProductId, productId));
        if (inv != null) { inv.setQuantity(quantity); inventoryMapper.updateById(inv); }
    }

    @Override
    public void setWarning(Integer productId, Integer warning) {
        Inventory inv = inventoryMapper.selectOne(new LambdaQueryWrapper<Inventory>().eq(Inventory::getProductId, productId));
        if (inv != null) { inv.setWarning(warning); inventoryMapper.updateById(inv); }
    }

    @Override
    public List<Inventory> getWarningList() {
        return inventoryMapper.selectList(new LambdaQueryWrapper<Inventory>()
                .apply("quantity <= warning"));
    }

    @Override
    public void adjustStock(Integer productId, Integer delta) {
        Inventory inv = inventoryMapper.selectOne(new LambdaQueryWrapper<Inventory>().eq(Inventory::getProductId, productId));
        if (inv != null) { inv.setQuantity(inv.getQuantity() + delta); inventoryMapper.updateById(inv); }
    }
}