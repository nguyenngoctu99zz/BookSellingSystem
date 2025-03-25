package com.example.BookSelling.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "book_category")
public class BookCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer book_categoryID;

    @ManyToOne
    @JoinColumn(name = "bookId")
    Book book;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    Category category;
}
