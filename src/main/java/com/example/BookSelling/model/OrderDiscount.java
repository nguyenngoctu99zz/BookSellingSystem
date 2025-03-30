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
@Table(name = "order_discounts")
public class OrderDiscount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer orderDiscountId;

    @ManyToOne
    @JoinColumn(name = "orderId")
    OrderItem orderItem;

    @ManyToOne
    @JoinColumn(name = "discountId")
    Discount discount;
}