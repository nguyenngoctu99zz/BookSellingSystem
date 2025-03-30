package com.example.BookSelling.repository;

import com.example.BookSelling.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findByUserUserIdAndIsApprovedTrue(Integer userId);
    List<Book> findByUserUserIdAndIsApprovedFalse(Integer userId);
}
