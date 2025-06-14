package com.example.web.service;

import com.example.web.dto.request.OrderRequest;
import com.example.web.dto.response.OrderResponse;
import com.example.web.entity.*;
import com.example.web.mapper.IOrderMapper;
import com.example.web.mapper.IUserMapper;
import com.example.web.repository.OrderDetailRepository;
import com.example.web.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private IOrderMapper orderMapper;

    public OrderResponse createOrder(OrderRequest request) {
        User user = userService.getCurrentUser();
        Order order = orderMapper.toOrder(request);
        return orderMapper.toOrderResponse(orderRepository.save(order));
    }

    public void cancelOrder(long orderId) {
        orderRepository.deleteById(orderId);
    }

    public List<OrderResponse> getAllOrder() {
        List<Order> orders = orderRepository.findAll();
        for (Order order : orders) {
            Set<OrderDetail> detail = orderDetailRepository.getAllByOrderId(order.getId());
            order.setDetails(detail);
        }
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
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new RuntimeException("Order not found with id: " + orderId));
        // Gán user nếu có
        if (request.getUser() != null) {
            order.setUser(request.getUser());
        }

        // Gán các thuộc tính nếu có
        if (request.getNote() != null) {
            order.setNote(request.getNote());
        }

        if (request.getPaymentMethod() != null) {
            order.setPaymentMethod(request.getPaymentMethod());
        }

        if (request.getTotalPrice() != 0) {
            order.setTotalPrice(request.getTotalPrice());
        }

        if (request.getTotalQuantity() != 0) {
            order.setTotalQuantity(request.getTotalQuantity());
        }

        if (request.getShippingFee() != 0) {
            order.setShippingFee(request.getShippingFee());
        }

        if (request.getOrderDate() != null) {
            order.setOrderDate(request.getOrderDate());
        }

        if (request.getStatus() != null) {
            order.setStatus(request.getStatus());
        }

        if (request.getDetails() != null && !request.getDetails().isEmpty()) {
            order.setDetails(request.getDetails());
        }
        return orderMapper.toOrderResponse(orderRepository.save(order));
    }
}




