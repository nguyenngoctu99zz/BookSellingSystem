package com.example.BookSelling.controller;

import com.example.BookSelling.dto.request.UserCreationRequest;
import com.example.BookSelling.dto.response.ResponseData;
import com.example.BookSelling.dto.response.UserResponse;
import com.example.BookSelling.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;
    @PostMapping("")
    public UserResponse createUser(@RequestBody UserCreationRequest request){
        return userService.createUser(request);
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
}
