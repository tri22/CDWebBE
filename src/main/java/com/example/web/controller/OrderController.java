package com.example.web.controller;



import com.example.web.configuration.JwtAuthenticationFilter;
import com.example.web.dto.request.LogRequest;


import com.example.web.dto.request.OrderDetailRequest;
import com.example.web.dto.request.OrderRequest;
import com.example.web.dto.response.ApiResponse;
import com.example.web.dto.response.OrderResponse;
import com.example.web.entity.PaymentMethod;
import com.example.web.exception.AppException;
import com.example.web.exception.ErrorCode;
import com.example.web.repository.PaymentMethodRepository;
import com.example.web.dto.response.ProductResponse;
import com.example.web.entity.Product;
import com.example.web.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private LogService logService;
    @Autowired
    JwtAuthenticationFilter jwtAuthenticationFilter;


    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @PostMapping("/create")
    public OrderResponse createOrder(@RequestBody OrderRequest orderRequest) {
        return orderService.createOrder(orderRequest);
    }

    @PostMapping("/from-cart")
    public ResponseEntity<OrderResponse> orderFromCart(@RequestBody OrderDetailRequest request) {
        PaymentMethod paymentMethod = paymentMethodRepository.findById(request.getPaymentMethodId())
                .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_METHOD_NOT_FOUND));
        return ResponseEntity
                .ok(orderService.createOrderFromCart(request.getNote(), paymentMethod, request.getShippingFee()));
    }

    @GetMapping("/{orderId}")
    public ApiResponse<OrderResponse> getOrderById(@PathVariable Long orderId) {
        ApiResponse<OrderResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(orderService.getOrderById(orderId));
        return apiResponse;
    }

    @GetMapping("/get-order/{userId}")
    public ApiResponse<List<OrderResponse>> getOrdersByUserId(@PathVariable Long userId) {
        ApiResponse<List<OrderResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(orderService.getOrdersByUserId(userId));
        return apiResponse;
    }

    @DeleteMapping("/delete/{orderId}")
    public void deleteOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
    }

    @GetMapping("/all")
    public ApiResponse<List<OrderResponse>> getAllOrder() {
        ApiResponse<List<OrderResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(orderService.getAllOrder());
        return apiResponse;
    }

    @PutMapping("/update/{orderId}")
    public ApiResponse<OrderResponse> updateOrder(@PathVariable Long orderId, @RequestBody OrderRequest request) {
        ApiResponse<OrderResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(orderService.updateOrder(orderId, request));
        return apiResponse;
    }

    @GetMapping("/week-best-selling/{date}")
    public ApiResponse<Product> getWeekBestSelling(@PathVariable LocalDate date) {
        ApiResponse<Product> apiResponse = new ApiResponse<>();
        apiResponse.setResult(orderService.getWeekBestSelling(date));
        return apiResponse;
    }

    @GetMapping("/week-total/{date}")
    public ApiResponse<Integer> getWeekTotal(@PathVariable LocalDate date) {
        ApiResponse<Integer> apiResponse = new ApiResponse<>();
        apiResponse.setResult(orderService.getWeekTotal(date));
        return apiResponse;
    }

    @GetMapping("/week-sale/{date}")
    public ApiResponse<Double> getWeekSale(@PathVariable LocalDate date) {
        ApiResponse<Double> apiResponse = new ApiResponse<>();
        apiResponse.setResult(orderService.getWeekSale(date));
        return apiResponse;
    }

    @GetMapping("/week-cancelled/{date}")
    public ApiResponse<Integer> getWeekCancelledOrder(@PathVariable LocalDate date) {
        ApiResponse<Integer> apiResponse = new ApiResponse<>();
        apiResponse.setResult(orderService.getWeekCancelledOrder(date));
        return apiResponse;
    }

    @GetMapping("/revenue/weekly/{date}")
    public ApiResponse<List<OrderService.SaleDataPoint>> getWeeklyRevenue(@PathVariable LocalDate date) {
        ApiResponse<List<OrderService.SaleDataPoint>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(orderService.getWeeklyRevenue(date));
        return apiResponse;
    }

    @GetMapping("/revenue/monthly/{date}")
    public ApiResponse<List<OrderService.SaleDataPoint>> getMonthlyRevenue(@PathVariable LocalDate date) {
        ApiResponse<List<OrderService.SaleDataPoint>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(orderService.getMonthlyRevenue(date));
        return apiResponse;
    }

    @GetMapping("/revenue/yearly/{date}")
    public ApiResponse<List<OrderService.SaleDataPoint>> getYearlyRevenue(@PathVariable LocalDate date) {
        ApiResponse<List<OrderService.SaleDataPoint>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(orderService.getYearlyRevenue(date));
        return apiResponse;
    }

}
