package com.example.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.web.entity.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

}