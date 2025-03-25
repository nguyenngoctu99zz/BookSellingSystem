package com.example.BookSelling.service.impl;

import com.example.BookSelling.common.UserRole;
import com.example.BookSelling.dto.request.UserCreationRequest;
import com.example.BookSelling.dto.request.UserUpdateRequest;
import com.example.BookSelling.dto.response.UserResponse;
import com.example.BookSelling.model.User;
import com.example.BookSelling.repository.UserRepository;
import com.example.BookSelling.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    UserRepository userRepository;

    @Override
    public UserResponse createUser(UserCreationRequest request) {
        // chua validate
        User user = User.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .createdAt(LocalDateTime.now())
//                .userImage(request.getUserImage())
                .userRole(UserRole.USER)
                .isActive(true)
                .build();
        userRepository.save(user);
        return UserResponse.builder()
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .createdAt(LocalDateTime.now())
                .userImage(user.getUserImage())
                .userRole(user.getUserRole())
                .isActive(true)
                .build();
    }

    @Override
    public UserResponse updateUser(UserUpdateRequest request) {
        return null;
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return List.of();
    }

    @Override
    public UserResponse getUserById(int userId) {
        return null;
    }

    @Override
    public void deleteUser(int userId) {

    }
}
