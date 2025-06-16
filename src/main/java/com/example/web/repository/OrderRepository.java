package com.example.web.repository;

import com.example.web.entity.Order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);

    List<Order> findAllByOrderDateBetween(LocalDate orderDate, LocalDate orderDate2);

    List<Order> findAllByOrderDateBetweenAndStatus(LocalDate startOfWeek, LocalDate endOfWeek, String cancelled);

    List<Order> getOrdersByOrderDate(LocalDate day);
}
