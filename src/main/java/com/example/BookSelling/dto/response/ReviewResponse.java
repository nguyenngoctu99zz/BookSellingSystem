package com.example.BookSelling.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewResponse {
    int reviewId;
    int userId;
    int bookId;
    int rating;
    String comment;
    LocalDateTime createdAt;
}
