package com.example.BookSelling.dto.response;

import com.example.BookSelling.model.Book;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemResponse {
    Integer cartItemId;
    Integer bookId;
    String bookTitle;
    BigDecimal bookPrice;
    Integer quantity;
    BigDecimal totalPrice;
}