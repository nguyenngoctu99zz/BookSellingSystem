package com.example.BookSelling.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum PaymentMethod {
   @JsonProperty("COD") COD,
   @JsonProperty("VNPAY")VNPAY
}
