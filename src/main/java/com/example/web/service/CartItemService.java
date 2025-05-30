package com.example.web.service;

import com.example.web.entity.CartItem;
import com.example.web.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartItemService {

    @Autowired
    CartItemRepository cartItemRepository;

    public void deleteItem(CartItem item) {
        cartItemRepository.delete(item);
    }
}
