package com.example.BookSelling.service;

import com.example.BookSelling.dto.request.BookRequest;
import com.example.BookSelling.model.Book;

import java.util.List;

public interface BookService {

    Book addNewBook(Integer userId, BookRequest request);
    Book updateBook(Integer bookId, BookRequest request);
    Book changeBookStatus(Integer bookId, boolean isActive);
    List<Book> getAllBooks();
    Book getBookById(Integer bookId);



    List<Book> getMyShopBooks(Integer userId);

    List<Book> getMyRequestBooks(Integer userId);
}
