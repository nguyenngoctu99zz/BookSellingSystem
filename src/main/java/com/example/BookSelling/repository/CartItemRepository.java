package com.example.BookSelling.repository;

import com.example.BookSelling.model.Book;
import com.example.BookSelling.model.CartItem;
import com.example.BookSelling.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    Optional<CartItem> findByUserAndBook(User user, Book book);
    void deleteAllByUser(User user);

    List<CartItem> findByUser(User user);
}
