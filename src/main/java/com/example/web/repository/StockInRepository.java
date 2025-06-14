package com.example.web.repository;

import com.example.web.entity.StockIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockInRepository extends JpaRepository<StockIn,Long> {
}
