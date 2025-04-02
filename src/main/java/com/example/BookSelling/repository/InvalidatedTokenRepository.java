package com.example.BookSelling.repository;

import com.example.BookSelling.model.InvalidatedToken;
import com.example.BookSelling.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, Integer> {
    Optional<InvalidatedToken> findByToken(String token);

    void deleteByUser(User user);
    boolean existsByToken(String token);
}
