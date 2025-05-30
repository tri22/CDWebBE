package com.example.web.entity;

import jakarta.persistence.*;
import lombok.*;

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
    // private Date createAt;
    // private int totalQuantity;
    // private double totalPrice;
    // @OneToMany(mappedBy = "stock_in")
    // private Set<StockInDetail> details
}
