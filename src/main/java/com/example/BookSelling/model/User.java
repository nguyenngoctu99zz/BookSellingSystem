package com.example.BookSelling.model;

import com.example.BookSelling.common.UserRole;
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
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    Integer userId;
    @Column(name = "username")
    String username;
    @Column(name = "password")
    String password;
    @Column(name = "fullName")
    String fullName;
    @Column(name = "phoneNumber")
    String phoneNumber;
    @Column(name = "email")
    String email;
    @Column(name = "userImage")
    String userImage;
    @Column(name = "createdAt")
    LocalDateTime createdAt;
    @Column(name = "isActive")
    boolean isActive;

    @Enumerated(EnumType.STRING)
    UserRole userRole;
}

