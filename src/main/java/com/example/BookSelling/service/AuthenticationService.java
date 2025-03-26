package com.example.BookSelling.service;

import com.example.BookSelling.dto.request.AuthenticationRequest;
import com.example.BookSelling.dto.response.AuthenticationResponse;
import com.example.BookSelling.model.User;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request);
    String generateToken(User user);
}
