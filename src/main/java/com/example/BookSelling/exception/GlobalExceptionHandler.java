package com.example.BookSelling.exception;

import com.example.BookSelling.dto.response.ResponseData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ResponseData<?>> handlingRuntimeException() {
        ResponseData<?> responseData = new ResponseData<>();

        responseData.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getErrorCode());
        responseData.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getErrorMessage());

        return ResponseEntity.badRequest()
                .header("Content-Type", "application/json")
                .body(responseData);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ResponseData<?>> handleAppException(final AppException e) {
        ErrorCode errorCode = (e.getErrorCode() != null)
                ? e.getErrorCode()
                : ErrorCode.UNCATEGORIZED_EXCEPTION;
        ResponseData<?> responseData = new ResponseData<>();
        responseData.setCode(errorCode.getErrorCode());
        responseData.setMessage(errorCode.getErrorMessage());
        return ResponseEntity.status(errorCode.getHttpStatusCode()).body(responseData);
    }

}
