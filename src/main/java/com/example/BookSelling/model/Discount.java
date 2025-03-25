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
@Table(name = "discounts")
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer discountCodeId;

    String nameDiscount;
    Double disPercent;

    LocalDateTime activateDate;
    LocalDateTime expiry;


    Integer quantity;
    LocalDateTime createAt;
    boolean isActive;

}