package com.foodsales.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.foodsales.dto.PageDTO;
import com.foodsales.mapper.ProductMapper;
import com.foodsales.model.Product;
import com.foodsales.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired private ProductMapper productMapper;

    @Override
    public PageDTO<Product> getProductList(Integer categoryId, String keyword, Integer minPrice, Integer maxPrice, int page, int pageSize) {
        Page<Product> p = new Page<>(page, pageSize);
        LambdaQueryWrapper<Product> q = new LambdaQueryWrapper<>();
        if (categoryId != null && categoryId > 0) q.eq(Product::getCategoryId, categoryId);
        if (keyword != null && !keyword.isEmpty()) q.like(Product::getName, keyword);
        if (minPrice != null) q.ge(Product::getPrice, minPrice);
        if (maxPrice != null) q.le(Product::getPrice, maxPrice);
        q.eq(Product::getStatus, 1).orderByDesc(Product::getCreateTime);
        productMapper.selectPage(p, q);
        return PageDTO.of(p.getTotal(), page, pageSize, p.getRecords());
    }

    @Override public Product getProductById(Integer id) { return productMapper.selectById(id); }
    @Override public List<Product> getHotProducts(int limit) { return productMapper.selectHotProducts(limit); }
    @Override public List<Product> getNewProducts(int limit) { return productMapper.selectNewProducts(limit); }
    @Override public List<Product> getTopSales(int limit) { return productMapper.selectTopSales(limit); }

    @Override
    public Product addProduct(Product product) {
        product.setSales(0);
        product.setCreateTime(LocalDateTime.now());
        productMapper.insert(product);
        return product;
    }

    @Override
    public Product updateProduct(Product product) {
        product.setUpdateTime(LocalDateTime.now());
        productMapper.updateById(product);
        return productMapper.selectById(product.getProductId());
    }

    @Override public void deleteProduct(Integer id) { productMapper.deleteById(id); }

    @Override
    public void toggleStatus(Integer id) {
        Product p = productMapper.selectById(id);
        if (p != null) { p.setStatus(p.getStatus() == 1 ? 0 : 1); productMapper.updateById(p); }
    }
}