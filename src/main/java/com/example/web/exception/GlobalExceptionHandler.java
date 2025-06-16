package com.example.web.exception;

import com.example.web.dto.response.ApiResponse;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse> handleException(RuntimeException e) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(1001);
        apiResponse.setResult(e.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse> handleException(AppException e) {
        ErrorCode errorCode = e.getErrorCode();
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setResult(errorCode.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();

        e.getBindingResult().getFieldErrors().forEach(error -> {
            String enumKey = error.getDefaultMessage(); // e.g., "USERNAME_INVALID"
            ErrorCode errorCode = ErrorCode.KEY_INVALID;
            try {
                errorCode = ErrorCode.valueOf(enumKey);
            } catch (IllegalArgumentException ignored) {
                // Nếu defaultMessage không đúng tên enum, giữ KEY_INVALID
            }
            errors.put(error.getField(), errorCode.getMessage()); // e.g., username -> "Tên không hợp lệ"
        });

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(ErrorCode.KEY_INVALID.getCode());
        apiResponse.setResult(errors); // Trả toàn bộ lỗi field cho client

        return ResponseEntity.badRequest().body(apiResponse);
    }
}
