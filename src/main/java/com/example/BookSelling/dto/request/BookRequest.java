package com.example.BookSelling.dto.request;


import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

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
    String description;
    String publishDate;
    MultipartFile bookImage;
}
