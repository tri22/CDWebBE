package com.example.web.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity(name = "voucher")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Data
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private long id;
    private String code;
    private String description;
    private int quantity;
}
