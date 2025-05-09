package com.example.BookSelling.repository;

import com.example.BookSelling.model.Book;
import com.example.BookSelling.model.Review;
import com.example.BookSelling.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByBook(Book book);
    boolean existsByUserAndBook(User user, Book book);
}
