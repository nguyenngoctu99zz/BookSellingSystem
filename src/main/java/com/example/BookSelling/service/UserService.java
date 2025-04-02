package com.example.BookSelling.service;

import com.example.BookSelling.dto.request.UserCreationRequest;
import com.example.BookSelling.dto.request.UserUpdateRequest;
import com.example.BookSelling.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse createUser(UserCreationRequest request);
    UserResponse updateUser(int userId, UserUpdateRequest request);
    List<UserResponse> getAllUsers();
    UserResponse getUserById(int userId);
    void deleteUser(int userId);
    void softDeleteUser(int userId);
    Integer getCurrentUserId();
}
