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
    String bookImage;
    String description;
    String publishDate;
    LocalDateTime createdAt;
    double discountPercentage;
    @Column(columnDefinition = "TINYINT(0)")
    boolean isActive;
    @Column(columnDefinition = "TINYINT(1)")
    boolean isApproved;

    @ManyToOne
    @JoinColumn(name = "sellerId")
    User user;



}
