package com.example.BookSelling.service;

import com.example.BookSelling.dto.request.CartItemRequest;
import com.example.BookSelling.dto.response.CartItemResponse;
import com.example.BookSelling.dto.response.OrderItemResponse;


import java.util.List;

public interface CartService{

    List<CartItemResponse> getCartItemsByUserId(Integer userId);
    CartItemResponse addToCart(Integer userId, CartItemRequest request);
    CartItemResponse updateCartItemQuantity(Integer userId, Integer cartItemId, Integer quantity);
    void removeFromCart(Integer userId, Integer cartItemId);
    void clearCart(Integer userId);
    OrderItemResponse checkoutSingleItem(Integer userId, Integer cartItemId);

}
