package com.example.BookSelling.controller;

import com.example.BookSelling.dto.request.UserCreationRequest;
import com.example.BookSelling.dto.response.UserResponse;
import com.example.BookSelling.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
