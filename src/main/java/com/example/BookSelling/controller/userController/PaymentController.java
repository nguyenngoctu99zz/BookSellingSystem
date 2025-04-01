package com.example.BookSelling.controller.userController;
import com.example.BookSelling.common.PaymentMethod;
import com.example.BookSelling.common.PaymentStatus;
import com.example.BookSelling.dto.request.PaymentSubmitRequest;
import com.example.BookSelling.dto.response.PaymentResponse;
import com.example.BookSelling.model.Payment;
import com.example.BookSelling.repository.PaymentRepository;
import com.example.BookSelling.service.PaymentService;
import com.example.BookSelling.service.impl.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private VNPayService vnPayService;
    @Autowired
    private PaymentService paymentService;
    @PostMapping("/submitOrder")
    public ResponseEntity<PaymentResponse> submidOrder(@RequestBody PaymentSubmitRequest paymentSubmitRequest, HttpServletRequest request){
             return paymentService.paymentHandler(paymentSubmitRequest,request);
    }

    @GetMapping("/vnpay-payment-return")
    public String paymentCompleted(HttpServletRequest request){
        int paymentStatus =vnPayService.orderReturn(request);
        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");
        Payment payment = Payment.builder().paymentAmount(Double.parseDouble(totalPrice))
                .paymentMethod(PaymentMethod.VNPAY)
                .paymentStatus(paymentStatus == 1 ? PaymentStatus.SUCCEED : PaymentStatus.FAILED)
                .orderInfo(orderInfo)
                .payDate(paymentTime).build();
        paymentRepository.saveAndFlush(payment);
        return paymentStatus == 1 ? "ordersuccess" : "orderfail";
    }
}