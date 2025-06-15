package com.example.web.configuration;

import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiEndPoint {
    public static final String[] ADMIN_ENDPOINTS = {
            "/users/all",
            "/order/all",
            "/stock-in/*",

            "/vouchers/*",

            // dữ liệu thống kê
            "/order/week-best-selling/{date}",
            "/order/week-total/{date}",
            "/order/week-sale/{date}",
            "/order/week-cancelled/{date}",
            "/order/revenue/weekly/{date}",
            "/order/revenue/monthly/{date}",
            "/order/revenue/yearly/{date}",
    };

    public static final String[] PUBLIC_ENDPOINTS = {
            // user crud
            "/users",

            "/users/get/{userId}",
            "/users/update/{userId}",
            "/users/delete/{userId}",

            // login url
            "/auth/login",

            // Product url
            "/products",
            "/products/{id}",
    };
}
