package com.example.web.service;

import com.example.web.dto.request.OrderRequest;
import com.example.web.dto.response.OrderResponse;
import com.example.web.entity.*;
import com.example.web.mapper.IOrderMapper;
import com.example.web.mapper.IUserMapper;
import com.example.web.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserService userService;

    @Autowired
    IOrderMapper orderMapper;

    public OrderResponse createOrder(OrderRequest request){
        User user = userService.getCurrentUser();
        Order order = orderMapper.toOrder(request);
        return orderMapper.toOrderResponse(orderRepository.save(order));
    }
    public void cancelOrder(long orderId){
        orderRepository.deleteById(orderId);
    }

}
