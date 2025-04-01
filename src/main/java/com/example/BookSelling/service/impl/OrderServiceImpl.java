package com.example.BookSelling.service.impl;

import com.example.BookSelling.common.OrderStatus;
import com.example.BookSelling.dto.request.OrderItemRequest;
import com.example.BookSelling.dto.response.OrderItemResponse;
import com.example.BookSelling.exception.AppException;
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
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<OrderItemResponse> getOrdersByUserId(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException("User not found"));

        return orderItemRepository.findByUser(user).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderItemResponse addToOrder(Integer userId, OrderItemRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException("User not found"));

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new AppException("Book not found"));

        if (book.getQuantity() < request.getQuantity()) {
            throw new AppException("Not enough quantity available");
        }

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

        OrderItem savedItem = orderItemRepository.save(orderItem);
        return mapToResponse(savedItem);
    }

    @Override
    @Transactional
    public OrderItemResponse updateOrderItemQuantity(Integer userId, Integer orderItemId, Integer quantity) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new AppException("Order item not found"));

        if (!orderItem.getUser().getUserId().equals(userId)) {
            throw new AppException("Unauthorized access");
        }

        if (orderItem.getOrderStatus() != OrderStatus.PENDING) {
            throw new AppException("Only pending orders can be modified");
        }

        Book book = orderItem.getBook();
        int quantityDifference = quantity - orderItem.getQuantity();

        if (book.getQuantity() < quantityDifference) {
            throw new AppException("Not enough quantity available");
        }

        book.setQuantity(book.getQuantity() - quantityDifference);
        bookRepository.save(book);

        orderItem.setQuantity(quantity);
        orderItem.setTotalPrice(book.getPrice() * quantity);

        OrderItem updatedItem = orderItemRepository.save(orderItem);
        return mapToResponse(updatedItem);
    }

    @Override
    @Transactional
    public void cancelOrderItem(Integer userId, Integer orderItemId) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new AppException("Order item not found"));

        if (!orderItem.getUser().getUserId().equals(userId)) {
            throw new AppException("Unauthorized access");
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
    public OrderItemResponse approveOrderItem(Integer sellerId, Integer orderItemId) {
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new AppException("Seller not found"));

        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new AppException("Order not found"));

        if (!orderItem.getBook().getUser().getUserId().equals(sellerId)) {
            throw new AppException("Unauthorized access");
        }

        if (orderItem.getOrderStatus() != OrderStatus.PENDING) {
            throw new AppException("Only pending orders can be approved");
        }

        if (orderItem.getBook().getQuantity() < orderItem.getQuantity()) {
            throw new AppException("Not enough quantity available");
        }

        orderItem.setOrderStatus(OrderStatus.ACCEPTED);
        OrderItem updatedItem = orderItemRepository.save(orderItem);
        return mapToResponse(updatedItem);
    }

    @Override
    @Transactional
    public List<OrderItemResponse> getPendingOrdersForSeller(Integer sellerId) {
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new AppException("Seller not found"));

        return orderItemRepository.findByOrderStatusAndBookUser(OrderStatus.PENDING, seller)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderItemResponse rejectOrderItem(Integer sellerId, Integer orderItemId) {
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new AppException("Seller not found"));

        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new AppException("Order not found"));

        if (!orderItem.getBook().getUser().getUserId().equals(sellerId)) {
            throw new AppException("Unauthorized access");
        }

        if (orderItem.getOrderStatus() != OrderStatus.PENDING) {
            throw new AppException("Only pending orders can be rejected");
        }

        Book book = orderItem.getBook();
        book.setQuantity(book.getQuantity() + orderItem.getQuantity());
        bookRepository.save(book);

        orderItem.setOrderStatus(OrderStatus.REJECTED);
        OrderItem updatedItem = orderItemRepository.save(orderItem);
        return mapToResponse(updatedItem);
    }


    private OrderItemResponse mapToResponse(OrderItem orderItem) {
        return OrderItemResponse.builder()
                .orderItemId(orderItem.getOrderItemId())
                .bookId(orderItem.getBook().getBookId())
                .bookTitle(orderItem.getBook().getBookTitle())
                .quantity(orderItem.getQuantity())
                .orderDate(orderItem.getOrderDate())
                .orderStatus(orderItem.getOrderStatus())
                .totalPrice(BigDecimal.valueOf(orderItem.getTotalPrice()))
                .build();
    }

}
