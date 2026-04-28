package com.foodsales.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.foodsales.dto.*;
import com.foodsales.mapper.UserMapper;
import com.foodsales.model.User;
import com.foodsales.model.Address;
import com.foodsales.service.UserService;
import com.foodsales.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {

    @Autowired private UserMapper userMapper;
    @Autowired private com.foodsales.mapper.AddressMapper addressMapper;
    @Autowired private JwtUtil jwtUtil;

    @Override
    public LoginResponse login(LoginRequest req) {
        User user;
        if (req.getLoginType() != null && req.getLoginType() == 1) {
            user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getOpenid, req.getCode()));
            if (user == null) {
                user = new User();
                user.setUserNo("U" + System.currentTimeMillis());
                user.setUsername("wx_" + req.getCode().substring(0, Math.min(8, req.getCode().length())));
                user.setNickname("微信用户");
                user.setOpenid(req.getCode());
                user.setRole(0);
                user.setBalance(BigDecimal.ZERO);
                user.setPoints(0);
                user.setStatus(1);
                user.setCreateTime(LocalDateTime.now());
                userMapper.insert(user);
                // 新用户自动创建默认地址
                Address addr = new Address();
                addr.setUserId(user.getUserId());
                addr.setReceiver("请填写收货人");
                addr.setPhone("请填写手机号");
                addr.setProvince("请填写省份");
                addr.setCity("请填写城市");
                addr.setDistrict("请填写区县");
                addr.setDetail("请填写详细地址");
                addr.setIsDefault(1);
                addr.setCreateTime(LocalDateTime.now());
                addressMapper.insert(addr);
            }
        } else {
            user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                    .eq(User::getUsername, req.getUsername()).eq(User::getRole, 0));
            if (user == null || !req.getPassword().equals(user.getPassword())) {
                throw new RuntimeException("用户名或密码错误");
            }
            if (user.getStatus() == 0) throw new RuntimeException("账号已被禁用");
        }
        String token = jwtUtil.generateToken(user.getUserId(), user.getUsername(), user.getRole());
        return buildLoginResponse(user, token);
    }

    private LoginResponse buildLoginResponse(User user, String token) {
        LoginResponse resp = new LoginResponse();
        resp.setToken(token);
        resp.setUserId(user.getUserId());
        resp.setUsername(user.getUsername());
        resp.setNickname(user.getNickname());
        resp.setAvatar(user.getAvatar());
        resp.setRole(user.getRole());
        resp.setPhone(user.getPhone());
        resp.setBalance(user.getBalance());
        resp.setPoints(user.getPoints());
        return resp;
    }

    @Override public User getUserById(Integer id) { return userMapper.selectById(id); }

    @Override
    public User updateUser(User user) {
        user.setUpdateTime(LocalDateTime.now());
        userMapper.updateById(user);
        return userMapper.selectById(user.getUserId());
    }

    @Override
    public void updatePassword(Integer userId, PasswordRequest req) {
        User user = userMapper.selectById(userId);
        if (!user.getPassword().equals(req.getOldPassword())) throw new RuntimeException("原密码错误");
        user.setPassword(req.getNewPassword());
        userMapper.updateById(user);
    }

    @Override
    public PageDTO<User> getUserList(String keyword, int page, int pageSize) {
        Page<User> p = new Page<>(page, pageSize);
        LambdaQueryWrapper<User> q = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            q.like(User::getUsername, keyword).or().like(User::getPhone, keyword).or().like(User::getNickname, keyword);
        }
        q.orderByDesc(User::getCreateTime);
        userMapper.selectPage(p, q);
        return PageDTO.of(p.getTotal(), page, pageSize, p.getRecords());
    }

    @Override
    public void updateUserStatus(Integer userId, Integer status) {
        User user = userMapper.selectById(userId);
        if (user != null) { user.setStatus(status); userMapper.updateById(user); }
    }
}