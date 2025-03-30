package com.example.BookSelling.controller.sellerController;

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
@RequestMapping("/manage-order")
public class ManageOrder {

    OrderService orderService;
    UserService userService;

    @PutMapping("/{orderItemId}/approve")
    public ResponseEntity<OrderItem> approveOrderItem(
            @PathVariable Integer orderItemId) {
        Integer currentSellerId = userService.getCurrentUserId();
        OrderItem result = orderService.approveOrderItem(currentSellerId, orderItemId);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{orderItemId}/reject")
    public ResponseEntity<OrderItem> rejectOrderItem(
            @PathVariable Integer orderItemId) {
        Integer currentSellerId = userService.getCurrentUserId();
        OrderItem result = orderService.rejectOrderItem(currentSellerId, orderItemId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/seller/pending")
    public ResponseEntity<List<OrderItem>> getMyPendingOrders() {
        Integer currentSellerId = userService.getCurrentUserId();
        List<OrderItem> result = orderService.getPendingOrdersForSeller(currentSellerId);
        return ResponseEntity.ok(result);
    }
}
