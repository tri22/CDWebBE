package com.example.web.controller;

import com.example.web.configuration.JwtAuthenticationFilter;
import com.example.web.dto.request.LogRequest;
import com.example.web.dto.request.OrderRequest;
import com.example.web.dto.response.ApiResponse;
import com.example.web.dto.response.OrderResponse;
import com.example.web.dto.response.ProductResponse;
import com.example.web.entity.Product;
import com.example.web.entity.User;
import com.example.web.service.LogService;
import com.example.web.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
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

    JwtAuthenticationFilter jwtAuthenticationFilter;

    @PostMapping("/create")
    public OrderResponse createOrder(@RequestBody OrderRequest orderRequest) {
        return orderService.createOrder(orderRequest);
    }

    @DeleteMapping("/delete/{orderId}")
    public void deleteOrder(@PathVariable Long orderId, @RequestBody HttpServletRequest request) {
        User user = jwtAuthenticationFilter.extractUser(request);
        try{
            orderService.cancelOrder(orderId);
            logService.addLog(LogRequest.builder()
                    .action("DELETE_ORDER_SUCCESS")
                    .user(user)
                    .ip(request.getRemoteAddr())
                    .level("INFO")
                    .dataIn(orderId)
                    .dataOut("Success")
                    .date(new Date())
                    .resource("ORDER MANAGEMENT")
                    .build());
        }catch (Exception e){
            logService.addLog(LogRequest.builder()
                    .action("DELETE_ORDER_FAIL")
                    .user(user)
                    .ip(request.getRemoteAddr())
                    .level("ERROR")
                    .dataIn(orderId)
                    .dataOut("Fail")
                    .date(new Date())
                    .resource("ORDER MANAGEMENT")
                    .build());
            throw e;
        }


    }

    @GetMapping("/all")
    public ApiResponse<List<OrderResponse>> getAllOrder() {
        ApiResponse<List<OrderResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(orderService.getAllOrder());
        return apiResponse;
    }

    @PutMapping("/update/{orderId}")
    public ApiResponse<OrderResponse> updateOrder(@PathVariable Long orderId,
                                                  @RequestBody OrderRequest request,
                                                  HttpServletRequest httpServletRequest) {
        User user = jwtAuthenticationFilter.extractUser(httpServletRequest);
        ApiResponse<OrderResponse> apiResponse = new ApiResponse<>();

        try {
            OrderResponse result = orderService.updateOrder(orderId, request);

            logService.addLog(LogRequest.builder()
                    .action("UPDATE_ORDER_SUCCESS")
                    .user(user)
                    .ip(httpServletRequest.getRemoteAddr())
                    .level("INFO")
                    .dataIn(request)
                    .dataOut(result)
                    .build());

            apiResponse.setResult(result);
        } catch (Exception e) {
            logService.addLog(LogRequest.builder()
                    .action("UPDATE_ORDER_FAILED")
                    .user(user)
                    .ip(httpServletRequest.getRemoteAddr())
                    .level("ERROR")
                    .dataIn(request)
                    .dataOut(e.getMessage())
                    .build());

            throw e; // ném lại để controller advice xử lý nếu có
        }
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
    public  ApiResponse<List<OrderService.SaleDataPoint>> getWeeklyRevenue(@PathVariable LocalDate date) {
        ApiResponse<List<OrderService.SaleDataPoint>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(orderService.getWeeklyRevenue(date));
        return apiResponse;
    }

    @GetMapping("/revenue/monthly/{date}")
    public  ApiResponse<List<OrderService.SaleDataPoint>> getMonthlyRevenue(@PathVariable LocalDate date) {
        ApiResponse<List<OrderService.SaleDataPoint>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(orderService.getMonthlyRevenue(date));
        return apiResponse;
    }

    @GetMapping("/revenue/yearly/{date}")
    public  ApiResponse<List<OrderService.SaleDataPoint>> getYearlyRevenue(@PathVariable LocalDate date) {
        ApiResponse<List<OrderService.SaleDataPoint>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(orderService.getYearlyRevenue(date));
        return apiResponse;
    }




}
