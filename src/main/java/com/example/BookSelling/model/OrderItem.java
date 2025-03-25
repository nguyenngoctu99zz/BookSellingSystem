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
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer orderItemId;

    Integer quantity;

    @ManyToOne
    @JoinColumn(name = "orderId")
    Order order;

    @OneToOne
    @JoinColumn(name = "bookId")
    Book book;
}
