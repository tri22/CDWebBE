package com.example.web.configuration;


import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiEndPoint {
    public static final String[] ADMIN_ENDPOINTS = {
            "/users/all",
    };

    public static final String[] PUBLIC_ENDPOINTS = {
            //user crud
            "/users",

            "/users/get/{userId}",
            "/users/update/{userId}",
            "/users/delete/{userId}",

            // login url
            "/auth/login"

    };
}
