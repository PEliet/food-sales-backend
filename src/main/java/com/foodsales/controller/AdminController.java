package com.foodsales.controller;

import com.foodsales.dto.*;
import com.foodsales.model.Product;
import com.foodsales.model.User;
import com.foodsales.service.*;
import com.foodsales.util.JwtUtil;
import com.foodsales.util.ResponseResult;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired private UserService userService;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private DashboardService dashboardService;
    @Autowired private SysLogService sysLogService;
    @Autowired private ProductService productService;

    @PostMapping("/login")
    public ResponseResult<LoginResponse> login(@RequestBody LoginRequest req) {
        User user = userService.getUserList(req.getUsername(), 1, 1).getList().stream()
                .filter(u -> u.getUsername().equals(req.getUsername()) && u.getRole() >= 1)
                .findFirst().orElse(null);
        if (user == null || !req.getPassword().equals(user.getPassword()))
            return ResponseResult.error("用户名或密码错误");
        if (user.getStatus() == 0) return ResponseResult.error("账号已被禁用");
        String token = jwtUtil.generateToken(user.getUserId(), user.getUsername(), user.getRole());
        LoginResponse resp = new LoginResponse();
        resp.setToken(token); resp.setUserId(user.getUserId());
        resp.setUsername(user.getUsername()); resp.setNickname(user.getNickname());
        resp.setAvatar(user.getAvatar()); resp.setRole(user.getRole());
        return ResponseResult.success(resp);
    }

    @GetMapping("/profile")
    public ResponseResult<User> profile(HttpServletRequest request) {
        return ResponseResult.success(userService.getUserById((Integer)request.getAttribute("userId")));
    }

    @PutMapping("/updateProfile")
    public ResponseResult<User> updateProfile(@RequestBody User user, HttpServletRequest request) {
        user.setUserId((Integer)request.getAttribute("userId"));
        return ResponseResult.success(userService.updateUser(user));
    }

    // Dashboard
    @GetMapping("/dashboard/stats")
    public ResponseResult<Map<String,Object>> dashboardStats() {
        return ResponseResult.success(dashboardService.getDashboardStats());
    }

    @GetMapping("/statistics/sales")
    public ResponseResult<List<Map<String,Object>>> salesTrend(
            @RequestParam(defaultValue="week") String period) {
        return ResponseResult.success(dashboardService.getSalesTrend(period));
    }

    @GetMapping("/statistics/topProducts")
    public ResponseResult<List<Map<String,Object>>> topProducts(
            @RequestParam(defaultValue="10") int limit) {
        return ResponseResult.success(dashboardService.getTopProducts(limit));
    }

    @GetMapping("/statistics/status")
    public ResponseResult<Map<String,Object>> orderStatus() {
        return ResponseResult.success(dashboardService.getOrderStatusDistribution());
    }

    @GetMapping("/statistics/summary")
    public ResponseResult<Map<String,Object>> summary(
            @RequestParam String startDate, @RequestParam String endDate) {
        return ResponseResult.success(dashboardService.getSalesSummary(startDate, endDate));
    }

    // Logs
    @GetMapping("/log/list")
    public ResponseResult<PageDTO<com.foodsales.model.SysLog>> logList(
            @RequestParam(required=false) String module,
            @RequestParam(required=false) String action,
            @RequestParam(defaultValue="1") int page,
            @RequestParam(defaultValue="20") int pageSize) {
        return ResponseResult.success(sysLogService.getLogs(module, action, page, pageSize));
    }

    // Customer
    @GetMapping("/customer/list")
    public ResponseResult<PageDTO<User>> customerList(
            @RequestParam(required=false) String keyword,
            @RequestParam(defaultValue="1") int page,
            @RequestParam(defaultValue="20") int pageSize) {
        return ResponseResult.success(userService.getUserList(keyword, page, pageSize));
    }

    // Report export
    @GetMapping("/report/export")
    public ResponseResult<String> exportReport(@RequestParam String type,
            @RequestParam String startDate, @RequestParam String endDate) {
        return ResponseResult.success("导出功能请通过API直接下载: /api/report/download?type="
                + type + "&start=" + startDate + "&end=" + endDate);
    }

    // Search all products (open endpoint)
    @GetMapping("/search")
    public ResponseResult<PageDTO<Product>> search(
            @RequestParam String keyword,
            @RequestParam(defaultValue="1") int page,
            @RequestParam(defaultValue="20") int pageSize) {
        return ResponseResult.success(productService.getProductList(null, keyword, null, null, page, pageSize));
    }
}