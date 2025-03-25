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
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer userId;

    String username;
    String password;
    String fullName;
    String phoneNumber;
    String email;
    String userImage;
    LocalDateTime createdAt;
    boolean isActive;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    UserRole userRole;
}

