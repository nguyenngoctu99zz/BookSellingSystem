package com.example.BookSelling.service;

import com.example.BookSelling.dto.request.AuthenticationRequest;
import com.example.BookSelling.dto.request.LogoutRequest;
import com.example.BookSelling.dto.request.RefreshRequest;
import com.example.BookSelling.dto.response.AuthenticationResponse;
import com.example.BookSelling.dto.response.LogoutResponse;
import com.example.BookSelling.dto.response.RefreshResponse;
import com.example.BookSelling.model.User;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request);
    String generateToken(User user);
    LogoutResponse logout(LogoutRequest request) throws ParseException, JOSEException;
    RefreshResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException;
}
