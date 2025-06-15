package com.example.web.service;

import com.example.web.dto.response.ProductResponse;
import com.example.web.entity.Cart;
import com.example.web.entity.CartItem;
import com.example.web.entity.Product;
import com.example.web.entity.User;
import com.example.web.exception.AppException;
import com.example.web.exception.ErrorCode;
import com.example.web.repository.CartItemRepository;
import com.example.web.repository.CartRepository;
import com.example.web.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

@Service
@Transactional
public class CartService {

    @Autowired
    CartRepository cartRepository;
    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    UserService userService;

    @Autowired
    ProductRepository productRepository;

    public void addToCart(long productId, int quantity) {
        User user = userService.getCurrentUser();
        Cart cart = cartRepository.findByUser_Id(user.getId())
                .orElseGet(() -> {
                    Cart c = new Cart();
                    c.setUser(user);
                    c.setItems(new HashSet<>());
                    return c;
                });
        // Kiểm tra xem sản phẩm đã có trong giỏ chưa
        CartItem existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId() == (productId))
                .findFirst()
                .orElse(null);
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXIST));
            CartItem newItem = new CartItem();
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            newItem.setCart(cart);
            cart.getItems().add(newItem);
        }
        cartRepository.save(cart);
    }

    public void clearCartForCurrentUser() {
        User user = userService.getCurrentUser();
        Cart cart = cartRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));
        cart.getItems().clear(); // xoá hết items
        cartRepository.save(cart);
    }

    public void removeCartItemById(long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    public void updateCartItemQuantity(long cartItemId, int quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION));
        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);
    }

    public Cart getCart() {
        User user = userService.getCurrentUser();
        Cart cart = user.getCart();
        if (cart == null) {
            System.out.println("creating new cart");
            cart = new Cart();
            cart.setUser(user);
            cart.setItems(new HashSet<CartItem>());
            cartRepository.save(cart);
        }
        return cart;
    }
}
