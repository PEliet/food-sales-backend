package com.foodsales.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.foodsales.mapper.CategoryMapper;
import com.foodsales.model.Category;
import com.foodsales.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired private CategoryMapper categoryMapper;

    @Override
    public List<Category> getAllCategories() {
        return categoryMapper.selectList(new LambdaQueryWrapper<Category>().eq(Category::getStatus, 1).orderByAsc(Category::getSort));
    }

    @Override
    public List<Category> getParentCategories() {
        return categoryMapper.selectList(new LambdaQueryWrapper<Category>().eq(Category::getParentId, 0).eq(Category::getStatus, 1));
    }

    @Override
    public Category addCategory(Category category) {
        category.setCreateTime(LocalDateTime.now());
        categoryMapper.insert(category);
        return category;
    }

    @Override
    public Category updateCategory(Category category) {
        category.setUpdateTime(LocalDateTime.now());
        categoryMapper.updateById(category);
        return category;
    }

    @Override
    public void deleteCategory(Integer id) { categoryMapper.deleteById(id); }
}