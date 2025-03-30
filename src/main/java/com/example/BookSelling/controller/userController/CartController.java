package com.example.BookSelling.controller.userController;


import com.example.BookSelling.dto.request.CartItemRequest;
import com.example.BookSelling.model.CartItem;
import com.example.BookSelling.model.OrderItem;
import com.example.BookSelling.service.CartService;
import com.example.BookSelling.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/cart")
public class CartController {
    CartService cartService;
    UserService userService;

    @GetMapping("/my-cart")
    public ResponseEntity<List<CartItem>> showMyCart() {
        Integer currentUserId = userService.getCurrentUserId();
        List<CartItem> result = cartService.getCartItemsByUserId(currentUserId);
        return ResponseEntity.ok(result);
    }


    @PostMapping("/add-cart")
    public ResponseEntity<CartItem> addToCart(@RequestBody CartItemRequest request) {
        Integer currentUserId = userService.getCurrentUserId();

        CartItem result = cartService.addToCart(currentUserId, request);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/update-quantity/{cartItemId}")
    public ResponseEntity<CartItem> updateCartItemQuantity(
            @PathVariable Integer cartItemId,
            @RequestParam Integer quantity) {
        Integer currentUserId = userService.getCurrentUserId();
        CartItem result = cartService.updateCartItemQuantity(currentUserId, cartItemId, quantity);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/remove-item/{cartItemId}")
    public ResponseEntity<Void> removeFromCart(@PathVariable Integer cartItemId) {
        Integer currentUserId = userService.getCurrentUserId();
        cartService.removeFromCart(currentUserId, cartItemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart() {
        Integer currentUserId = userService.getCurrentUserId();
        cartService.clearCart(currentUserId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/checkout/{cartItemId}")
    public ResponseEntity<OrderItem> checkoutSingleItem(
            @PathVariable Integer cartItemId) {
        Integer currentUserId = userService.getCurrentUserId();
        OrderItem orderItem = cartService.checkoutSingleItem(currentUserId, cartItemId);
        return ResponseEntity.ok(orderItem);
    }

}
