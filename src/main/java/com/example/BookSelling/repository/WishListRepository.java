package com.example.BookSelling.repository;

import com.example.BookSelling.model.WishList;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WishListRepository extends JpaRepository<WishList,Integer> {
    @Query(value = "SELECT w.* FROM wish_lists w " +
            "JOIN books b ON w.book_id = b.book_id " +
            "WHERE w.user_id = ?1 " +
            "AND b.is_approved = true " +
            "AND b.is_active = true",
            nativeQuery = true)
    public List<WishList> findWishListByUserId(int userId);
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM wish_lists WHERE book_id = ?1 AND user_id = ?2",nativeQuery = true)
    int deleteByBookAndUser(int bookId, int userId);
}
