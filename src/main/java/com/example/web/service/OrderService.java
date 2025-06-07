package com.example.web.service;

import com.example.web.dto.request.OrderRequest;
import com.example.web.dto.response.OrderResponse;
import com.example.web.entity.*;
import com.example.web.mapper.IOrderMapper;
import com.example.web.mapper.IUserMapper;
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
}




