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
@Table(name = "stores")
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long storeId;

    String description;
    String avatarImage;
    String coverImage;

    @OneToOne
    @JoinColumn(name = "userId")
    User user;
}