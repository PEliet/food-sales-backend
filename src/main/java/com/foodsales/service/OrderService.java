package com.foodsales.service;

import com.foodsales.dto.*;
import com.foodsales.model.*;
import com.foodsales.dto.PageDTO;
import java.util.List;
import java.util.Map;

public interface OrderService {
    Order createOrder(Integer userId, OrderRequest req);
    java.util.List<Order> getUserOrders(Integer userId, String status);
    Order getOrderDetail(Integer orderId, Integer userId);
    void cancelOrder(Integer orderId, Integer userId, String reason);
    void confirmReceive(Integer orderId, Integer userId);
    PageDTO<Order> getAllOrders(String orderNo, String status, Integer userId, int page, int pageSize);
    void updateOrderStatus(Integer orderId, String status);
    void shipOrder(Integer orderId, String company, String trackingNo);
    void processRefund(Integer orderId, boolean approve);
}