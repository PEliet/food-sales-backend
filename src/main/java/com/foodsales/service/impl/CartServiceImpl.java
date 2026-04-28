package com.foodsales.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.foodsales.mapper.CartItemMapper;
import com.foodsales.mapper.CartMapper;
import com.foodsales.mapper.ProductMapper;
import com.foodsales.model.Cart;
import com.foodsales.model.CartItem;
import com.foodsales.model.Product;
import com.foodsales.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CartServiceImpl implements CartService {

    @Autowired private CartMapper cartMapper;
    @Autowired private CartItemMapper cartItemMapper;
    @Autowired private ProductMapper productMapper;

    private Cart getOrCreateCart(Integer userId) {
        Cart cart = cartMapper.selectOne(new LambdaQueryWrapper<Cart>().eq(Cart::getUserId, userId));
        if (cart == null) {
            cart = new Cart();
            cart.setUserId(userId);
            cartMapper.insert(cart);
        }
        return cart;
    }

    @Override public Cart getCartByUserId(Integer userId) { return getOrCreateCart(userId); }

    @Override
    public List<Map<String, Object>> getCartItems(Integer userId) {
        Cart cart = getOrCreateCart(userId);
        List<CartItem> items = cartItemMapper.selectList(
                new LambdaQueryWrapper<CartItem>().eq(CartItem::getCartId, cart.getCartId()));
        List<Map<String, Object>> result = new ArrayList<>();
        for (CartItem item : items) {
            Map<String, Object> map = new HashMap<>();
            map.put("cartItemId", item.getCartItemId());
            map.put("productId", item.getProductId());
            map.put("quantity", item.getQuantity());
            map.put("checked", item.getChecked());
            Product p = productMapper.selectById(item.getProductId());
            if (p != null) {
                map.put("productName", p.getName());
                map.put("price", p.getPrice());
                map.put("image", p.getImage());
                map.put("stock", p.getStock());
            }
            result.add(map);
        }
        return result;
    }

    @Override @Transactional
    public void addItem(Integer userId, Integer productId, Integer quantity) {
        Cart cart = getOrCreateCart(userId);
        CartItem existing = cartItemMapper.selectOne(
                new LambdaQueryWrapper<CartItem>().eq(CartItem::getCartId, cart.getCartId()).eq(CartItem::getProductId, productId));
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + quantity);
            cartItemMapper.updateById(existing);
        } else {
            CartItem item = new CartItem();
            item.setCartId(cart.getCartId());
            item.setProductId(productId);
            item.setQuantity(quantity);
            item.setChecked(1);
            cartItemMapper.insert(item);
        }
    }

    @Override
    public void updateItemQuantity(Integer userId, Integer cartItemId, Integer quantity) {
        Cart cart = getOrCreateCart(userId);
        CartItem item = cartItemMapper.selectById(cartItemId);
        if (item != null && item.getCartId().equals(cart.getCartId())) {
            item.setQuantity(quantity);
            cartItemMapper.updateById(item);
        }
    }

    @Override
    public void deleteItem(Integer userId, Integer cartItemId) {
        Cart cart = getOrCreateCart(userId);
        cartItemMapper.delete(new LambdaQueryWrapper<CartItem>()
                .eq(CartItem::getCartItemId, cartItemId).eq(CartItem::getCartId, cart.getCartId()));
    }

    @Override
    public void toggleCheck(Integer userId, Integer cartItemId, Integer checked) {
        Cart cart = getOrCreateCart(userId);
        CartItem item = cartItemMapper.selectById(cartItemId);
        if (item != null && item.getCartId().equals(cart.getCartId())) {
            item.setChecked(checked);
            cartItemMapper.updateById(item);
        }
    }

    @Override
    public void toggleAllCheck(Integer userId, Integer checked) {
        Cart cart = getOrCreateCart(userId);
        CartItem item = new CartItem();
        item.setChecked(checked);
        cartItemMapper.update(item, new LambdaQueryWrapper<CartItem>().eq(CartItem::getCartId, cart.getCartId()));
    }

    @Override
    public void clearCart(Integer userId) {
        Cart cart = getOrCreateCart(userId);
        cartItemMapper.delete(new LambdaQueryWrapper<CartItem>().eq(CartItem::getCartId, cart.getCartId()));
    }
}