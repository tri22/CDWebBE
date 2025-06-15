package com.example.web.controller;

import com.example.web.dto.request.CartItemRequest;
import com.example.web.dto.request.UserUpdateReq;
import com.example.web.dto.response.ApiResponse;
import com.example.web.dto.response.UserResponse;
import com.example.web.entity.Cart;
import com.example.web.entity.Product;
import com.example.web.entity.User;
import com.example.web.exception.AppException;
import com.example.web.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@Slf4j
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add-item")
    public ApiResponse<?> addToCart(@RequestBody CartItemRequest request) {
        try {
            cartService.addToCart(request.getProductId(), request.getQuantity());
            return ApiResponse.builder()
                    .message("Sản phẩm đã được thêm vào giỏ hàng thành công.")
                    .code(HttpStatus.OK.value())
                    .build();
        } catch (AppException e) {
            return ApiResponse.builder()
                    .message(e.getMessage())
                    .code(HttpStatus.BAD_REQUEST.value())
                    .build();
        } catch (Exception e) {
            return ApiResponse.builder()
                    .message("Đã xảy ra lỗi không xác định.")
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .build();
        }
    }

    @DeleteMapping("/clear")
    public ApiResponse<?> clearCart() {
        try {
            cartService.clearCartForCurrentUser();
            return ApiResponse.builder()
                    .message("Giỏ hàng đã được xoá thành công.")
                    .code(HttpStatus.OK.value())
                    .build();
        } catch (Exception e) {
            return ApiResponse.builder()
                    .message("Đã xảy ra lỗi khi xoá giỏ hàng.")
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .build();
        }
    }

    @DeleteMapping("/delete-item/{cartItemId}")
    public ApiResponse<?> deleteCartItem(@PathVariable long cartItemId) {
        try {
            cartService.removeCartItemById(cartItemId);
            return ApiResponse.builder()
                    .message("Sản phẩm đã xóa khỏi giỏ hàng thành công.")
                    .code(HttpStatus.OK.value())
                    .build();
        } catch (AppException e) {
            return ApiResponse.builder()
                    .message(e.getMessage())
                    .code(HttpStatus.BAD_REQUEST.value())
                    .build();
        } catch (Exception e) {
            return ApiResponse.builder()
                    .message("Đã xảy ra lỗi không xác định.")
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .build();
        }
    }

    @PutMapping("/update-item")
    public ApiResponse<?> updateItemQuantity(@RequestBody CartItemRequest request) {
        try {
            cartService.updateCartItemQuantity(request.getCartItemId(), request.getQuantity());
            return ApiResponse.builder()
                    .message("Cập nhập số lượng sản phẩm thành công.")
                    .code(HttpStatus.OK.value())
                    .build();
        } catch (AppException e) {
            return ApiResponse.builder()
                    .message(e.getMessage())
                    .code(HttpStatus.BAD_REQUEST.value())
                    .build();
        } catch (Exception e) {
            return ApiResponse.builder()
                    .message("Đã xảy ra lỗi không xác định.")
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .build();
        }
    }

    @GetMapping
    public Cart getCart() {
        return cartService.getCart();
    }

}
