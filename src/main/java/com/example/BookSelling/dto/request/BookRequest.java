package com.example.BookSelling.dto.request;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookRequest {
    String bookTitle;
    String publisher;
    String author;
    Integer quantity;
    Double price;
    String bookImage;
    String description;
    String publishDate;
}
