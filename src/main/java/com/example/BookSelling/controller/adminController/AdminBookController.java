package com.example.BookSelling.controller.adminController;

import com.example.BookSelling.dto.response.BookResponse;
import com.example.BookSelling.dto.response.ResponseData;
import com.example.BookSelling.service.BookService;
import com.example.BookSelling.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

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
}

