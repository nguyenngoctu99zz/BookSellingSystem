package com.example.BookSelling.service;

import com.example.BookSelling.dto.request.BookRequest;
import com.example.BookSelling.dto.response.BookResponse;
import com.example.BookSelling.model.Book;

import java.util.List;

public interface BookService {

    BookResponse addNewBook(Integer userId, BookRequest request);
    BookResponse updateBook(Integer bookId, BookRequest request);
    BookResponse changeBookStatus(Integer bookId, boolean isActive);
    List<BookResponse> getAllBooks();
    BookResponse getBookById(Integer bookId);
    List<BookResponse> getMyShopBooks(Integer userId);
    List<BookResponse> getMyRequestBooks(Integer userId);

}
