package com.example.BookSelling.dto.response;


import com.example.BookSelling.common.OrderStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItemResponse {
    Integer orderItemId;
    Integer bookId;
    String bookTitle;
    Integer quantity;
    LocalDateTime orderDate;
    OrderStatus orderStatus;
    BigDecimal totalPrice;
}