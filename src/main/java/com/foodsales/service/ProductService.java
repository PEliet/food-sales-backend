package com.foodsales.service;

import com.foodsales.dto.*;
import com.foodsales.model.*;
import com.foodsales.dto.PageDTO;
import java.util.List;
import java.util.Map;

public interface ProductService {
    PageDTO<Product> getProductList(Integer categoryId, String keyword, Integer minPrice, Integer maxPrice, int page, int pageSize);
    Product getProductById(Integer id);
    java.util.List<Product> getHotProducts(int limit);
    java.util.List<Product> getNewProducts(int limit);
    java.util.List<Product> getTopSales(int limit);
    Product addProduct(Product product);
    Product updateProduct(Product product);
    void deleteProduct(Integer id);
    void toggleStatus(Integer id);
}