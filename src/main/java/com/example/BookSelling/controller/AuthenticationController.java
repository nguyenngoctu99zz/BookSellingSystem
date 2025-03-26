package com.example.BookSelling.controller;

import com.example.BookSelling.dto.request.AuthenticationRequest;
import com.example.BookSelling.dto.response.AuthenticationResponse;
import com.example.BookSelling.dto.response.ResponseData;
import com.example.BookSelling.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/log-in")
    public ResponseData<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseData.<AuthenticationResponse>builder()
                .data(authenticationService.authenticate(request))
                .message("Successfully logged in")
                .build();
    }
}
