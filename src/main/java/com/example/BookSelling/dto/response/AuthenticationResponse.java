package com.example.BookSelling.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationResponse {
    int userId;
    String accessToken;
    String refreshToken;
    boolean authenticated;
}
