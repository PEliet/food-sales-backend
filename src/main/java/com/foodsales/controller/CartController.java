package com.foodsales.controller;

import com.foodsales.model.Cart;
import com.foodsales.service.CartService;
import com.foodsales.util.ResponseResult;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired private CartService cartService;

    @GetMapping
    public ResponseResult<List<Map<String,Object>>> getCart(HttpServletRequest request) {
        return ResponseResult.success(cartService.getCartItems((Integer)request.getAttribute("userId")));
    }

    @PostMapping("/add")
    public ResponseResult<Void> add(@RequestBody Map<String,Object> params,
            HttpServletRequest request) {
        Integer productId = Integer.valueOf(params.get("productId").toString());
        Integer quantity = params.containsKey("quantity") ? Integer.valueOf(params.get("quantity").toString()) : 1;
        cartService.addItem((Integer)request.getAttribute("userId"), productId, quantity);
        return ResponseResult.success(null);
    }

    @PutMapping("/quantity")
    public ResponseResult<Void> updateQuantity(@RequestParam Integer cartItemId,
            @RequestParam Integer quantity, HttpServletRequest request) {
        cartService.updateItemQuantity((Integer)request.getAttribute("userId"), cartItemId, quantity);
        return ResponseResult.success(null);
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseResult<Void> delete(@PathVariable Integer cartItemId, HttpServletRequest request) {
        cartService.deleteItem((Integer)request.getAttribute("userId"), cartItemId);
        return ResponseResult.success(null);
    }

    @PutMapping("/check")
    public ResponseResult<Void> toggleCheck(@RequestParam Integer cartItemId,
            @RequestParam Integer checked, HttpServletRequest request) {
        cartService.toggleCheck((Integer)request.getAttribute("userId"), cartItemId, checked);
        return ResponseResult.success(null);
    }

    @PutMapping("/checkAll")
    public ResponseResult<Void> checkAll(@RequestParam Integer checked, HttpServletRequest request) {
        cartService.toggleAllCheck((Integer)request.getAttribute("userId"), checked);
        return ResponseResult.success(null);
    }

    @DeleteMapping("/clear")
    public ResponseResult<Void> clear(HttpServletRequest request) {
        cartService.clearCart((Integer)request.getAttribute("userId"));
        return ResponseResult.success(null);
    }
}