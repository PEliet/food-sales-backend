package com.foodsales.service;

import com.foodsales.dto.*;
import com.foodsales.model.*;
import com.foodsales.dto.PageDTO;
import java.util.List;
import java.util.Map;

public interface UserService {
    LoginResponse login(LoginRequest req);
    User getUserById(Integer id);
    User updateUser(User user);
    void updatePassword(Integer userId, PasswordRequest req);
    PageDTO<User> getUserList(String keyword, int page, int pageSize);
    void updateUserStatus(Integer userId, Integer status);
}