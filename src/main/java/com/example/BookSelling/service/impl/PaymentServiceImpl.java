package com.example.BookSelling.service.impl;

import com.example.BookSelling.common.OrderStatus;
import com.example.BookSelling.common.PaymentMethod;
import com.example.BookSelling.common.PaymentStatus;
import com.example.BookSelling.dto.request.PaymentSubmitRequest;
import com.example.BookSelling.dto.response.PaymentResponse;
import com.example.BookSelling.exception.AppException;
import com.example.BookSelling.exception.ErrorCode;
import com.example.BookSelling.model.Book;
import com.example.BookSelling.model.OrderItem;
import com.example.BookSelling.model.Payment;
import com.example.BookSelling.repository.BookRepository;
import com.example.BookSelling.repository.OrderItemRepository;
import com.example.BookSelling.repository.PaymentRepository;
import com.example.BookSelling.repository.UserRepository;
import com.example.BookSelling.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentServiceImpl implements PaymentService {

    PaymentRepository paymentRepository;
    VNPayService vnPayService;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    OrderItemRepository orderItemRepository;

    @Override
    public ResponseEntity<PaymentResponse> paymentHandler(PaymentSubmitRequest paymentSubmitRequest, HttpServletRequest request) {
        String orderInfo = paymentSubmitRequest.getOrderInfo();
        Book book = bookRepository.findById(Integer.parseInt(orderInfo.split(",")[1].split(":")[1])).get();
        if (book.getUser().getUserId().equals(Integer.parseInt(orderInfo.split(",")[0].split(":")[1]))) {
            throw new AppException(ErrorCode.ORDER_INVALID);
        }
        if(paymentSubmitRequest.getPaymentMethod() == PaymentMethod.COD){

            Payment payment = Payment.builder().paymentAmount(paymentSubmitRequest.getAmount())
                    .paymentMethod(PaymentMethod.COD)
                    .paymentStatus(PaymentStatus.PENDING)
                    .payDate(new Date().toString()).orderInfo(paymentSubmitRequest.getOrderInfo())
                    .build();
            paymentRepository.saveAndFlush(payment);
            OrderItem orderItem = OrderItem.builder().orderStatus(OrderStatus.PENDING)
                    .book(book)
                    .user(userRepository.findById(Integer.parseInt(orderInfo.split(",")[0].split(":")[1])).get())
                    .payment(payment)
                    .quantity(Integer.parseInt(orderInfo.split(",")[2].split(":")[1]))
                    .orderDate(LocalDateTime.now())
                    .totalPrice(paymentSubmitRequest.getAmount()).build();
            orderItemRepository.save(orderItem);
            book.setQuantity(book.getQuantity()-Integer.parseInt(orderInfo.split(",")[2].split(":")[1]));
            bookRepository.save(book);
            return ResponseEntity.ok().body(new PaymentResponse("/book-detail/success"));
        }

        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/api/v1/payment";
        String vnpayUrl = vnPayService.createOrder(request, (int) paymentSubmitRequest.getAmount(), paymentSubmitRequest.getOrderInfo(), baseUrl);
        return ResponseEntity.ok().body(new PaymentResponse(vnpayUrl));
    }
}
