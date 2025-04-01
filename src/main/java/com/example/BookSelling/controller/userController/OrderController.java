package com.example.BookSelling.controller.userController;

import com.example.BookSelling.dto.request.OrderItemRequest;
import com.example.BookSelling.dto.response.OrderItemResponse;
import com.example.BookSelling.dto.response.ResponseData;
import com.example.BookSelling.service.OrderService;
import com.example.BookSelling.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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
    public ResponseData<List<OrderItemResponse>> showMyOrder() {
        Integer currentUserId = userService.getCurrentUserId();
        return ResponseData.<List<OrderItemResponse>>builder()
                .data(orderService.getOrdersByUserId(currentUserId))
                .message("Get orders successfully")
                .build();
    }

    @PostMapping
    public ResponseData<OrderItemResponse> addToOrder(@RequestBody OrderItemRequest request) {
        Integer currentUserId = userService.getCurrentUserId();
        return ResponseData.<OrderItemResponse>builder()
                .data(orderService.addToOrder(currentUserId, request))
                .message("Add to order successfully")
                .build();
    }

    @PutMapping("/update-quantity/{orderItemId}")
    public ResponseData<OrderItemResponse> updateOrderItemQuantity(
            @PathVariable Integer orderItemId,
            @RequestParam Integer quantity) {
        Integer currentUserId = userService.getCurrentUserId();
        return ResponseData.<OrderItemResponse>builder()
                .data(orderService.updateOrderItemQuantity(currentUserId, orderItemId, quantity))
                .message("Update quantity successfully")
                .build();
    }

    @DeleteMapping("/cancel/{orderItemId}")
    public ResponseData<Void> cancelOrderItem(@PathVariable Integer orderItemId) {
        Integer currentUserId = userService.getCurrentUserId();
        orderService.cancelOrderItem(currentUserId, orderItemId);
        return ResponseData.<Void>builder()
                .message("Cancel order successfully")
                .build();
    }
}
