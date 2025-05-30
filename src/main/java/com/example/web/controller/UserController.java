package com.example.web.controller;

import com.example.web.dto.request.UserCreationReq;
import com.example.web.dto.request.UserUpdateReq;
import com.example.web.dto.response.ApiResponse;
import com.example.web.dto.response.UserResponse;
import com.example.web.entity.User;
import com.example.web.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping
    public ApiResponse<UserResponse> creatUser(@RequestBody @Valid UserCreationReq req) {
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.createUser(req));
        return apiResponse;
    }

    @GetMapping("/all")
    public ApiResponse<List<UserResponse>> getUsers() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        auth.getAuthorities().forEach(a -> log.info("Authority: {}", a.getAuthority()));
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getUsers())
                .build();

    }

    @GetMapping("/get/{userId}")
    public ApiResponse<UserResponse> getUserById(@PathVariable("userId") Long userId) {

        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.getUserById(userId));
        return apiResponse;

    }

    @PutMapping("/update/{userId}")
    public ApiResponse<UserResponse> updateUser(@PathVariable("userId") long id, @RequestBody UserUpdateReq req) {
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.updateUser(id, req));
        return apiResponse;
    }

    @DeleteMapping("/delete/{userId}")
    public void deleteUser(@PathVariable("userId") long id) {
        userService.deleteUser(id);
    }

    @GetMapping("/me")
    public ApiResponse<User> getCurrentUser() {
        ApiResponse<User> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.getCurrentUser());
        return apiResponse;
    }
}
