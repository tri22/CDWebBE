package com.example.web.controller;

import com.example.web.dto.request.OrderDetailRequest;
import com.example.web.dto.request.OrderRequest;
import com.example.web.dto.response.ApiResponse;
import com.example.web.dto.response.OrderResponse;
import com.example.web.entity.PaymentMethod;
import com.example.web.exception.AppException;
import com.example.web.exception.ErrorCode;
import com.example.web.repository.PaymentMethodRepository;
import com.example.web.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;

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

}
