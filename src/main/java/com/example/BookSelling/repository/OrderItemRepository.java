package com.example.BookSelling.repository;

import com.example.BookSelling.common.OrderStatus;
import com.example.BookSelling.model.OrderItem;
import com.example.BookSelling.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;


public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

    List<OrderItem> findByOrderStatusAndBookUser(OrderStatus orderStatus, User seller);
    List<OrderItem> findByUser(User user);
}
