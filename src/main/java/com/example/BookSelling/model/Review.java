package com.example.BookSelling.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer reviewId;

    Integer ratings;
    String comment;
    LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "userId")
    Users users;

    @ManyToOne
    @JoinColumn(name = "bookId")
    Book book;
}