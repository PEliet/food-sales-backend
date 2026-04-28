package com.foodsales.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.foodsales.model.Inventory;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InventoryMapper extends BaseMapper<Inventory> {
}