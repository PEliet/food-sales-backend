package com.foodsales.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.foodsales.model.CartItem;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CartItemMapper extends BaseMapper<CartItem> {
}