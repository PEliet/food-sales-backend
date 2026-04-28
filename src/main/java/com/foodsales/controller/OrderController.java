package com.foodsales.controller;

import com.foodsales.dto.*;
import com.foodsales.model.Order;
import com.foodsales.model.OrderItem;
import com.foodsales.mapper.OrderItemMapper;
import com.foodsales.service.*;
import com.foodsales.util.ResponseResult;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import java.util.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired private OrderService orderService;
    @Autowired private OrderItemMapper orderItemMapper;

    @PostMapping("/create")
    public ResponseResult<Order> create(@RequestBody OrderRequest req, HttpServletRequest request) {
        try {
            Order order = orderService.createOrder((Integer)request.getAttribute("userId"), req);
            return ResponseResult.success(order);
        } catch (RuntimeException e) {
            return ResponseResult.error(e.getMessage());
        }
    }

    @GetMapping("/list")
    public ResponseResult<List<Order>> list(@RequestParam(required=false) String status,
            HttpServletRequest request) {
        return ResponseResult.success(orderService.getUserOrders((Integer)request.getAttribute("userId"), status));
    }

    @GetMapping("/detail")
    public ResponseResult<Map<String,Object>> detail(@RequestParam Integer orderId,
            HttpServletRequest request) {
        Order order = orderService.getOrderDetail(orderId, (Integer)request.getAttribute("userId"));
        if (order == null) return ResponseResult.error("订单不存在");
        List<OrderItem> items = orderItemMapper.selectList(
                new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, orderId));
        Map<String,Object> result = new HashMap<>();
        result.put("order", order);
        result.put("items", items);
        return ResponseResult.success(result);
    }

    @PostMapping("/cancel")
    public ResponseResult<Void> cancel(@RequestParam Integer orderId,
            @RequestParam(required=false) String reason, HttpServletRequest request) {
        orderService.cancelOrder(orderId, (Integer)request.getAttribute("userId"), reason);
        return ResponseResult.success(null);
    }

    @PostMapping("/pay")
    public ResponseResult<Void> mockPay(@RequestParam Integer orderId, HttpServletRequest request) {
        orderService.updateOrderStatus(orderId, "paid");
        return ResponseResult.success(null);
    }

    @PostMapping("/confirm")
    public ResponseResult<Void> confirmReceive(@RequestParam Integer orderId, HttpServletRequest request) {
        orderService.confirmReceive(orderId, (Integer)request.getAttribute("userId"));
        return ResponseResult.success(null);
    }

    // Admin endpoints
    @GetMapping("/admin/list")
    public ResponseResult<PageDTO<Order>> adminList(
            @RequestParam(required=false) String orderNo,
            @RequestParam(required=false) String status,
            @RequestParam(required=false) Integer userId,
            @RequestParam(defaultValue="1") int page,
            @RequestParam(defaultValue="20") int pageSize) {
        return ResponseResult.success(orderService.getAllOrders(orderNo, status, userId, page, pageSize));
    }

    @PutMapping("/admin/status")
    public ResponseResult<Void> updateStatus(@RequestParam Integer orderId, @RequestParam String status) {
        orderService.updateOrderStatus(orderId, status);
        return ResponseResult.success(null);
    }

    @PostMapping("/admin/ship")
    public ResponseResult<Void> ship(@RequestBody OrderShipRequest req) {
        orderService.shipOrder(req.getOrderId(), req.getCompany(), req.getTrackingNo());
        return ResponseResult.success(null);
    }

    @PostMapping("/admin/refund")
    public ResponseResult<Void> refund(@RequestParam Integer orderId, @RequestParam boolean approve) {
        orderService.processRefund(orderId, approve);
        return ResponseResult.success(null);
    }
}