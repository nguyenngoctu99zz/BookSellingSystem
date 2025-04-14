package com.example.BookSelling.controller.userController;

import com.example.BookSelling.dto.response.SearchBookResponse;
import com.example.BookSelling.service.BookService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SearchController {

    BookService bookService;

    @GetMapping("/search")
    public ResponseEntity<SearchBookResponse> searchBook(@RequestParam String keyword){
       return ResponseEntity.ok().body(bookService.searchBookHandler(keyword));
    }
}
