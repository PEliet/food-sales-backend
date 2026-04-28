package com.foodsales.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.foodsales.mapper.LogisticsMapper;
import com.foodsales.mapper.LogisticsTrackMapper;
import com.foodsales.model.Logistics;
import com.foodsales.model.LogisticsTrack;
import com.foodsales.service.LogisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LogisticsServiceImpl implements LogisticsService {

    @Autowired private LogisticsMapper logisticsMapper;
    @Autowired private LogisticsTrackMapper trackMapper;

    @Override
    public Logistics getLogisticsByOrderId(Integer orderId) {
        return logisticsMapper.selectOne(new LambdaQueryWrapper<Logistics>().eq(Logistics::getOrderId, orderId));
    }

    @Override
    public List<LogisticsTrack> getTracks(Integer logisticsId) {
        return trackMapper.selectList(new LambdaQueryWrapper<LogisticsTrack>().eq(LogisticsTrack::getLogisticsId, logisticsId).orderByAsc(LogisticsTrack::getCreateTime));
    }

    @Override
    public Logistics createLogistics(Integer orderId, String company, String trackingNo) {
        Logistics l = new Logistics();
        l.setOrderId(orderId);
        l.setCompany(company);
        l.setTrackingNo(trackingNo);
        l.setStatus("pending");
        l.setCreateTime(LocalDateTime.now());
        logisticsMapper.insert(l);
        return l;
    }

    @Override
    public void updateTrack(Integer logisticsId, String operation, String operator, String location) {
        LogisticsTrack t = new LogisticsTrack();
        t.setLogisticsId(logisticsId);
        t.setOperation(operation);
        t.setOperator(operator);
        t.setLocation(location);
        t.setCreateTime(LocalDateTime.now());
        trackMapper.insert(t);
    }
}