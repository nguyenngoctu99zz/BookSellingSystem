package com.example.BookSelling.controller.sellerController;

import com.example.BookSelling.dto.request.BookRequest;
import com.example.BookSelling.dto.response.BookResponse;
import com.example.BookSelling.dto.response.ResponseData;
import com.example.BookSelling.service.BookService;
import com.example.BookSelling.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
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
    public ResponseData<List<BookResponse>> getMyShopBooks() {
        Integer currentUserId = userService.getCurrentUserId();
        List<BookResponse> books = bookService.getMyShopBooks(currentUserId);
        return ResponseData.<List<BookResponse>>builder()
                .code(200)
                .message("Success")
                .data(books)
                .build();
    }

    @PutMapping(value = "/{bookId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseData<BookResponse> updateBook(
            @PathVariable Integer bookId,
            @ModelAttribute BookRequest request) {
        BookResponse updatedBook = bookService.updateBook(bookId, request);
        return ResponseData.<BookResponse>builder()
                .code(200)
                .message("Book updated successfully")
                .data(updatedBook)
                .build();
    }

    @PatchMapping("/{bookId}/status")
    public ResponseData<BookResponse> changeBookStatus(
            @PathVariable Integer bookId,
            @RequestParam boolean isActive) {
        BookResponse book = bookService.changeBookStatus(bookId, isActive);
        return ResponseData.<BookResponse>builder()
                .code(200)
                .message("Book status changed successfully")
                .data(book)
                .build();
    }

}
