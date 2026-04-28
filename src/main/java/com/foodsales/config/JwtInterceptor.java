package com.foodsales.config;

import com.foodsales.util.JwtUtil;
import com.foodsales.util.ResponseResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String method = request.getMethod();
        if ("OPTIONS".equalsIgnoreCase(method)) return true;

        String path = request.getRequestURI();
        if (path.contains("/login") || path.contains("/register") ||
            path.contains("/product/list") || path.contains("/product/hot") ||
            path.contains("/product/new") || path.contains("/product/detail") ||
            path.contains("/category/list") || path.contains("/banner/list") ||
            path.contains("/search")) {
            return true;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            writeUnauthorized(response, "Missing token");
            return false;
        }

        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            writeUnauthorized(response, "Invalid or expired token");
            return false;
        }

        request.setAttribute("userId", jwtUtil.getUserId(token));
        request.setAttribute("role", jwtUtil.getRole(token));
        return true;
    }

    private void writeUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(ResponseResult.unauthorized(message)));
    }
}
