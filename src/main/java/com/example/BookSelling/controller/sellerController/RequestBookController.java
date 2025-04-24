package com.example.BookSelling.controller.sellerController;


import com.example.BookSelling.dto.request.BookRequest;
import com.example.BookSelling.dto.response.BookResponse;
import com.example.BookSelling.dto.response.ResponseData;
import com.example.BookSelling.model.Book;
import com.example.BookSelling.service.BookService;
import com.example.BookSelling.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
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

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseData<BookResponse> addNewBook(@ModelAttribute BookRequest request) {
        Integer currentUserId = userService.getCurrentUserId();
        BookResponse newBook = bookService.addNewBook(currentUserId, request);
        return ResponseData.<BookResponse>builder()
                .code(201)
                .message("Book request created successfully")
                .data(newBook)
                .build();
    }

    @GetMapping("/my-requests")
    public ResponseData<List<BookResponse>> getMyRequestBooks() {
        Integer currentUserId = userService.getCurrentUserId();
        List<BookResponse> books = bookService.getMyRequestBooks(currentUserId);
        return ResponseData.<List<BookResponse>>builder()
                .code(200)
                .message("Success")
                .data(books)
                .build();
    }


    @DeleteMapping("/{bookId}")
    public ResponseEntity<?> deleteRequestBook(@PathVariable Integer bookId) {
        Integer currentUserId = userService.getCurrentUserId();
        bookService.deleteRequestBook(currentUserId, bookId);
        return ResponseEntity.ok()
                .body(ResponseData.builder()
                        .code(200)
                        .message("Book request deleted successfully")
                        .build());
    }

}
