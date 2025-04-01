package com.example.BookSelling.service.impl;

import com.example.BookSelling.dto.request.BookRequest;
import com.example.BookSelling.dto.response.NewBookByPageResponse;
import com.example.BookSelling.dto.response.SearchBookResponse;
import com.example.BookSelling.model.Book;
import com.example.BookSelling.model.User;
import com.example.BookSelling.repository.BookRepository;
import com.example.BookSelling.repository.UserRepository;
import com.example.BookSelling.service.BookService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Override
    public Book addNewBook(Integer userId, BookRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Book book = Book.builder()
                .bookTitle(request.getBookTitle())
                .publisher(request.getPublisher())
                .author(request.getAuthor())
                .quantity(request.getQuantity())
                .price(request.getPrice())
                .bookImage(request.getBookImage())
                .description(request.getDescription())
                .publishDate(request.getPublishDate())
                .createdAt(LocalDateTime.now())
                .isActive(false)
                .isApproved(false)
                .user(user)
                .build();

        return bookRepository.save(book);
    }

    @Override
    public Book updateBook(Integer bookId, BookRequest request) {
        Book existingBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        existingBook.setBookTitle(request.getBookTitle());
        existingBook.setPublisher(request.getPublisher());
        existingBook.setAuthor(request.getAuthor());
        existingBook.setQuantity(request.getQuantity());
        existingBook.setPrice(request.getPrice());
        existingBook.setBookImage(request.getBookImage());
        existingBook.setDescription(request.getDescription());
        existingBook.setPublishDate(request.getPublishDate());

        return bookRepository.save(existingBook);
    }

    @Override
    public Book changeBookStatus(Integer bookId, boolean isActive) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        book.setActive(isActive);
        return bookRepository.save(book);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book getBookById(Integer bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));
    }

    @Override
    public List<Book> getMyShopBooks(Integer userId) {
        return bookRepository.findByUserUserIdAndIsApprovedTrue(userId);
    }

    @Override
    public List<Book> getMyRequestBooks(Integer userId) {
        return bookRepository.findByUserUserIdAndIsApprovedFalse(userId);
    }

    @Override
    public List<Book> getNewestBookInPage(int pageNumber, int numberOfBookEachPage){
        return bookRepository.getBookByNewest(numberOfBookEachPage,(pageNumber-1)*numberOfBookEachPage);
    }

    @Override
    public NewBookByPageResponse newBookPageHandler(int pageNumber, int numberOfBookEachPage){
        int totalNumberOfPage =Math.ceilDiv(getAllBooks().size(),numberOfBookEachPage);
        List<Book> books = getNewestBookInPage(pageNumber,numberOfBookEachPage);
        return new NewBookByPageResponse(books,totalNumberOfPage);
    }

    @Override
    public List<Book> getBestReviewBook(int pageNumber, int numberOfBookEachPage) {
        return bookRepository.getBookByBestReview(numberOfBookEachPage,(pageNumber-1)*numberOfBookEachPage);
    }

    @Override
    public NewBookByPageResponse bestReviewBookHandler(int pageNumber, int numberOfBookEachPage) {
        int totalNumberOfPage =Math.ceilDiv(getAllBooks().size(),numberOfBookEachPage);
        List<Book> books = getBestReviewBook(pageNumber,numberOfBookEachPage);
        return new NewBookByPageResponse(books,totalNumberOfPage);
    }

    @Override
    public List<Book> getBestDiscountBook(int pageNumber, int numberOfbookEachPage) {
        return bookRepository.getBookByBestDiscount(numberOfbookEachPage,(pageNumber-1)*numberOfbookEachPage);
    }

    @Override
    public NewBookByPageResponse bestDiscountBookHandler(int pageNumber, int numberOfBookEachPage) {
        int totalNumberOfPage =Math.ceilDiv(getAllBooks().size(),numberOfBookEachPage);
        List<Book> books = getBestDiscountBook(pageNumber,numberOfBookEachPage);
        return new NewBookByPageResponse(books,totalNumberOfPage);
    }

    @Override
    public SearchBookResponse searchBookHandler(String keyword) {
        List<Book> books = bookRepository.searchBookByKeyWord(keyword);
        SearchBookResponse searchBookResponse = new SearchBookResponse(books,books.size());
        return searchBookResponse;
    }

}
