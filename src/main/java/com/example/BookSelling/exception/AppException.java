package com.example.BookSelling.exception;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppException extends RuntimeException {
    ErrorCode errorCode;

    public AppException(String notEnoughQuantityAvailable) {
    }
}

