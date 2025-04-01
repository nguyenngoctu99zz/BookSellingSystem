package com.example.BookSelling.dto.request;

import com.example.BookSelling.common.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentSubmitRequest {
    private PaymentMethod paymentMethod;
    private String orderInfo;
    private int amount;
}
