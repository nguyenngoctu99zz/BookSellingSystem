package com.example.BookSelling.model;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "invalidatedToken")
public class InvalidatedToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer tokenID;

    Date expiryTime;

    String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    User user;
}
