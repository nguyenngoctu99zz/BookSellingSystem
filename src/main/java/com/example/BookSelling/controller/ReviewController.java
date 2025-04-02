package com.example.BookSelling.controller;

import com.example.BookSelling.dto.request.ReviewRequest;
import com.example.BookSelling.dto.request.ReviewUpdateRequest;
import com.example.BookSelling.dto.response.BookReviewResponse;
import com.example.BookSelling.dto.response.ResponseData;
import com.example.BookSelling.dto.response.ReviewResponse;
import com.example.BookSelling.service.ReviewService;
import com.example.BookSelling.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewController {
    ReviewService reviewService;
    UserService userService;

    @PostMapping("")
    public ReviewResponse addReview(@RequestBody ReviewRequest request) {
        var user = userService.getCurrentUserId();
       return reviewService.createReview(user,request);
    }
    @GetMapping("/{reviewId}")
    public ResponseData<ReviewResponse> getReviewById(@PathVariable int reviewId) {
        return ResponseData.<ReviewResponse>builder()
                .code(200)
                .message("Success")
                .data(reviewService.getReviewById(reviewId))
                .build();
    }
    @GetMapping("")
    public ResponseData<List<ReviewResponse>> getAllReviews() {
        return ResponseData.<List<ReviewResponse>>builder()
                .code(200)
                .message("Success")
                .data(reviewService.getAllReviews())
                .build();
    }
    @GetMapping("/book/{bookId}")
    public ResponseData<BookReviewResponse> getReviewsByBookId(@PathVariable int bookId) {
        var reviews = reviewService.getReviewsByBookId(bookId);
        return ResponseData.<BookReviewResponse>builder()
            .code(200)
            .data(reviews)
            .message("Success")
            .build();
    }

    @PutMapping("/book/{reviewId}")
    public ReviewResponse updateReview(@PathVariable int reviewId, @RequestBody ReviewUpdateRequest request) {
        return reviewService.updateReview(reviewId, request);
    }
}
