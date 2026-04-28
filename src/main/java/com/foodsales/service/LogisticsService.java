package com.foodsales.service;

import com.foodsales.dto.*;
import com.foodsales.model.*;
import com.foodsales.dto.PageDTO;
import java.util.List;
import java.util.Map;

public interface LogisticsService {
    Logistics getLogisticsByOrderId(Integer orderId);
    java.util.List<LogisticsTrack> getTracks(Integer logisticsId);
    Logistics createLogistics(Integer orderId, String company, String trackingNo);
    void updateTrack(Integer logisticsId, String operation, String operator, String location);
}