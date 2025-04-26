package com.example.BookSelling.repository;

import com.example.BookSelling.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findByUserUserIdAndIsApprovedTrue(Integer userId);
    List<Book> findByUserUserIdAndIsApprovedFalse(Integer userId);
    @Query(value = "SELECT * FROM books b WHERE b.is_active = true AND b.is_approved = true ORDER BY b.book_id DESC LIMIT ?1 OFFSET ?2",
            nativeQuery = true)
    List<Book> getBookByNewest(int numberOfBook, int offset);
    @Query(value = "SELECT b.* FROM books b " +
            "LEFT JOIN reviews r ON b.book_id = r.book_id " +
            "WHERE b.is_active = true AND b.is_approved = true " +
            "GROUP BY b.book_id " +
            "ORDER BY COALESCE(AVG(r.ratings), 0) DESC, COUNT(r.book_id) DESC " +
            "LIMIT ?1 OFFSET ?2",
            nativeQuery = true)
    List<Book> getBookByBestReview(int numberOfBook, int offset);
    @Query(value = "SELECT * FROM books b WHERE b.is_active = true AND b.is_approved = true ORDER BY b.discount_percentage DESC LIMIT ?1 OFFSET ?2",
            nativeQuery = true)
    List<Book> getBookByBestDiscount(int numberOfBook, int offset);
    @Query(value = "SELECT b.*, " +
            "(MATCH(b.book_title) AGAINST(?1 IN BOOLEAN MODE) * 3 + " +
            "MATCH(b.author) AGAINST(?1 IN BOOLEAN MODE) * 2 + " +
            "MATCH(b.description) AGAINST(?1 IN BOOLEAN MODE) * 1 + " +
            "CASE WHEN b.book_title LIKE CONCAT('%', ?1, '%') THEN 1 ELSE 0 END + " +
            "CASE WHEN b.author LIKE CONCAT('%', ?1, '%') THEN 1 ELSE 0 END + " +
            "CASE WHEN b.description LIKE CONCAT('%', ?1, '%') THEN 0.5 ELSE 0 END) AS relevance_score " +
            "FROM books b " +
            "WHERE " +
            "   (MATCH(b.book_title, b.author, b.description) AGAINST(?1 IN BOOLEAN MODE) " +
            "    OR b.book_title LIKE CONCAT('%', ?1, '%') " +
            "    OR b.author LIKE CONCAT('%', ?1, '%') " +
            "    OR b.description LIKE CONCAT('%', ?1, '%')) " +
            "   AND b.is_active = true " +
            "   AND b.is_approved = true " +
            "ORDER BY relevance_score DESC", nativeQuery = true)
    List<Book> searchBookByKeyWord(String keyword);
    //run in sql before test search:
    //ALTER TABLE books ADD FULLTEXT (author,book_title, description);
    //ALTER TABLE books ADD FULLTEXT (author);
    //ALTER TABLE books ADD FULLTEXT (book_title);
    //ALTER TABLE books ADD FULLTEXT (description);
    // BookRepository.java
    List<Book> findByIsApprovedFalse()
    ;
    @Query("SELECT DISTINCT b FROM Book b " +
            "JOIN BookCategory bc ON b.bookId = bc.book.bookId " +
            "WHERE (:categoryIds IS NULL OR bc.category.categoryID IN :categoryIds)")
    List<Book> findBooksByCategoryIds(@Param("categoryIds") List<Integer> categoryIds);

}
