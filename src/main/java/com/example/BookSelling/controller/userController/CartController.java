package com.example.BookSelling.controller.userController;


import com.example.BookSelling.dto.request.CartItemRequest;
import com.example.BookSelling.dto.response.CartItemResponse;
import com.example.BookSelling.dto.response.OrderItemResponse;
import com.example.BookSelling.dto.response.ResponseData;
import com.example.BookSelling.service.CartService;
import com.example.BookSelling.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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
    public ResponseData<List<CartItemResponse>> showMyCart() {
        return ResponseData.<List<CartItemResponse>>builder()
                .data(cartService.getCartItemsByUserId(userService.getCurrentUserId()))
                .message("Get cart successfully")
                .build();
    }

    @PostMapping("/add-cart")
    public ResponseData<CartItemResponse> addToCart(@RequestBody CartItemRequest request) {
        return ResponseData.<CartItemResponse>builder()
                .data(cartService.addToCart(userService.getCurrentUserId(), request))
                .message("Add to cart successfully")
                .build();
    }

    @PutMapping("/update-quantity/{cartItemId}")
    public ResponseData<CartItemResponse> updateCartItemQuantity(
            @PathVariable Integer cartItemId,
            @RequestParam Integer quantity) {
        return ResponseData.<CartItemResponse>builder()
                .data(cartService.updateCartItemQuantity(
                        userService.getCurrentUserId(),
                        cartItemId,
                        quantity))
                .message("Update quantity successfully")
                .build();
    }

    @DeleteMapping("/remove-item/{cartItemId}")
    public ResponseData<Void> removeFromCart(@PathVariable Integer cartItemId) {
        cartService.removeFromCart(userService.getCurrentUserId(), cartItemId);
        return ResponseData.<Void>builder()
                .message("Remove item successfully")
                .build();
    }

    @DeleteMapping("/clear")
    public ResponseData<Void> clearCart() {
        cartService.clearCart(userService.getCurrentUserId());
        return ResponseData.<Void>builder()
                .message("Clear cart successfully")
                .build();
    }

    @PostMapping("/checkout/{cartItemId}")
    public ResponseData<OrderItemResponse> checkoutSingleItem(
            @PathVariable Integer cartItemId) {
        return ResponseData.<OrderItemResponse>builder()
                .data(cartService.checkoutSingleItem(
                        userService.getCurrentUserId(),
                        cartItemId))
                .message("Checkout successfully")
                .build();
    }
}
