package com.foodsales.controller;

import com.foodsales.dto.*;
import com.foodsales.model.Product;
import com.foodsales.service.*;
import com.foodsales.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired private ProductService productService;
    @Autowired private ReviewService reviewService;

    @GetMapping("/list")
    public ResponseResult<PageDTO<Product>> list(
            @RequestParam(required=false) Integer categoryId,
            @RequestParam(required=false) String keyword,
            @RequestParam(required=false) Integer minPrice,
            @RequestParam(required=false) Integer maxPrice,
            @RequestParam(defaultValue="1") int page,
            @RequestParam(defaultValue="20") int pageSize) {
        return ResponseResult.success(productService.getProductList(categoryId, keyword, minPrice, maxPrice, page, pageSize));
    }

    @GetMapping("/detail")
    public ResponseResult<Map<String,Object>> detail(@RequestParam Integer id) {
        Map<String,Object> result = new HashMap<>();
        result.put("product", productService.getProductById(id));
        result.put("reviews", reviewService.getProductReviews(id));
        return ResponseResult.success(result);
    }

    @GetMapping("/hot")
    public ResponseResult<List<Product>> hot(@RequestParam(defaultValue="5") int limit) {
        return ResponseResult.success(productService.getHotProducts(limit));
    }

    @GetMapping("/new")
    public ResponseResult<List<Product>> newProducts(@RequestParam(defaultValue="5") int limit) {
        return ResponseResult.success(productService.getNewProducts(limit));
    }

    @GetMapping("/top")
    public ResponseResult<List<Product>> top(@RequestParam(defaultValue="5") int limit) {
        return ResponseResult.success(productService.getTopSales(limit));
    }

    @PostMapping("/add")
    public ResponseResult<Product> add(@RequestBody Product product) {
        return ResponseResult.success(productService.addProduct(product));
    }

    @PutMapping("/update")
    public ResponseResult<Product> update(@RequestBody Product product) {
        return ResponseResult.success(productService.updateProduct(product));
    }

    @DeleteMapping("/{id}")
    public ResponseResult<Void> delete(@PathVariable Integer id) {
        productService.deleteProduct(id);
        return ResponseResult.success(null);
    }

    @PutMapping("/toggleStatus")
    public ResponseResult<Void> toggleStatus(@RequestParam Integer id) {
        productService.toggleStatus(id);
        return ResponseResult.success(null);
    }
}