package com.example.BookSelling.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(1111, "Uncategorized exception", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXISTED(1010, "User already existed", HttpStatus.BAD_REQUEST),
    PHONE_NO_EXISTED(1010, "Phone number already existed", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTED(1010, "Email already existed", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1011, "User not found", HttpStatus.BAD_REQUEST),
    REVIEW_ALREADY_EXISTS(1011, "Review already existed", HttpStatus.BAD_REQUEST),
    ROLE_NOT_FOUND(1011, "Role not found", HttpStatus.BAD_REQUEST),
    BOOK_NOT_FOUND(1011, "Book not found", HttpStatus.BAD_REQUEST),
    REVIEW_NOT_FOUND(1011, "Review not found", HttpStatus.BAD_REQUEST),
    USER_NAME_INVALID(1012, "User name must be at least 3 characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1012, "Password must be at least 6 characters", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1013, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1014, "You do not have permission", HttpStatus.FORBIDDEN),
    USER_INACTIVE(1015, "Account is locked", HttpStatus.FORBIDDEN),
    ORDER_INVALID(1016, "You can not buy your book", HttpStatus.BAD_REQUEST),
    CART_INVALID(1017, "You can not add to cart your book", HttpStatus.BAD_REQUEST),
    ;
    private final int errorCode;
    private final String errorMessage;
    private final HttpStatusCode httpStatusCode;

    ErrorCode(int errorCode, String errorMessage, HttpStatusCode httpStatusCode) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.httpStatusCode = httpStatusCode;
    }
}
