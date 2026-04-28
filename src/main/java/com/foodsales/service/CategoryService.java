package com.foodsales.service;

import com.foodsales.dto.*;
import com.foodsales.model.*;
import com.foodsales.dto.PageDTO;
import java.util.List;
import java.util.Map;

public interface CategoryService {
    java.util.List<Category> getAllCategories();
    java.util.List<Category> getParentCategories();
    Category addCategory(Category category);
    Category updateCategory(Category category);
    void deleteCategory(Integer id);
}