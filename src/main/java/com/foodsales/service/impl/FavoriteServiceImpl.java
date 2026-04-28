package com.foodsales.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.foodsales.mapper.FavoriteMapper;
import com.foodsales.mapper.ProductMapper;
import com.foodsales.model.Favorite;
import com.foodsales.model.Product;
import com.foodsales.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired private FavoriteMapper favoriteMapper;
    @Autowired private ProductMapper productMapper;

    @Override
    public List<Product> getUserFavorites(Integer userId) {
        List<Favorite> favs = favoriteMapper.selectList(new LambdaQueryWrapper<Favorite>()
                .eq(Favorite::getUserId, userId).orderByDesc(Favorite::getCreateTime));
        return favs.stream().map(f -> productMapper.selectById(f.getProductId())).collect(Collectors.toList());
    }

    @Override
    public void addFavorite(Integer userId, Integer productId) {
        Favorite f = new Favorite();
        f.setUserId(userId);
        f.setProductId(productId);
        f.setCreateTime(LocalDateTime.now());
        favoriteMapper.insert(f);
    }

    @Override
    public void removeFavorite(Integer userId, Integer productId) {
        favoriteMapper.delete(new LambdaQueryWrapper<Favorite>()
                .eq(Favorite::getUserId, userId).eq(Favorite::getProductId, productId));
    }

    @Override
    public boolean isFavorited(Integer userId, Integer productId) {
        return favoriteMapper.selectCount(new LambdaQueryWrapper<Favorite>()
                .eq(Favorite::getUserId, userId).eq(Favorite::getProductId, productId)) > 0;
    }
}