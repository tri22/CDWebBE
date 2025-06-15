package com.example.web.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Entity(name = "stock_in")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Data
@Table(name = "stock_ins")
public class StockIn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private LocalDate createAt;
    private int quantity;
    private double price;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
