package com.example.BookSelling.service;

import com.example.BookSelling.dto.request.ReviewRequest;
import com.example.BookSelling.dto.request.ReviewUpdateRequest;
import com.example.BookSelling.dto.response.BookReviewResponse;
import com.example.BookSelling.dto.response.ReviewResponse;

import java.util.List;

public interface ReviewService {
    ReviewResponse createReview(int userId, ReviewRequest request);
    ReviewResponse updateReview(int userId, ReviewUpdateRequest request);
    List<ReviewResponse> getAllReviews();
    ReviewResponse getReviewById(int reviewId);
    void deleteReview(int reviewId);
    BookReviewResponse getReviewsByBookId(Integer bookId);
    ReviewResponse getReviewsByUserId(Integer userId);
}
