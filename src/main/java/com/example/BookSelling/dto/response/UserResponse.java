package com.example.BookSelling.dto.response;

import com.example.BookSelling.common.UserRole;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    Integer userId;
    String username;
    String fullName;
    String phoneNumber;
    String email;
    String userImage;
    LocalDateTime createdAt;
    boolean isActive;
    UserRole userRole;
}
