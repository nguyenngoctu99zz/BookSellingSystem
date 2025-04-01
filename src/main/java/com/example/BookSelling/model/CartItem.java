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
@Table(name = "cart_items", uniqueConstraints = {} )
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer cartItemId;

    Integer quantity;

    Double totalPrice;

    @ManyToOne
    @JoinColumn(name = "userId")
    User user;


    @ManyToOne
    @JoinColumn(name = "bookId")
    Book book;
}