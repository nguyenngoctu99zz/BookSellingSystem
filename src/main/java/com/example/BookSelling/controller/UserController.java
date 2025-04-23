package com.example.BookSelling.controller;

import com.example.BookSelling.dto.request.UserCreationRequest;
import com.example.BookSelling.dto.request.UserUpdateRequest;
import com.example.BookSelling.dto.response.ResponseData;
import com.example.BookSelling.dto.response.UserResponse;
import com.example.BookSelling.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UserResponse createUser(@ModelAttribute UserCreationRequest request){
        return userService.createUser(request);
    }

    @PutMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseData<UserResponse> updateUser(@PathVariable("userId") int userId, @ModelAttribute UserUpdateRequest request){
        return ResponseData.<UserResponse>builder()
                .code(200)
                .message("User updated")
                .data(userService.updateUser(userId, request))
                .build();
    }
    @GetMapping("")
    public ResponseData<List<UserResponse>> getAllUsers(){
        return ResponseData.<List<UserResponse>>builder()
                .code(200)
                .data(userService.getAllUsers())
                .message("Successfully retrieved all users")
                .build();
    }

    @GetMapping("/{userId}")
    public ResponseData<UserResponse> getUserById(@PathVariable int userId){
        return ResponseData.<UserResponse>builder()
                .code(200)
                .data(userService.getUserById(userId))
                .message("Successfully retrieved user")
                .build();
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable int userId){
        userService.deleteUser(userId);
    }
    @DeleteMapping("disable/{userId}")
    public void softDeleteUser(@PathVariable int userId){
        userService.softDeleteUser(userId);
    }
}
