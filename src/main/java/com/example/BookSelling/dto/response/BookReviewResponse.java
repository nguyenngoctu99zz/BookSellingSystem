package com.example.BookSelling.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookReviewResponse {
    private double averageRating;
    private List<ReviewResponse> reviews;
}
