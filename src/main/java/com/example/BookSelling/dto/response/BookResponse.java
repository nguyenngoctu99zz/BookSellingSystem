package com.example.BookSelling.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookResponse {
    Integer bookId;
    String bookTitle;
    String publisher;
    String author;
    Integer quantity;
    Double price;
    String bookImage;
    String description;
    String publishDate;
    LocalDateTime createdAt;
    boolean isActive;
    boolean isApproved;
    Integer sellerId;
    double discountPercentage;
//    Integer storeId;
//    Integer wishListId;
}