package com.example.BookSelling.service;

import com.example.BookSelling.dto.request.BookRequest;

import com.example.BookSelling.dto.response.NewBookByPageResponse;
import com.example.BookSelling.dto.response.SearchBookResponse;

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
    void deleteRequestBook(Integer userId, Integer bookId);
    BookResponse approveBook(Integer bookId, Integer userId);



    List<Book> getNewestBookInPage(int pageNumber, int numberOfBookEachPage);
    NewBookByPageResponse newBookPageHandler(int pageNumber, int numberOfBookEachPage);
    List<Book> getBestReviewBook(int pageNumber,int numberOfBookEachPage);
    NewBookByPageResponse bestReviewBookHandler(int pageNumber, int numberOfBookEachPage);
    List<Book> getBestDiscountBook(int pageNumber, int numberOfbookEachPage);
    NewBookByPageResponse bestDiscountBookHandler(int pageNumber, int numberOfBookEachPage);
    SearchBookResponse searchBookHandler(String keyword);

}
