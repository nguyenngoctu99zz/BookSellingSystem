package com.example.BookSelling.controller.userController;

import com.example.BookSelling.dto.response.BookResponse;
import com.example.BookSelling.dto.response.NewBookByPageResponse;
import com.example.BookSelling.dto.response.ResponseData;
import com.example.BookSelling.service.BookService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookController {

    BookService bookService;

    @GetMapping("")
    public ResponseData<List<BookResponse>> getAllBooks() {
        List<BookResponse> books = bookService.getAllBooks();
        return ResponseData.<List<BookResponse>>builder()
                .code(200)
                .message("Success")
                .data(books)
                .build();
    }
    @GetMapping("/new-book")
    public ResponseEntity<NewBookByPageResponse> showNewBooks(@RequestParam(name = "pageNumber") int pageNumber){
       return ResponseEntity.ok().header("Content-Type","application/json").body(bookService.newBookPageHandler(pageNumber,5));
    }

    @GetMapping("/best-review")
    public ResponseEntity<NewBookByPageResponse> showBestReviewBook(@RequestParam(name ="pageNumber") int pageNumber){
        return ResponseEntity.ok().header("Content-Type", "application/json").body(bookService.bestReviewBookHandler(pageNumber,5));
    }

    @GetMapping("/best-discount")
    public ResponseEntity<NewBookByPageResponse> showBestDiscountBook(@RequestParam(name ="pageNumber") int pageNumber){
        return ResponseEntity.ok().header("Content-Type", "application/json").body(bookService.bestDiscountBookHandler(pageNumber,5));
    }
}
