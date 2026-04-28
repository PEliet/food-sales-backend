package com.foodsales.controller;

import com.foodsales.dto.*;
import com.foodsales.model.Message;
import com.foodsales.model.Product;
import com.foodsales.model.User;
import com.foodsales.service.*;
import com.foodsales.util.ResponseResult;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired private UserService userService;
    @Autowired private FavoriteService favoriteService;
    @Autowired private BrowseHistoryService historyService;
    @Autowired private MessageService messageService;
    @Autowired private ReviewService reviewService;

    @PostMapping("/login")
    public ResponseResult<LoginResponse> login(@RequestBody LoginRequest req) {
        try { return ResponseResult.success(userService.login(req)); }
        catch (RuntimeException e) { return ResponseResult.error(e.getMessage()); }
    }

    @GetMapping("/info")
    public ResponseResult<User> getInfo(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        return ResponseResult.success(userService.getUserById(userId));
    }

    @PutMapping("/update")
    public ResponseResult<User> update(@RequestBody User user, HttpServletRequest request) {
        user.setUserId((Integer) request.getAttribute("userId"));
        return ResponseResult.success(userService.updateUser(user));
    }

    @PutMapping("/password")
    public ResponseResult<Void> password(@RequestBody PasswordRequest req, HttpServletRequest request) {
        try { userService.updatePassword((Integer) request.getAttribute("userId"), req);
            return ResponseResult.success(null); }
        catch (RuntimeException e) { return ResponseResult.error(e.getMessage()); }
    }

    @GetMapping("/list")
    public ResponseResult<PageDTO<User>> list(@RequestParam(required=false) String keyword,
            @RequestParam(defaultValue="1") int page, @RequestParam(defaultValue="20") int pageSize) {
        return ResponseResult.success(userService.getUserList(keyword, page, pageSize));
    }

    @PutMapping("/status")
    public ResponseResult<Void> status(@RequestParam Integer userId, @RequestParam Integer status) {
        userService.updateUserStatus(userId, status);
        return ResponseResult.success(null);
    }

    // Favorites
    @GetMapping("/favorites")
    public ResponseResult<List<Product>> favorites(HttpServletRequest request) {
        return ResponseResult.success(favoriteService.getUserFavorites((Integer)request.getAttribute("userId")));
    }

    @PostMapping("/favorite")
    public ResponseResult<Void> addFavorite(@RequestParam Integer productId, HttpServletRequest request) {
        favoriteService.addFavorite((Integer)request.getAttribute("userId"), productId);
        return ResponseResult.success(null);
    }

    @DeleteMapping("/favorite")
    public ResponseResult<Void> removeFavorite(@RequestParam Integer productId, HttpServletRequest request) {
        favoriteService.removeFavorite((Integer)request.getAttribute("userId"), productId);
        return ResponseResult.success(null);
    }

    @GetMapping("/favorite/check")
    public ResponseResult<Boolean> checkFavorite(@RequestParam Integer productId, HttpServletRequest request) {
        return ResponseResult.success(favoriteService.isFavorited((Integer)request.getAttribute("userId"), productId));
    }

    // History
    @GetMapping("/history")
    public ResponseResult<List<Product>> history(HttpServletRequest request) {
        return ResponseResult.success(historyService.getUserHistory((Integer)request.getAttribute("userId")));
    }

    @PostMapping("/history")
    public ResponseResult<Void> addHistory(@RequestParam Integer productId, HttpServletRequest request) {
        historyService.addHistory((Integer)request.getAttribute("userId"), productId);
        return ResponseResult.success(null);
    }

    @DeleteMapping("/history")
    public ResponseResult<Void> clearHistory(HttpServletRequest request) {
        historyService.clearHistory((Integer)request.getAttribute("userId"));
        return ResponseResult.success(null);
    }

    // Messages
    @GetMapping("/messages")
    public ResponseResult<List<Message>> messages(HttpServletRequest request,
                                                  @RequestParam(required=false) Integer type) {
        return ResponseResult.success(messageService.getUserMessages((Integer)request.getAttribute("userId"), type));
    }

    @GetMapping("/messages/unread")
    public ResponseResult<Integer> unread(HttpServletRequest request) {
        return ResponseResult.success(messageService.getUnreadCount((Integer)request.getAttribute("userId")));
    }

    @PutMapping("/message/read")
    public ResponseResult<Void> readMessage(@RequestParam Integer messageId) {
        messageService.markAsRead(messageId);
        return ResponseResult.success(null);
    }

    // Reviews
    @PostMapping("/review")
    public ResponseResult<Void> addReview(@RequestBody Map<String,Object> body, HttpServletRequest request) {
        reviewService.addReview((Integer)request.getAttribute("userId"),
                Integer.parseInt(body.get("orderId").toString()),
                Integer.parseInt(body.get("productId").toString()),
                Integer.parseInt(body.get("rating").toString()),
                (String)body.get("content"), (String)body.get("images"));
        return ResponseResult.success(null);
    }
}