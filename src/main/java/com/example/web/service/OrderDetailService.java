package com.example.web.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.web.repository.CartItemRepository;

public class OrderDetailService {
    @Autowired
    CartItemRepository cartItemRepository;
}
