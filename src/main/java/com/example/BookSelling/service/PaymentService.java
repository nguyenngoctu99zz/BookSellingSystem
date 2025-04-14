package com.example.BookSelling.service;

import com.example.BookSelling.dto.request.PaymentSubmitRequest;
import com.example.BookSelling.dto.response.PaymentResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface PaymentService {
    ResponseEntity<PaymentResponse> paymentHandler(PaymentSubmitRequest paymentSubmitRequest, HttpServletRequest request);
}
