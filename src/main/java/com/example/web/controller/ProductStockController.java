package com.example.web.controller;

import com.example.web.dto.response.ApiResponse;
import com.example.web.entity.Product;
import com.example.web.entity.StockIn;
import com.example.web.service.StockInService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/stock-in")
@Slf4j
public class ProductStockController {
    @Autowired
    StockInService stockInService;

    @GetMapping("/all")
    public ApiResponse<List<StockIn>> getAllRecords(){
        ApiResponse<List<StockIn>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(stockInService.getAllRecord());
        return apiResponse;
    }

    @GetMapping("/remain")
    public ApiResponse<Map<Long,Product>> getRemain(){
        ApiResponse<Map<Long,Product>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(stockInService.getRemain());
        return apiResponse;
    }

}
