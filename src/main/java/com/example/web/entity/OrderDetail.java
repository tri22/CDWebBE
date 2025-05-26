package com.example.web.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "order_detail")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Data
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private long id;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private int quantity;
    private double price;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
