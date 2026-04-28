package com.foodsales.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.foodsales.model.Product;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {
    java.util.List<Product> selectHotProducts(int limit);
    java.util.List<Product> selectNewProducts(int limit);
    java.util.List<Product> selectTopSales(int limit);
}