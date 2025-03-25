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
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer cartItemId;

    Integer quantity;

    @ManyToOne
    @JoinColumn(name = "cartId")
    Cart cart;

    @OneToOne
    @JoinColumn(name = "bookId")
    Book book;
}