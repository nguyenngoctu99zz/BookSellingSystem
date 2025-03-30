package com.example.BookSelling.controller.sellerController;

import com.example.BookSelling.dto.request.BookRequest;
import com.example.BookSelling.model.Book;
import com.example.BookSelling.service.BookService;
import com.example.BookSelling.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/manage-book")
public class ManageBookController {

    BookService bookService;
    UserService userService;

    @GetMapping("/my-shop")
    public ResponseEntity<List<Book>> getMyShopBooks() {
        Integer currentUserId = userService.getCurrentUserId();
        List<Book> books = bookService.getMyShopBooks(currentUserId);
        return ResponseEntity.ok(books);
    }


    @PutMapping("/{bookId}")
    public ResponseEntity<Book> updateBook(
            @PathVariable Integer bookId,
            @RequestBody BookRequest request) {
        Book updatedBook = bookService.updateBook(bookId, request);
        return ResponseEntity.ok(updatedBook);
    }

    @PatchMapping("/{bookId}/status")
    public ResponseEntity<Book> changeBookStatus(
            @PathVariable Integer bookId,
            @RequestParam boolean isActive) {
        Book book = bookService.changeBookStatus(bookId, isActive);
        return ResponseEntity.ok(book);
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<Book> getBookById(@PathVariable Integer bookId) {
        Book book = bookService.getBookById(bookId);
        return ResponseEntity.ok(book);
    }
}
