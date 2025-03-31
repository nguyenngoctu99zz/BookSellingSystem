package com.example.BookSelling.controller.sellerController;

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
@RequestMapping("/manage-order")
public class ManageOrder {

    OrderService orderService;
    UserService userService;

    @PutMapping("/{orderItemId}/approve")
    public ResponseData<OrderItemResponse> approveOrderItem(
            @PathVariable Integer orderItemId) {
        Integer currentSellerId = userService.getCurrentUserId();
        OrderItemResponse result = orderService.approveOrderItem(currentSellerId, orderItemId);
        return ResponseData.<OrderItemResponse>builder()
                .message("Order approved successfully")
                .data(result)
                .build();
    }

    @PutMapping("/{orderItemId}/reject")
    public ResponseData<OrderItemResponse> rejectOrderItem(
            @PathVariable Integer orderItemId) {
        Integer currentSellerId = userService.getCurrentUserId();
        OrderItemResponse result = orderService.rejectOrderItem(currentSellerId, orderItemId);
        return ResponseData.<OrderItemResponse>builder()
                .message("Order rejected successfully")
                .data(result)
                .build();
    }

    @GetMapping("/seller/pending")
    public ResponseData<List<OrderItemResponse>> getMyPendingOrders() {
        Integer currentSellerId = userService.getCurrentUserId();
        List<OrderItemResponse> result = orderService.getPendingOrdersForSeller(currentSellerId);
        return ResponseData.<List<OrderItemResponse>>builder()
                .message("Pending orders retrieved successfully")
                .data(result)
                .build();
    }

    @DeleteMapping("/cancel/{orderItemId}")
    public ResponseData<Void> cancelOrderItem(
            @PathVariable Integer orderItemId) {
        Integer currentUserId = userService.getCurrentUserId();
        orderService.cancelOrderItem(currentUserId, orderItemId);
        return ResponseData.<Void>builder()
                .message("Order cancelled successfully")
                .build();
    }

}
