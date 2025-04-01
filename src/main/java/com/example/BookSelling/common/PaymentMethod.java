package com.example.BookSelling.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum PaymentMethod {
   @JsonProperty("cod") COD,
   @JsonProperty("vnpay")VNPAY
}
