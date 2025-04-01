package com.example.BookSelling.controller.userController;

import com.example.BookSelling.dto.response.SearchBookResponse;
import com.example.BookSelling.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SearchController {
    @Autowired
    BookService bookService;
    @GetMapping("/search")
    public ResponseEntity<SearchBookResponse> searchBook(@RequestParam String keyword){
       return ResponseEntity.ok().body(bookService.searchBookHandler(keyword));
    }
}
