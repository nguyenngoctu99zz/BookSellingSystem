package com.example.BookSelling.service.impl;

import com.example.BookSelling.common.UserRole;
import com.example.BookSelling.dto.request.BookRequest;

import com.example.BookSelling.dto.response.CategoryResponse;
import com.example.BookSelling.dto.response.NewBookByPageResponse;
import com.example.BookSelling.dto.response.SearchBookResponse;

import com.example.BookSelling.dto.response.BookResponse;

import com.example.BookSelling.exception.AppException;
import com.example.BookSelling.exception.ErrorCode;
import com.example.BookSelling.model.Book;
import com.example.BookSelling.model.BookCategory;
import com.example.BookSelling.model.Category;
import com.example.BookSelling.model.User;
import com.example.BookSelling.repository.BookCategoryRepository;
import com.example.BookSelling.repository.BookRepository;
import com.example.BookSelling.repository.CategoryRepository;
import com.example.BookSelling.repository.UserRepository;
import com.example.BookSelling.service.BookService;
import com.example.BookSelling.service.ImageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    ImageService imageService;
    CategoryRepository categoryRepository;
    BookCategoryRepository bookCategoryRepository;

    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    @Override
    public BookResponse addNewBook(Integer userId, BookRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        String imageName = null;
        if (request.getBookImage() != null && !request.getBookImage().isEmpty()) {
            imageName = imageService.uploadImage(request.getBookImage(), null);
        }

        Book book = Book.builder()
                .bookTitle(request.getBookTitle())
                .publisher(request.getPublisher())
                .author(request.getAuthor())
                .quantity(request.getQuantity())
                .price(request.getPrice())
                .bookImage(imageName)
                .description(request.getDescription())
                .publishDate(request.getPublishDate())
                .createdAt(LocalDateTime.now())
                .isActive(false)
                .isApproved(false)
                .user(user)
                .build();

        Book savedBook = bookRepository.save(book);
        if (request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()) {
            List<BookCategory> bookCategories = request.getCategoryIds().stream()
                    .map(catId -> {
                        Category category = categoryRepository.findById(catId)
                                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + catId));
                        return BookCategory.builder()
                                .book(savedBook)
                                .category(category)
                                .build();
                    })
                    .toList();
            bookCategoryRepository.saveAll(bookCategories);
        }
        return mapToBookResponse(savedBook);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    @Override
    public BookResponse updateBook(Integer bookId, BookRequest request) {
        Book existingBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        String imageName = existingBook.getBookImage();
        if (request.getBookImage() != null && !request.getBookImage().isEmpty()) {
            try {
                if (imageName != null) {
                    Path oldImagePath = Paths.get("uploads", imageName);
                    Files.deleteIfExists(oldImagePath);
                }

                imageName = imageService.uploadImage(request.getBookImage(), null);
            } catch (IOException e) {
                throw new RuntimeException("Failed to update image: " + e.getMessage());
            }
        }
        existingBook.setBookTitle(request.getBookTitle());
        existingBook.setPublisher(request.getPublisher());
        existingBook.setAuthor(request.getAuthor());
        existingBook.setQuantity(request.getQuantity());
        existingBook.setPrice(request.getPrice());
        existingBook.setBookImage(imageName);
        existingBook.setDescription(request.getDescription());
        existingBook.setPublishDate(request.getPublishDate());

        Book updatedBook = bookRepository.save(existingBook);
        return mapToBookResponse(updatedBook);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    @Override
    public BookResponse changeBookStatus(Integer bookId, boolean isActive) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));
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
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));
        return mapToBookResponse(book);
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    public List<BookResponse> getMyShopBooks(Integer userId) {
        List<Book> books = bookRepository.findByUserUserIdAndIsApprovedTrue(userId);
        return books.stream()
                .map(this::mapToBookResponse)
                .collect(Collectors.toList());
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    public List<BookResponse> getMyRequestBooks(Integer userId) {
        List<Book> books = bookRepository.findByUserUserIdAndIsApprovedFalse(userId);
        return books.stream()
                .map(this::mapToBookResponse)
                .collect(Collectors.toList());
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<BookResponse> getAllPendingBooks() {
        List<Book> books = bookRepository.findByIsApprovedFalse();
        return books.stream()
                .map(this::mapToBookResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> getNewestBookInPage(int pageNumber, int numberOfBookEachPage){
        return bookRepository.getBookByNewest(numberOfBookEachPage,(pageNumber-1)*numberOfBookEachPage);
    }

    @Override
    public NewBookByPageResponse newBookPageHandler(int pageNumber, int numberOfBookEachPage){
        int totalNumberOfPage =Math.ceilDiv(getAllBooks().size(),numberOfBookEachPage);
        List<Book> books = getNewestBookInPage(pageNumber,numberOfBookEachPage);
        List bookResponse = books.stream()
                .map(this::mapToBookResponse)
                .collect(Collectors.toList());
        return new NewBookByPageResponse(bookResponse,totalNumberOfPage);
    }

    @Override
    public List<Book> getBestReviewBook(int pageNumber, int numberOfBookEachPage) {
        return bookRepository.getBookByBestReview(numberOfBookEachPage,(pageNumber-1)*numberOfBookEachPage);
    }

    @Override
    public NewBookByPageResponse bestReviewBookHandler(int pageNumber, int numberOfBookEachPage) {
        int totalNumberOfPage =Math.ceilDiv(getAllBooks().size(),numberOfBookEachPage);
        List<Book> books = getBestReviewBook(pageNumber,numberOfBookEachPage);
        List bookResponse = books.stream()
                .map(this::mapToBookResponse)
                .collect(Collectors.toList());
        return new NewBookByPageResponse(bookResponse,totalNumberOfPage);
    }

    @Override
    public List<Book> getBestDiscountBook(int pageNumber, int numberOfbookEachPage) {
        return bookRepository.getBookByBestDiscount(numberOfbookEachPage,(pageNumber-1)*numberOfbookEachPage);
    }

    @Override
    public NewBookByPageResponse bestDiscountBookHandler(int pageNumber, int numberOfBookEachPage) {
        int totalNumberOfPage =Math.ceilDiv(getAllBooks().size(),numberOfBookEachPage);
        List<Book> books = getBestDiscountBook(pageNumber,numberOfBookEachPage);
        List bookResponse = books.stream()
                .map(this::mapToBookResponse)
                .collect(Collectors.toList());
        return new NewBookByPageResponse(bookResponse,totalNumberOfPage);
    }

    @Override
    public SearchBookResponse searchBookHandler(String keyword) {
        List<Book> books = bookRepository.searchBookByKeyWord(keyword);
        List bookResponse = books.stream()
                .map(this::mapToBookResponse)
                .collect(Collectors.toList());
        return new SearchBookResponse(bookResponse, books.size());
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public BookResponse approveBook(Integer bookId, Integer adminId) {
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (!admin.getUserRole().equals(UserRole.ADMIN)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));

        book.setApproved(true);
        book.setActive(true);

        Book approvedBook = bookRepository.save(book);
        return mapToBookResponse(approvedBook);
    }
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public BookResponse rejectBook(Integer bookId, Integer adminId) {
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (!admin.getUserRole().equals(UserRole.ADMIN)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));

        bookRepository.delete(book);
        return mapToBookResponse(book);
    }
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteBook(Integer bookId) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));
        bookRepository.delete(book);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    @Override
    public void deleteRequestBook(Integer userId, Integer bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));

        if (!book.getUser().getUserId().equals(userId)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        // not allow approved booked getting deleted
        if (book.isApproved()) {
            throw new AppException("BOOK_ALREADY_APPROVED");
        }

        bookRepository.delete(book);
    }

    public List<BookResponse> filterBooksByCategories(List<Integer> categoryIds) {
        List<Book> books = bookRepository.findBooksByCategoryIds(categoryIds);
        return books.stream().map(this::mapToBookResponse).toList();
    }

    private BookResponse mapToBookResponse(Book book) {
        List<CategoryResponse> categoryInfos = bookCategoryRepository.findByBook(book).stream()
                .map(bc -> {
                    Category c = bc.getCategory();
                    return CategoryResponse.builder()
                            .categoryId(c.getCategoryID())
                            .categoryName(c.getCategoryName())
                            .description(c.getDescription())
                            .build();
                })
                .toList();

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
                .discountPercentage(book.getDiscountPercentage())
                .categories(categoryInfos)
                .build();

    }

}
