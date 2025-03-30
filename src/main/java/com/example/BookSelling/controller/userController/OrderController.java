package com.example.BookSelling.controller.userController;

import com.example.BookSelling.dto.request.OrderItemRequest;
import com.example.BookSelling.model.OrderItem;
import com.example.BookSelling.service.OrderService;
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
@RequestMapping("/order")
public class OrderController {

    OrderService orderService;
    UserService userService;

    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderItem>> showMyOrder() {
        Integer currentUserId = userService.getCurrentUserId();
        List<OrderItem> result = orderService.getOrdersByUserId(currentUserId);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<OrderItem> addToOrder(@RequestBody OrderItemRequest request) {
        Integer currentUserId = userService.getCurrentUserId();
        OrderItem result = orderService.addToOrder(currentUserId, request);
        return ResponseEntity.ok(result);
    }


    @PutMapping("/update-quantity/{orderItemId}")
    public ResponseEntity<OrderItem> updateOrderItemQuantity(
            @PathVariable Integer orderItemId,
            @RequestParam Integer quantity) {
        Integer currentUserId = userService.getCurrentUserId();
        OrderItem result = orderService.updateOrderItemQuantity(currentUserId, orderItemId, quantity);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/cancel/{orderItemId}")
    public ResponseEntity<Void> cancelOrderItem(@PathVariable Integer orderItemId) {
        Integer currentUserId = userService.getCurrentUserId();
        orderService.cancelOrderItem(currentUserId, orderItemId);
        return ResponseEntity.noContent().build();
    }

}
