package com.foodsales.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.foodsales.mapper.BrowseHistoryMapper;
import com.foodsales.mapper.ProductMapper;
import com.foodsales.model.BrowseHistory;
import com.foodsales.model.Product;
import com.foodsales.service.BrowseHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BrowseHistoryServiceImpl implements BrowseHistoryService {

    @Autowired private BrowseHistoryMapper historyMapper;
    @Autowired private ProductMapper productMapper;

    @Override
    public List<Product> getUserHistory(Integer userId) {
        List<BrowseHistory> list = historyMapper.selectList(new LambdaQueryWrapper<BrowseHistory>()
                .eq(BrowseHistory::getUserId, userId).orderByDesc(BrowseHistory::getCreateTime));
        return list.stream().map(h -> productMapper.selectById(h.getProductId())).collect(Collectors.toList());
    }

    @Override
    public void addHistory(Integer userId, Integer productId) {
        BrowseHistory h = new BrowseHistory();
        h.setUserId(userId);
        h.setProductId(productId);
        h.setCreateTime(LocalDateTime.now());
        historyMapper.insert(h);
    }

    @Override
    public void clearHistory(Integer userId) {
        historyMapper.delete(new LambdaQueryWrapper<BrowseHistory>().eq(BrowseHistory::getUserId, userId));
    }
}