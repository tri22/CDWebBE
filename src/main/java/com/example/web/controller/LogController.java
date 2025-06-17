package com.example.web.controller;

import com.example.web.dto.request.LogRequest;
import com.example.web.dto.response.ApiResponse;
import com.example.web.entity.Log;
import com.example.web.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/log")
@Slf4j
public class LogController {
    @Autowired
    private LogService logService;

    @GetMapping("/all")
    public ApiResponse<List<Log>> getAllLog() {
        ApiResponse<List<Log>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(logService.getAllLog());
        return apiResponse;
    }
}
