package com.example.BookSelling.service.impl;

import com.example.BookSelling.dto.request.BookRequest;
import com.example.BookSelling.dto.response.BookResponse;
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
import java.util.stream.Collectors;


@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    BookRepository bookRepository;
    UserRepository userRepository;


    @Override
    public BookResponse addNewBook(Integer userId, BookRequest request) {
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

        Book savedBook = bookRepository.save(book);
        return mapToBookResponse(savedBook);
    }

    @Override
    public BookResponse updateBook(Integer bookId, BookRequest request) {
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

        Book updatedBook = bookRepository.save(existingBook);
        return mapToBookResponse(updatedBook);
    }

    @Override
    public BookResponse changeBookStatus(Integer bookId, boolean isActive) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        book.setActive(isActive);
        Book updatedBook = bookRepository.save(book);
        return mapToBookResponse(updatedBook);
    }

    @Override
    public List<BookResponse> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .map(this::mapToBookResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BookResponse getBookById(Integer bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        return mapToBookResponse(book);
    }

    @Override
    public List<BookResponse> getMyShopBooks(Integer userId) {
        List<Book> books = bookRepository.findByUserUserIdAndIsApprovedTrue(userId);
        return books.stream()
                .map(this::mapToBookResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookResponse> getMyRequestBooks(Integer userId) {
        List<Book> books = bookRepository.findByUserUserIdAndIsApprovedFalse(userId);
        return books.stream()
                .map(this::mapToBookResponse)
                .collect(Collectors.toList());
    }

    private BookResponse mapToBookResponse(Book book) {
        return BookResponse.builder()
                .bookId(book.getBookId())
                .bookTitle(book.getBookTitle())
                .publisher(book.getPublisher())
                .author(book.getAuthor())
                .quantity(book.getQuantity())
                .price(book.getPrice())
                .bookImage(book.getBookImage())
                .description(book.getDescription())
                .publishDate(book.getPublishDate())
                .createdAt(book.getCreatedAt())
                .isActive(book.isActive())
                .isApproved(book.isApproved())
                .sellerId(book.getUser() != null ? book.getUser().getUserId() : null)
//                .storeId(book.getStore() != null ? book.getStore().getStoreId() : null)
//                .wishListId(book.getWishList() != null ? book.getWishList().getWishListId() : null)
                .build();
    }

}
