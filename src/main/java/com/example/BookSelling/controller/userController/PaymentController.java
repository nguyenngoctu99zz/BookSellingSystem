package com.example.BookSelling.controller.userController;
import com.example.BookSelling.common.OrderStatus;
import com.example.BookSelling.common.PaymentMethod;
import com.example.BookSelling.common.PaymentStatus;
import com.example.BookSelling.dto.request.PaymentSubmitRequest;
import com.example.BookSelling.dto.response.PaymentResponse;
import com.example.BookSelling.model.Book;
import com.example.BookSelling.model.OrderItem;
import com.example.BookSelling.model.Payment;
import com.example.BookSelling.repository.BookRepository;
import com.example.BookSelling.repository.OrderItemRepository;
import com.example.BookSelling.repository.PaymentRepository;
import com.example.BookSelling.repository.UserRepository;
import com.example.BookSelling.service.PaymentService;
import com.example.BookSelling.service.impl.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Date;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentController {
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    VNPayService vnPayService;
    @Autowired
    PaymentService paymentService;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    OrderItemRepository orderItemRepository;


    @PostMapping("/submitOrder")
    public ResponseEntity<PaymentResponse> submitOrder(@RequestBody PaymentSubmitRequest paymentSubmitRequest, HttpServletRequest request){
        return paymentService.paymentHandler(paymentSubmitRequest,request);
    }

    @GetMapping("/vnpay-payment-return")
    public ResponseEntity paymentCompleted(HttpServletRequest request){
        int paymentStatus =vnPayService.orderReturn(request);
        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");
        if(paymentStatus==1) {
            Book book = bookRepository.findById(Integer.parseInt(orderInfo.split(",")[1].split(":")[1])).get();
            Payment payment = Payment.builder().paymentAmount(Double.parseDouble(totalPrice)/100)
                    .paymentMethod(PaymentMethod.VNPAY)
                    .paymentStatus(paymentStatus == 1 ? PaymentStatus.SUCCEED : PaymentStatus.FAILED)
                    .orderInfo(orderInfo)
                    .payDate(paymentTime).build();
            paymentRepository.saveAndFlush(payment);
            System.out.println(Integer.parseInt(orderInfo.split(",")[0].split(":")[1]));
            OrderItem orderItem = OrderItem.builder().orderStatus(OrderStatus.PENDING)
                    .book(book)
                    .user(userRepository.findById(Integer.parseInt(orderInfo.split(",")[0].split(":")[1])).get())
                    .payment(payment)
                    .quantity(Integer.parseInt(orderInfo.split(",")[2].split(":")[1]))
                    .orderDate(LocalDateTime.now())
                    .totalPrice(Double.parseDouble(totalPrice)/100).build();
            orderItemRepository.save(orderItem);

            book.setQuantity(book.getQuantity()-Integer.parseInt(orderInfo.split(",")[2].split(":")[1]));
            bookRepository.save(book);
            URI redirectUri = URI.create("http://localhost:5173/book-detail/success");
            return ResponseEntity.status(HttpStatus.FOUND) // 302 redirect
                    .location(redirectUri)
                    .build();

        }
        URI redirectUri = URI.create("http://localhost:5173/book-detail/fail");
        return ResponseEntity.status(HttpStatus.FOUND) // 302 redirect
                .location(redirectUri)
                .build();

    }
}