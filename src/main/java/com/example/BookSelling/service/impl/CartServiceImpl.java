package com.example.BookSelling.service.impl;

import com.example.BookSelling.common.OrderStatus;
import com.example.BookSelling.dto.request.CartItemRequest;
import com.example.BookSelling.dto.response.CartItemResponse;
import com.example.BookSelling.dto.response.OrderItemResponse;
import com.example.BookSelling.exception.AppException;
import com.example.BookSelling.exception.ErrorCode;
import com.example.BookSelling.model.Book;
import com.example.BookSelling.model.CartItem;
import com.example.BookSelling.model.OrderItem;
import com.example.BookSelling.model.User;
import com.example.BookSelling.repository.BookRepository;
import com.example.BookSelling.repository.CartItemRepository;
import com.example.BookSelling.repository.OrderItemRepository;
import com.example.BookSelling.repository.UserRepository;
import com.example.BookSelling.service.CartService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    @Transactional
    public List<CartItemResponse> getCartItemsByUserId(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        List<CartItem> cartItems = cartItemRepository.findByUser(user);

        return cartItems.stream()
                .map(this::mapToCartItemResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CartItemResponse addToCart(Integer userId, CartItemRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found"));

        Optional<CartItem> existingCartItemOpt = cartItemRepository.findByUserAndBook(user, book);

        if (existingCartItemOpt.isPresent()) {
            CartItem existingCartItem = existingCartItemOpt.get();
            existingCartItem.setQuantity(existingCartItem.getQuantity() + request.getQuantity());
            existingCartItem.setTotalPrice(calculateTotalPrice(existingCartItem));
            CartItem savedItem = cartItemRepository.save(existingCartItem);
            return mapToCartItemResponse(savedItem);
        }

        CartItem cartItem = CartItem.builder()
                .user(user)
                .book(book)
                .quantity(request.getQuantity())
                .totalPrice(calculateTotalPrice(book, request.getQuantity()))
                .build();

        CartItem savedItem = cartItemRepository.save(cartItem);
        return mapToCartItemResponse(savedItem);
    }

    @Override
    @Transactional
    public CartItemResponse updateCartItemQuantity(Integer userId, Integer cartItemId, Integer quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (!cartItem.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        cartItem.setQuantity(quantity);
        cartItem.setTotalPrice(calculateTotalPrice(cartItem));
        CartItem savedItem = cartItemRepository.save(cartItem);
        return mapToCartItemResponse(savedItem);
    }

    @Override
    @Transactional
    public void removeFromCart(Integer userId, Integer cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (!cartItem.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        cartItemRepository.delete(cartItem);
    }

    @Override
    @Transactional
    public void clearCart(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        cartItemRepository.deleteAllByUser(user);
    }

    @Override
    @Transactional
    public OrderItemResponse checkoutSingleItem(Integer userId, Integer cartItemId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (!cartItem.getUser().getUserId().equals(userId)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        OrderItem orderItem = OrderItem.builder()
                .user(user)
                .book(cartItem.getBook())
                .quantity(cartItem.getQuantity())
                .orderDate(LocalDateTime.now())
                .orderStatus(OrderStatus.PENDING)
                .totalPrice(cartItem.getTotalPrice())
                .build();

        OrderItem savedOrderItem = orderItemRepository.save(orderItem);
        cartItemRepository.delete(cartItem);

        return mapToOrderItemResponse(savedOrderItem);
    }

    private double calculateTotalPrice(CartItem cartItem) {
        return cartItem.getBook().getPrice() * cartItem.getQuantity();
    }

    private double calculateTotalPrice(Book book, Integer quantity) {
        return book.getPrice() * quantity;
    }

    private CartItemResponse mapToCartItemResponse(CartItem cartItem) {
        return CartItemResponse.builder()
                .cartItemId(cartItem.getCartItemId())
                .bookId(cartItem.getBook().getBookId())
                .bookTitle(cartItem.getBook().getBookTitle())
                .bookPrice(BigDecimal.valueOf(cartItem.getBook().getPrice()))
                .quantity(cartItem.getQuantity())
                .totalPrice(BigDecimal.valueOf(cartItem.getTotalPrice()))
                .build();
    }

    private OrderItemResponse mapToOrderItemResponse(OrderItem orderItem) {
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

