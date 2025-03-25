package com.example.BookSelling.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer bookId;

    String bookTitle;
    String publisher;
    String author;
    Integer quantity;
    Double price;
    List<String> bookImage;
    String description;
    String publishDate;
    LocalDateTime createdAt;
    boolean isActive;

    @ManyToOne
    @JoinColumn(name = "storeId")
    Store store;

    @ManyToOne
    @JoinColumn(name = "wishListId")
    WishList wishList;
}
