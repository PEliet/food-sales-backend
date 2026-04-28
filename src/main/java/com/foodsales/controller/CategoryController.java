package com.foodsales.controller;

import com.foodsales.model.Category;
import com.foodsales.service.CategoryService;
import com.foodsales.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired private CategoryService categoryService;

    @GetMapping("/list")
    public ResponseResult<List<Category>> list() {
        return ResponseResult.success(categoryService.getAllCategories());
    }

    @PostMapping("/add")
    public ResponseResult<Category> add(@RequestBody Category category) {
        return ResponseResult.success(categoryService.addCategory(category));
    }

    @PutMapping("/update")
    public ResponseResult<Category> update(@RequestBody Category category) {
        return ResponseResult.success(categoryService.updateCategory(category));
    }

    @DeleteMapping("/{id}")
    public ResponseResult<Void> delete(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return ResponseResult.success(null);
    }
}