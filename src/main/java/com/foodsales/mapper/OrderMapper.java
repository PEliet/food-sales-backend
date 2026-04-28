package com.foodsales.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.foodsales.model.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
    java.util.Map<String, Object> selectOrderStats();
    java.util.List<java.util.Map<String, Object>> selectSalesTrend(java.time.LocalDateTime startTime);
}