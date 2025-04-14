package com.example.BookSelling.service.impl;

import com.example.BookSelling.dto.request.ReviewRequest;
import com.example.BookSelling.dto.request.ReviewUpdateRequest;
import com.example.BookSelling.dto.response.BookReviewResponse;
import com.example.BookSelling.dto.response.ReviewResponse;
import com.example.BookSelling.exception.AppException;
import com.example.BookSelling.exception.ErrorCode;
import com.example.BookSelling.model.Review;
import com.example.BookSelling.repository.BookRepository;
import com.example.BookSelling.repository.ReviewRepository;
import com.example.BookSelling.repository.UserRepository;
import com.example.BookSelling.service.ReviewService;
import com.example.BookSelling.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewServiceImpl implements ReviewService {

    UserRepository userRepository;
    ReviewRepository reviewRepository;
    BookRepository bookRepository;
    UserService userService;

    @Override
    public ReviewResponse createReview(int userId, ReviewRequest request) {
        var user = userRepository.findById(userId)
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND));
        var book = bookRepository.findById(request.getBookId())
                .orElseThrow(()-> new AppException(ErrorCode.BOOK_NOT_FOUND));
        Review review = Review.builder()
                .user(user)
                .book(book)
                .ratings(request.getRating())
                .comment(request.getComment())
                .createdAt(LocalDateTime.now())
                .build();
        return mapToReviewResponse(reviewRepository.save(review));
    }

    @Override
    public ReviewResponse updateReview(int reviewId, ReviewUpdateRequest request) {

        var review = reviewRepository.findById(reviewId)
                .orElseThrow(()-> new AppException(ErrorCode.REVIEW_NOT_FOUND));
        if (review.getBook().getBookId() != request.getBookId()) {
            throw new AppException(ErrorCode.BOOK_NOT_FOUND);
        }
       review.setComment(request.getComment());
       review.setRatings(request.getRating());
       review.setCreatedAt(LocalDateTime.now());
       return mapToReviewResponse(reviewRepository.save(review));
    }

    @Override
    public List<ReviewResponse> getAllReviews() {
        return reviewRepository.findAll()
                .stream().map(this::mapToReviewResponse)
                .toList();
    }

    @Override
    public ReviewResponse getReviewById(int reviewId) {
        var review = reviewRepository.findById(reviewId)
                .orElseThrow(()-> new AppException(ErrorCode.REVIEW_NOT_FOUND));

        return ReviewResponse.builder()
                .userId(review.getReviewId())
                .bookId(review.getBook().getBookId())
                .reviewId(review.getReviewId())
                .rating(review.getRatings())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteReview(int reviewId) {
        var review = reviewRepository.findById(reviewId)
                .orElseThrow(()-> new AppException(ErrorCode.REVIEW_NOT_FOUND));
        var user = userService.getCurrentUserId();
        if(!review.getUser().getUserId().equals(user)){
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        reviewRepository.deleteById(reviewId);
    }

    @Override
    public BookReviewResponse getReviewsByBookId(Integer bookId) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(()-> new AppException(ErrorCode.BOOK_NOT_FOUND));

        var reviews = reviewRepository.findByBook(book);
        double averageRating = getAverageRating(bookId);
        var reviewResponses = reviews.stream()
                .map(this::mapToReviewResponse)
                .toList();

        return BookReviewResponse.builder()
                .averageRating(averageRating)
                .reviews(reviewResponses)
                .build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ReviewResponse getReviewsByUserId(Integer userId) {
        return null;
    }


    private Double getAverageRating(Integer bookId) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));

        var reviews = reviewRepository.findByBook(book);

        return reviews.stream()
                .mapToInt(Review::getRatings)
                .average()
                .orElse(0.0);
    }

    private ReviewResponse mapToReviewResponse(Review review) {
        return ReviewResponse.builder()
                .reviewId(review.getReviewId())
                .rating(review.getRatings())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .userId(review.getUser().getUserId())
                .bookId(review.getBook().getBookId())
                .build();
    }
}
