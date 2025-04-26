package com.example.BookSelling.repository;

import com.example.BookSelling.model.Book;
import com.example.BookSelling.model.BookCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookCategoryRepository extends JpaRepository<BookCategory, Integer> {
    List<BookCategory> findByBook(Book book);
}
