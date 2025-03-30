package com.example.BookSelling.service.impl;

import com.example.BookSelling.common.UserRole;
import com.example.BookSelling.dto.request.UserCreationRequest;
import com.example.BookSelling.dto.request.UserUpdateRequest;
import com.example.BookSelling.dto.response.UserResponse;
import com.example.BookSelling.exception.AppException;
import com.example.BookSelling.exception.ErrorCode;
import com.example.BookSelling.model.User;
import com.example.BookSelling.repository.UserRepository;
import com.example.BookSelling.service.UserService;
import com.example.BookSelling.utils.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @Override
    public UserResponse createUser(UserCreationRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .createdAt(LocalDateTime.now())
//                .userImage(request.getUserImage())
                .userRole(UserRole.USER)
                .isActive(true)
                .build();
        if(userRepository.existsByUsername(user.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
        if(userRepository.existsByPhoneNumber(user.getPhoneNumber())) {
            throw new AppException(ErrorCode.PHONE_NO_EXISTED);
        }
        userRepository.save(user);
        return UserResponse.builder()
                .userId(user.getUserId())
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
        return userRepository.findAll()
                .stream()
                .map(user -> UserResponse.builder()
                        .userId(user.getUserId())
                        .username(user.getUsername())
                        .fullName(user.getFullName())
                        .email(user.getEmail())
                        .phoneNumber(user.getPhoneNumber())
                        .createdAt(LocalDateTime.now())
                        .userImage(user.getUserImage())
                        .userRole(user.getUserRole())
                        .isActive(true)
                        .build())
                .toList();
    }

    @Override
    public UserResponse getUserById(int userId) {
        return null;
    }

    @Override
    public void deleteUser(int userId) {

    }

    @Override
    public Integer getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return user.getUserId();
    }


//    @Override
//    public Integer getCurrentUserId() {
//        return Integer.valueOf(SecurityUtils.getCurrentLogin()
//                .orElseThrow(() -> new RuntimeException("User not authenticated")));
//    }
}
