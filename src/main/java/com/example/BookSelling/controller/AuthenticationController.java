package com.example.BookSelling.controller;

import com.example.BookSelling.dto.request.AuthenticationRequest;
import com.example.BookSelling.dto.request.LogoutRequest;
import com.example.BookSelling.dto.response.AuthenticationResponse;
import com.example.BookSelling.dto.response.LogoutResponse;
import com.example.BookSelling.dto.response.RefreshResponse;
import com.example.BookSelling.dto.response.ResponseData;
import com.example.BookSelling.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

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
    @PostMapping("log-out")
    ResponseData<LogoutResponse> logout(@RequestBody LogoutRequest request)
            throws ParseException, JOSEException {
        return ResponseData.<LogoutResponse>builder()
                .data(authenticationService.logout(request))
                .build();
    }

    @PostMapping("refresh")
    ResponseData<RefreshResponse> refresh( HttpServletRequest request, HttpServletResponse response)
            throws JOSEException, ParseException {
        return ResponseData.<RefreshResponse>builder()
                .data(authenticationService.refreshToken(request, response))
                .build();
    }


}
