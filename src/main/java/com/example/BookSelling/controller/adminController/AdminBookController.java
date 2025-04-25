package com.example.BookSelling.controller.adminController;

import com.example.BookSelling.dto.response.BookResponse;
import com.example.BookSelling.dto.response.ResponseData;
import com.example.BookSelling.service.BookService;
import com.example.BookSelling.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/admin/books")
public class AdminBookController {

    UserService userService;
    BookService bookService;

    @PutMapping("/{bookId}/approve")
    public ResponseData<BookResponse> approveBook(@PathVariable int bookId) {
        int adminId = userService.getCurrentUserId();
        BookResponse approvedBook = bookService.approveBook(bookId, adminId);
        return ResponseData.<BookResponse>builder()
                .code(200)
                .message("Book approved successfully")
                .data(approvedBook)
                .build();
    }
    @DeleteMapping("/{bookId}/reject")
    public ResponseData<BookResponse> rejectBook(@PathVariable int bookId) {
        int adminId = userService.getCurrentUserId();
        BookResponse rejectedBook = bookService.rejectBook(bookId, adminId);
        return ResponseData.<BookResponse>builder()
                .code(200)
                .message("Book rejected successfully")
                .data(rejectedBook)
                .build();
    }
    @GetMapping("/pending")
    public ResponseData<List<BookResponse>> getAllPendingBooks() {
        List<BookResponse> pendingBooks = bookService.getAllPendingBooks();
        return ResponseData.<List<BookResponse>>builder()
                .code(200)
                .message("All pending book requests retrieved successfully")
                .data(pendingBooks)
                .build();
    }
    @DeleteMapping("/delete/{bookId}")
    public void deleteBook(@PathVariable int bookId) {
        bookService.deleteBook(bookId);
    }
}

