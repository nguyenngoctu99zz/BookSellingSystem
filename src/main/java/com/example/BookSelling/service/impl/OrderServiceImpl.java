package com.example.BookSelling.service.impl;

import com.example.BookSelling.common.OrderStatus;
import com.example.BookSelling.dto.request.OrderItemRequest;
import com.example.BookSelling.exception.AppException;
import com.example.BookSelling.exception.ErrorCode;
import com.example.BookSelling.model.Book;
import com.example.BookSelling.model.OrderItem;
import com.example.BookSelling.model.User;
import com.example.BookSelling.repository.*;
import com.example.BookSelling.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class OrderServiceImpl implements OrderService {

    OrderItemRepository orderItemRepository;
    UserRepository userRepository;
    BookRepository bookRepository;

    @Override
    @Transactional
    public OrderItem addToOrder(Integer userId, OrderItemRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (book.getQuantity() < request.getQuantity()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough quantity available");        }

        book.setQuantity(book.getQuantity() - request.getQuantity());
        bookRepository.save(book);

        OrderItem orderItem = OrderItem.builder()
                .user(user)
                .book(book)
                .quantity(request.getQuantity())
                .orderDate(LocalDateTime.now())
                .orderStatus(OrderStatus.PENDING)
                .totalPrice(book.getPrice() * request.getQuantity())
                .build();

        return orderItemRepository.save(orderItem);
    }

    @Override
    @Transactional
    public OrderItem updateOrderItemQuantity(Integer userId, Integer orderItemId, Integer quantity) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "OrderItem not found"));

        if (!orderItem.getUser().getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized access");
        }

        Book book = orderItem.getBook();
        int quantityDifference = quantity - orderItem.getQuantity();

        if (book.getQuantity() < quantityDifference) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough quantity available");
        }

        // Update book quantity
        book.setQuantity(book.getQuantity() - quantityDifference);
        bookRepository.save(book);

        orderItem.setQuantity(quantity);
        orderItem.setTotalPrice(book.getPrice() * quantity);

        return orderItemRepository.save(orderItem);
    }

    @Override
    @Transactional
    public void cancelOrderItem(Integer userId, Integer orderItemId) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new RuntimeException("OrderItem not found"));

        if (!orderItem.getUser().getUserId().equals(userId)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        if (orderItem.getOrderStatus() == OrderStatus.PENDING ||
                orderItem.getOrderStatus() == OrderStatus.ACCEPTED) {
            Book book = orderItem.getBook();
            book.setQuantity(book.getQuantity() + orderItem.getQuantity());
            bookRepository.save(book);
        }

        orderItemRepository.delete(orderItem);
    }


    @Override
    @Transactional
    public OrderItem approveOrderItem(Integer sellerId, Integer orderItemId) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new RuntimeException("Order not found"));


        if (orderItem.getOrderStatus() != OrderStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order is not in pending status");
        }

        Book book = orderItem.getBook();
        if (book.getQuantity() < orderItem.getQuantity()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough quantity available");
        }

        orderItem.setOrderStatus(OrderStatus.ACCEPTED);
        return orderItemRepository.save(orderItem);
    }

    @Override
    public List<OrderItem> getPendingOrdersForSeller(Integer sellerId) {
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return orderItemRepository.findByOrderStatusAndBookUser(OrderStatus.PENDING, seller);
    }

    @Override
    @Transactional
    public OrderItem rejectOrderItem(Integer sellerId, Integer orderItemId) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));


        if (orderItem.getOrderStatus() != OrderStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only pending orders can be rejected");
        }

        Book book = orderItem.getBook();
        book.setQuantity(book.getQuantity() + orderItem.getQuantity());
        bookRepository.save(book);

        orderItem.setOrderStatus(OrderStatus.REJECTED);
        return orderItemRepository.save(orderItem);
    }

    @Override
    public List<OrderItem> getOrdersByUserId(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return orderItemRepository.findByUser(user);
    }
}
