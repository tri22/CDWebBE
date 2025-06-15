package com.example.web.service;

import com.example.web.dto.request.OrderRequest;
import com.example.web.dto.response.OrderResponse;
import com.example.web.entity.*;
import com.example.web.exception.AppException;
import com.example.web.exception.ErrorCode;
import com.example.web.mapper.IOrderMapper;
import com.example.web.mapper.IUserMapper;
import com.example.web.repository.CartRepository;
import com.example.web.repository.OrderRepository;
import com.example.web.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import com.example.web.entity.enums.OrderStatus; // Update to the correct package path for OrderStatus

@Service
public class OrderService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private IOrderMapper orderMapper;

    OrderService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public OrderResponse createOrder(OrderRequest request) {
        User user = userService.getCurrentUser();
        Order order = orderMapper.toOrder(request);
        return orderMapper.toOrderResponse(orderRepository.save(order));
    }

    @Transactional
    public OrderResponse createOrderFromCart(String note, PaymentMethod paymentMethod, double shippingFee) {
        User user = userService.getCurrentUser();
        Cart cart = user.getCart();

        if (cart == null || cart.getItems().isEmpty()) {
            throw new AppException(ErrorCode.CART_NOT_FOUND); // Hoặc tạo lỗi riêng như EMPTY_CART
        }

        Order order = new Order();
        order.setUser(user);
        order.setNote(note);
        order.setPaymentMethod(paymentMethod);
        order.setStatus(OrderStatus.NO_PAID); // Hoặc giá trị mặc định
        order.setOrderDate(LocalDate.now());
        order.setShippingFee(shippingFee);

        Set<OrderDetail> details = new HashSet<>();
        double totalPrice = 0;
        int totalQuantity = 0;

        for (CartItem item : cart.getItems()) {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setProduct(item.getProduct());
            detail.setQuantity(item.getQuantity());
            detail.setAddress(user.getAddress());
            detail.setPrice(item.getProduct().getPrice()); // bạn có thể truyền mỗi item 1 địa chỉ riêng nếu cần
            detail.setState("WAITING"); // trạng thái tuỳ logic hệ thống

            totalPrice += item.getProduct().getPrice() * item.getQuantity();
            totalQuantity += item.getQuantity();

            details.add(detail);
        }

        order.setDetails(details);
        order.setTotalQuantity(totalQuantity);
        order.setTotalPrice(totalPrice + shippingFee);

        Order savedOrder = orderRepository.save(order);

        // Xoá giỏ hàng sau khi tạo đơn hàng thành công
        cart.getItems().clear();
        cartRepository.save(cart); // Bạn cần inject `CartRepository` vào `OrderService`

        return orderMapper.toOrderResponse(savedOrder);
    }

    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        return orderMapper.toOrderResponse(order);
    }

    public List<OrderResponse> getOrdersByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream()
                .map(orderMapper::toOrderResponse)
                .collect(Collectors.toList());
    }

    public void cancelOrder(long orderId) {
        orderRepository.deleteById(orderId);
    }

    public List<OrderResponse> getAllOrder() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(order -> orderMapper.toOrderResponse(order)).toList();
    }

    @Transactional
    public void testMapping() {
        List<Order> orders = orderRepository.findAllById(Arrays.asList(3L, 4L));
        for (Order order : orders) {
            OrderResponse response = orderMapper.toOrderResponse(order);
            System.out.println(response);
        }
    }

    public OrderResponse updateOrder(Long orderId, OrderRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        if (request.getNote() != null) {
            order.setNote(request.getNote());
        }

        if (request.getUser() != null) {
            User updatedUser = request.getUser();
            User existingUser = order.getUser();

            if (updatedUser.getFullName() != null)
                existingUser.setFullName(updatedUser.getFullName());

            if (updatedUser.getBirthday() != null)
                existingUser.setBirthday(updatedUser.getBirthday());

            if (updatedUser.getAddress() != null)
                existingUser.setAddress(updatedUser.getAddress());

            if (updatedUser.getEmail() != null)
                existingUser.setEmail(updatedUser.getEmail());

            if (updatedUser.getPhone() != null)
                existingUser.setPhone(updatedUser.getPhone());

        }

        return orderMapper.toOrderResponse(orderRepository.save(order));
    }
}
