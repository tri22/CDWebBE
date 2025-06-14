package com.example.web.repository;

import com.example.web.entity.Order;
import com.example.web.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {


    Set<OrderDetail> getAllByOrderId(Long id);
}
