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
    BookResponse approveBook(Integer bookId, Integer userId);



    List<Book> getMyShopBooks(Integer userId);

    List<Book> getMyRequestBooks(Integer userId);

    public List<Book> getNewestBookInPage(int pageNumber, int numberOfBookEachPage);
    public NewBookByPageResponse newBookPageHandler(int pageNumber, int numberOfBookEachPage);
    public List<Book> getBestReviewBook(int pageNumber,int numberOfBookEachPage);
    public NewBookByPageResponse bestReviewBookHandler(int pageNumber, int numberOfBookEachPage);
    public List<Book> getBestDiscountBook(int pageNumber, int numberOfbookEachPage);
    public NewBookByPageResponse bestDiscountBookHandler(int pageNumber, int numberOfBookEachPage);
    public SearchBookResponse searchBookHandler(String keyword);

}
