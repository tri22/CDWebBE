package com.example.web.repository;

import com.example.web.entity.Cart;
import com.example.web.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> getCartByUser_Id(long userId);
}
