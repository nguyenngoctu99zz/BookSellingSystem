package com.example.BookSelling.service;

import com.example.BookSelling.dto.request.OrderItemRequest;
import com.example.BookSelling.dto.response.OrderItemResponse;


import java.util.List;

public interface OrderService {
    List<OrderItemResponse> getOrdersByUserId(Integer userId);
    OrderItemResponse addToOrder(Integer userId, OrderItemRequest request);
    OrderItemResponse updateOrderItemQuantity(Integer userId, Integer orderItemId, Integer quantity);
    void cancelOrderItem(Integer userId, Integer orderItemId);
    OrderItemResponse approveOrderItem(Integer sellerId, Integer orderItemId);
    List<OrderItemResponse> getPendingOrdersForSeller(Integer sellerId);
    OrderItemResponse rejectOrderItem(Integer sellerId, Integer orderItemId);

}
