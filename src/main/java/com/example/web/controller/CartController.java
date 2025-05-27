package com.example.web.controller;


import com.example.web.dto.request.UserUpdateReq;
import com.example.web.dto.response.ApiResponse;
import com.example.web.dto.response.UserResponse;
import com.example.web.entity.Product;
import com.example.web.entity.User;
import com.example.web.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@Slf4j
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add-item")
    public void addToCart(@RequestBody User user,@RequestBody Product product,@RequestBody int quantity) {
        cartService.addToCart(user,product,quantity);
    }

    @DeleteMapping("/delete-item")
    public void deleteCartItem(@RequestBody User user,@RequestBody Product product) {
        cartService.removeCartItem(user,product);
    }

}
