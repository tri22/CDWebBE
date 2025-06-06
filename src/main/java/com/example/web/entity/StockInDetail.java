package com.example.web.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "stock_in_detail")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Data
@Table(name = "stock_in_details")
public class StockInDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private long id;
    private int quantity;
    private double price;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    @ManyToOne
    @JoinColumn(name = "stock_in_id")
    private StockIn stock_in;
}
