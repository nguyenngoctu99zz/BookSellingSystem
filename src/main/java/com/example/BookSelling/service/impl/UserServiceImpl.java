package com.example.BookSelling.service.impl;

import com.example.BookSelling.common.UserRole;
import com.example.BookSelling.dto.request.UserCreationRequest;
import com.example.BookSelling.dto.request.UserUpdateRequest;
import com.example.BookSelling.dto.response.UserResponse;
import com.example.BookSelling.exception.AppException;
import com.example.BookSelling.exception.ErrorCode;
import com.example.BookSelling.model.User;
import com.example.BookSelling.repository.InvalidatedTokenRepository;
import com.example.BookSelling.repository.UserRepository;
import com.example.BookSelling.service.ImageService;
import com.example.BookSelling.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    InvalidatedTokenRepository invalidatedTokenRepository;
    ImageService imageService;
    @Override
    public UserResponse createUser(UserCreationRequest request) {
        var role = (request.getUserRole() != null) ?
                request.getUserRole() : UserRole.USER;
//        String imageName = null;
//        if (request.getUserImage() != null && !request.getUserImage().isEmpty()) {
//            imageName = imageService.uploadImage(request.getUserImage(), null);
//        }
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .createdAt(LocalDateTime.now())
                .userImage(request.getUserImage())
                .userRole(role)
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
    public UserResponse updateUser(int userId, UserUpdateRequest request) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        String imageName = user.getUserImage();
        if (request.getUserImage() != null && !request.getUserImage().isEmpty()) {
            try {
                if (imageName != null) {
                    Path oldImagePath = Paths.get("uploads", imageName);
                    Files.deleteIfExists(oldImagePath);
                }

                imageName = imageService.uploadImage(request.getUserImage(), null);
            } catch (IOException e) {
                throw new RuntimeException("Failed to update image: " + e.getMessage());
            }
        }
        user.setUsername(request.getUsername());
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setUserImage(imageName);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setUserRole(request.getUserRole());
        user.setActive(request.isActive());
        userRepository.save(user);

        return UserResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .userImage(user.getUserImage())
                .createdAt(user.getCreatedAt())
                .userRole(user.getUserRole())
                .isActive(user.isActive())
                .build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
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
                        .isActive(user.isActive())
                        .build())
                .toList();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse getUserById(int userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return UserResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .createdAt(LocalDateTime.now())
                .userImage(user.getUserImage())
                .userRole(user.getUserRole())
                .isActive(user.isActive())
                .build();
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(int userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        invalidatedTokenRepository.deleteByUser(user);
        userRepository.delete(user);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public UserResponse changeUserStatus(int userId, boolean isActive) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        user.setActive(isActive);
        var updateUser = userRepository.save(user);
        return UserResponse.builder()
                .userId(updateUser.getUserId())
                .username(updateUser.getUsername())
                .fullName(updateUser.getFullName())
                .email(updateUser.getEmail())
                .phoneNumber(updateUser.getPhoneNumber())
                .createdAt(LocalDateTime.now())
                .userImage(user.getUserImage())
                .userRole(user.getUserRole())
                .isActive(updateUser.isActive())
                .build();
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
}
