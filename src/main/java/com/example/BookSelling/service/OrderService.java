package com.example.BookSelling.service;

import com.example.BookSelling.dto.request.OrderItemRequest;
import com.example.BookSelling.model.OrderItem;
import jakarta.transaction.Transactional;

import java.util.List;

public interface OrderService {

    @Transactional
    OrderItem addToOrder(Integer userId, OrderItemRequest request);
    @Transactional
    OrderItem updateOrderItemQuantity(Integer userId, Integer orderItemId, Integer quantity);
    @Transactional
    void cancelOrderItem(Integer userId, Integer orderItemId);
    @Transactional
    OrderItem approveOrderItem(Integer sellerId, Integer orderItemId);
    List<OrderItem> getPendingOrdersForSeller(Integer sellerId);
    @Transactional
    OrderItem rejectOrderItem(Integer sellerId, Integer orderItemId);
    List<OrderItem> getOrdersByUserId(Integer userId);
}
