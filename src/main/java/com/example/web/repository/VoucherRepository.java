package com.example.web.repository;

import com.example.web.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    Voucher findDistinctByCode(String code);
}
