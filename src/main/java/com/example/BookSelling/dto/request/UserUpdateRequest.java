package com.example.BookSelling.dto.request;

import com.example.BookSelling.common.UserRole;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    String username;
    String password;
    String fullName;
    String phoneNumber;
    String email;
    MultipartFile userImage;
    boolean isActive;
    UserRole userRole;
}
