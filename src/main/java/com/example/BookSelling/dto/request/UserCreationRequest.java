package com.example.BookSelling.dto.request;

import com.example.BookSelling.common.UserRole;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    Integer id;
    String username;
    String password;
    String fullName;
    String phoneNumber;
    String email;
//    String userImage;
    LocalDateTime createdAt;
    boolean isActive;
    UserRole userRole;
}
