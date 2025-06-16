package com.example.web.controller;

import com.example.web.configuration.JwtAuthenticationFilter;
import com.example.web.dto.request.LogRequest;
import com.example.web.dto.request.UserCreationReq;
import com.example.web.dto.request.UserUpdateReq;
import com.example.web.dto.response.ApiResponse;
import com.example.web.dto.response.UserResponse;
import com.example.web.entity.User;
import com.example.web.service.LogService;
import com.example.web.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    private LogService logService;

    JwtAuthenticationFilter jwtAuthenticationFilter;

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
    public ApiResponse<UserResponse> updateUser(@PathVariable("userId") long id,
                                                @RequestBody UserUpdateReq req,
                                                HttpServletRequest request) {
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        User currentUser = jwtAuthenticationFilter.extractUser(request);
        String ip = request.getRemoteAddr();

        try {
            UserResponse result = userService.updateUser(id, req);
            logService.addLog(LogRequest.builder()
                    .action("UPDATE_USER_SUCCESS")
                    .user(currentUser)
                    .ip(ip)
                    .level("INFO")
                    .dataIn(req)
                    .dataOut(result)
                    .date(new Date())
                    .resource("USER MANAGEMENT")
                    .build());
            apiResponse.setResult(result);
        } catch (Exception e) {
            logService.addLog(LogRequest.builder()
                    .action("UPDATE_USER_FAILED")
                    .user(currentUser)
                    .ip(ip)
                    .level("ERROR")
                    .dataIn(req)
                    .dataOut(e.getMessage())
                    .date(new Date())
                    .resource("USER MANAGEMENT")
                    .build());
            throw e;
        }

        return apiResponse;
    }


    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<ApiResponse<?>> deleteUser(@PathVariable("userId") long id,
                                                     HttpServletRequest request) {
        User currentUser = jwtAuthenticationFilter.extractUser(request);
        String ip = request.getRemoteAddr();

        try {
            userService.deleteUser(id);
            logService.addLog(LogRequest.builder()
                    .action("DELETE_USER_SUCCESS")
                    .user(currentUser)
                    .ip(ip)
                    .level("INFO")
                    .dataIn(id)
                    .dataOut("User deleted successfully")
                    .date(new Date())
                    .resource("USER MANAGEMENT")
                    .build());

            ApiResponse<?> response = new ApiResponse<>();
            response.setMessage("User deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logService.addLog(LogRequest.builder()
                    .action("DELETE_USER_FAILED")
                    .user(currentUser)
                    .ip(ip)
                    .level("ERROR")
                    .dataIn(id)
                    .dataOut(e.getMessage())
                    .date(new Date())
                    .resource("USER MANAGEMENT")
                    .build());

            throw e;
        }
    }

    @GetMapping("/me")
    public ApiResponse<User> getCurrentUser() {
        ApiResponse<User> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.getCurrentUser());
        return apiResponse;
    }
}
