package com.example.web.service;

import com.example.web.entity.Cart;
import com.example.web.entity.CartItem;
import com.example.web.entity.Product;
import com.example.web.entity.User;
import com.example.web.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    @Autowired
    CartRepository cartRepository;
    @Autowired
    CartItemService cartItemService;

    @Autowired
    UserService userService;


    public void addToCart(Product product,int quantity) {
        User user = userService.getCurrentUser();
        Cart cart = user.getCart();
        if (cart == null) {
            cart = new Cart();
            user.setCart(cart);
        }
        // Kiểm tra xem sản phẩm đã có trong giỏ chưa
        CartItem existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId()==(product.getId()))
                .findFirst()
                .orElse(null);
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            CartItem newItem = new CartItem();
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            newItem.setCart(cart);
            cart.getItems().add(newItem);
        }
    }

    public void removeCartItem(Product product) {
        User user = userService.getCurrentUser();
        Cart cart = user.getCart();
        CartItem itemToRemove = cart.getItems().stream()
                .filter(item -> item.getProduct().getId()==(product.getId()))
                .findFirst()
                .orElse(null);
        if (itemToRemove != null) {
            cart.getItems().remove(itemToRemove); // Xóa khỏi danh sách
            // Nếu bạn dùng CascadeType.ALL thì chỉ cần gọi save cart
            cartItemService.deleteItem(itemToRemove); // hoặc xóa trực tiếp
        }
    }

    public Cart getCart() {
        User user = userService.getCurrentUser();
        Cart cart = user.getCart();
        if (cart == null) {
            cart = new Cart();
        }
        return cart;
    }
}
