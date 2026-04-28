package com.foodsales.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.foodsales.mapper.*;
import com.foodsales.model.Order;
import com.foodsales.model.Product;
import com.foodsales.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired private OrderMapper orderMapper;
    @Autowired private OrderItemMapper orderItemMapper;
    @Autowired private ProductMapper productMapper;
    @Autowired private UserMapper userMapper;
    @Autowired private InventoryMapper inventoryMapper;

    @Override
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.plusDays(1).atStartOfDay();

        List<Order> todayOrders = orderMapper.selectList(new LambdaQueryWrapper<Order>()
                .between(Order::getCreateTime, start, end));
        long orderCount = todayOrders.size();
        BigDecimal revenue = todayOrders.stream()
                .filter(o -> !"pending".equals(o.getStatus()) && !"cancelled".equals(o.getStatus()))
                .map(Order::getPayAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        long pendingShip = orderMapper.selectCount(new LambdaQueryWrapper<Order>().eq(Order::getStatus, "paid"));
        long warningCount = inventoryMapper.selectCount(null);
        long newUsers = userMapper.selectCount(new LambdaQueryWrapper<com.foodsales.model.User>()
                .between(com.foodsales.model.User::getCreateTime, start, end));

        stats.put("todayOrderCount", orderCount);
        stats.put("todayRevenue", revenue);
        stats.put("pendingShip", pendingShip);
        stats.put("warningCount", warningCount);
        stats.put("newUsers", newUsers);
        return stats;
    }

    @Override
    public List<Map<String, Object>> getSalesTrend(String period) {
        List<Map<String, Object>> result = new ArrayList<>();
        LocalDate start = "week".equals(period) ? LocalDate.now().minusDays(7) : LocalDate.now().minusDays(30);
        List<Order> orders = orderMapper.selectList(new LambdaQueryWrapper<Order>()
                .between(Order::getCreateTime, start.atStartOfDay(), LocalDate.now().plusDays(1).atStartOfDay())
                .notIn(Order::getStatus, "pending", "cancelled"));
        Map<LocalDate, List<Order>> grouped = orders.stream()
                .collect(Collectors.groupingBy(o -> o.getCreateTime().toLocalDate()));
        for (LocalDate d = start; !d.isAfter(LocalDate.now()); d = d.plusDays(1)) {
            Map<String, Object> item = new HashMap<>();
            item.put("date", d.toString());
            List<Order> dayOrders = grouped.getOrDefault(d, Collections.emptyList());
            item.put("count", dayOrders.size());
            item.put("revenue", dayOrders.stream().map(Order::getPayAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
            result.add(item);
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> getTopProducts(int limit) {
        List<Product> products = productMapper.selectList(
                new LambdaQueryWrapper<Product>().orderByDesc(Product::getSales).last("LIMIT " + limit));
        return products.stream().map(p -> {
            Map<String, Object> m = new HashMap<>();
            m.put("name", p.getName()); m.put("sales", p.getSales()); m.put("revenue", p.getPrice().multiply(BigDecimal.valueOf(p.getSales())));
            return m;
        }).collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> getOrderStatusDistribution() {
        Map<String, Object> dist = new HashMap<>();
        for (String s : Arrays.asList("pending", "paid", "shipped", "received", "cancelled")) {
            dist.put(s, orderMapper.selectCount(new LambdaQueryWrapper<Order>().eq(Order::getStatus, s)));
        }
        return dist;
    }

    @Override
    public Map<String, Object> getSalesSummary(String startDate, String endDate) {
        LocalDateTime start = LocalDate.parse(startDate).atStartOfDay();
        LocalDateTime end = LocalDate.parse(endDate).plusDays(1).atStartOfDay();
        List<Order> orders = orderMapper.selectList(new LambdaQueryWrapper<Order>()
                .between(Order::getCreateTime, start, end).notIn(Order::getStatus, "pending", "cancelled"));
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalOrders", orders.size());
        summary.put("totalRevenue", orders.stream().map(Order::getPayAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
        summary.put("avgOrderValue", orders.isEmpty() ? BigDecimal.ZERO :
                orders.stream().map(Order::getPayAmount).reduce(BigDecimal.ZERO, BigDecimal::add)
                        .divide(BigDecimal.valueOf(orders.size()), RoundingMode.HALF_UP));
        return summary;
    }
}