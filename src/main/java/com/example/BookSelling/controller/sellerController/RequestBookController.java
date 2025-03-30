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
@RequestMapping("/add-book")
public class RequestBookController {

    UserService userService;
    BookService bookService;


    @PostMapping
    public ResponseEntity<Book> addNewBook(@RequestBody BookRequest request) {
        Integer currentUserId = userService.getCurrentUserId();

        Book newBook = bookService.addNewBook(currentUserId, request);
        return ResponseEntity.ok(newBook);
    }

    @GetMapping("/my-requests")
    public ResponseEntity<List<Book>> getMyRequestBooks() {
        Integer currentUserId = userService.getCurrentUserId();
        List<Book> books = bookService.getMyRequestBooks(currentUserId);
        return ResponseEntity.ok(books);
    }

}
