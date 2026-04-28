package com.foodsales.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.foodsales.dto.OrderRequest;
import com.foodsales.dto.PageDTO;
import com.foodsales.mapper.*;
import com.foodsales.model.*;
import com.foodsales.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired private OrderMapper orderMapper;
    @Autowired private OrderItemMapper orderItemMapper;
    @Autowired private CartMapper cartMapper;
    @Autowired private CartItemMapper cartItemMapper;
    @Autowired private ProductMapper productMapper;
    @Autowired private AddressMapper addressMapper;

    @Override @Transactional
    public Order createOrder(Integer userId, OrderRequest req) {
        if (req.getAddressId() == null) {
            // 开发模式：自动取用户第一个地址
            Address addr = addressMapper.selectOne(
                    new LambdaQueryWrapper<Address>().eq(Address::getUserId, userId).last("limit 1"));
            if (addr != null) req.setAddressId(addr.getAddressId());
        }
        Address addr = addressMapper.selectById(req.getAddressId());
        if (addr == null) throw new RuntimeException("请先添加收货地址");

        Cart cart = cartMapper.selectOne(new LambdaQueryWrapper<Cart>().eq(Cart::getUserId, userId));
        List<CartItem> selectedItems;
        if (req.getCartItemIds() != null && !req.getCartItemIds().isEmpty()) {
            selectedItems = cartItemMapper.selectList(new LambdaQueryWrapper<CartItem>()
                    .in(CartItem::getCartItemId, req.getCartItemIds()).eq(CartItem::getCartId, cart.getCartId()));
        } else {
            selectedItems = cartItemMapper.selectList(new LambdaQueryWrapper<CartItem>()
                    .eq(CartItem::getCartId, cart.getCartId()).eq(CartItem::getChecked, 1));
        }
        if (selectedItems.isEmpty()) throw new RuntimeException("没有选中商品");

        BigDecimal total = BigDecimal.ZERO;
        Order order = new Order();
        order.setOrderNo("ORD" + System.currentTimeMillis());
        order.setUserId(userId);
        order.setStatus("pending");
        order.setTotalAmount(BigDecimal.ZERO);
        order.setPayAmount(BigDecimal.ZERO);
        order.setAddressId(req.getAddressId());
        order.setReceiverName(addr.getReceiver());
        order.setReceiverPhone(addr.getPhone());
        order.setReceiverAddress(addr.getProvince() + addr.getCity() + addr.getDistrict() + addr.getDetail());
        order.setRemark(req.getRemark());
        order.setCreateTime(LocalDateTime.now());
        orderMapper.insert(order);

        for (CartItem ci : selectedItems) {
            Product p = productMapper.selectById(ci.getProductId());
            if (p == null || p.getStatus() != 1) continue;
            BigDecimal sub = p.getPrice().multiply(BigDecimal.valueOf(ci.getQuantity()));
            total = total.add(sub);
            OrderItem oi = new OrderItem();
            oi.setOrderId(order.getOrderId());
            oi.setProductId(p.getProductId());
            oi.setProductName(p.getName());
            oi.setProductImage(p.getImage());
            oi.setPrice(p.getPrice());
            oi.setQuantity(ci.getQuantity());
            oi.setSubtotal(sub);
            orderItemMapper.insert(oi);
            cartItemMapper.deleteById(ci.getCartItemId());
        }

        order.setTotalAmount(total);
        order.setPayAmount(total);
        orderMapper.updateById(order);
        return order;
    }

    @Override
    public List<Order> getUserOrders(Integer userId, String status) {
        LambdaQueryWrapper<Order> q = new LambdaQueryWrapper<Order>().eq(Order::getUserId, userId);
        if (status != null && !status.isEmpty() && !"all".equals(status)) q.eq(Order::getStatus, status);
        q.orderByDesc(Order::getCreateTime);
        return orderMapper.selectList(q);
    }

    @Override
    public Order getOrderDetail(Integer orderId, Integer userId) {
        return orderMapper.selectOne(new LambdaQueryWrapper<Order>().eq(Order::getOrderId, orderId).eq(Order::getUserId, userId));
    }

    @Override
    public void cancelOrder(Integer orderId, Integer userId, String reason) {
        Order o = orderMapper.selectOne(new LambdaQueryWrapper<Order>().eq(Order::getOrderId, orderId).eq(Order::getUserId, userId));
        if (o != null && "pending".equals(o.getStatus())) {
            o.setStatus("cancelled");
            o.setCancelReason(reason);
            o.setCloseTime(LocalDateTime.now());
            orderMapper.updateById(o);
        }
    }

    @Override
    public void confirmReceive(Integer orderId, Integer userId) {
        Order o = orderMapper.selectOne(new LambdaQueryWrapper<Order>().eq(Order::getOrderId, orderId).eq(Order::getUserId, userId));
        if (o != null && "shipped".equals(o.getStatus())) {
            o.setStatus("received");
            orderMapper.updateById(o);
        }
    }

    @Override
    public PageDTO<Order> getAllOrders(String orderNo, String status, Integer userId, int page, int pageSize) {
        Page<Order> p = new Page<>(page, pageSize);
        LambdaQueryWrapper<Order> q = new LambdaQueryWrapper<>();
        if (orderNo != null && !orderNo.isEmpty()) q.like(Order::getOrderNo, orderNo);
        if (status != null && !status.isEmpty()) q.eq(Order::getStatus, status);
        if (userId != null) q.eq(Order::getUserId, userId);
        q.orderByDesc(Order::getCreateTime);
        orderMapper.selectPage(p, q);
        return PageDTO.of(p.getTotal(), page, pageSize, p.getRecords());
    }

    @Override
    public void updateOrderStatus(Integer orderId, String status) {
        Order o = orderMapper.selectById(orderId);
        if (o != null) { o.setStatus(status); orderMapper.updateById(o); }
    }

    @Override
    public void shipOrder(Integer orderId, String company, String trackingNo) {
        Order o = orderMapper.selectById(orderId);
        if (o != null && "paid".equals(o.getStatus())) {
            o.setStatus("shipped");
            orderMapper.updateById(o);
        }
    }

    @Override
    public void processRefund(Integer orderId, boolean approve) {
        Order o = orderMapper.selectById(orderId);
        if (o != null) {
            o.setStatus(approve ? "refunded" : "paid");
            orderMapper.updateById(o);
        }
    }
}