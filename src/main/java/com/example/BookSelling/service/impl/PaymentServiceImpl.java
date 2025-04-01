package com.example.BookSelling.service.impl;

import com.example.BookSelling.common.PaymentMethod;
import com.example.BookSelling.common.PaymentStatus;
import com.example.BookSelling.dto.request.PaymentSubmitRequest;
import com.example.BookSelling.dto.response.PaymentResponse;
import com.example.BookSelling.model.Payment;
import com.example.BookSelling.repository.PaymentRepository;
import com.example.BookSelling.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    VNPayService vnPayService;
    @Override
    public ResponseEntity<PaymentResponse> paymentHandler(PaymentSubmitRequest paymentSubmitRequest, HttpServletRequest request) {
        if(paymentSubmitRequest.getPaymentMethod() == PaymentMethod.COD){
            Payment payment = Payment.builder().paymentAmount(paymentSubmitRequest.getAmount())
                    .paymentMethod(PaymentMethod.COD)
                    .paymentStatus(PaymentStatus.PENDING)
                    .payDate(new Date().toString()).orderInfo(paymentSubmitRequest.getOrderInfo())
                    .build();
            paymentRepository.saveAndFlush(payment);
            return ResponseEntity.ok().body(new PaymentResponse("success"));
    }

        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/api/v1/api/payment";
        String vnpayUrl = vnPayService.createOrder(request, paymentSubmitRequest.getAmount(), paymentSubmitRequest.getOrderInfo(), baseUrl);
        return ResponseEntity.ok().body(new PaymentResponse("redirect:" + vnpayUrl));
    }
}
