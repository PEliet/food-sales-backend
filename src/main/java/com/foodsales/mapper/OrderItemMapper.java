package com.foodsales.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.foodsales.model.OrderItem;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItem> {
}