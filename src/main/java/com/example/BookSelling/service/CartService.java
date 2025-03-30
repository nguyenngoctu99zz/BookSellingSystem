package com.example.BookSelling.service;

import com.example.BookSelling.dto.request.CartItemRequest;
import com.example.BookSelling.model.CartItem;
import com.example.BookSelling.model.OrderItem;
import jakarta.transaction.Transactional;

import java.util.List;

public interface CartService{

    CartItem addToCart(Integer userId, CartItemRequest request);
    CartItem updateCartItemQuantity(Integer userId, Integer cartItemId, Integer quantity);
    void removeFromCart(Integer userId, Integer cartItemId);
    void clearCart(Integer userId);
    @Transactional
    OrderItem checkoutSingleItem(Integer userId, Integer cartItemId);
    List<CartItem> getCartItemsByUserId(Integer userId);
}
