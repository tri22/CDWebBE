package com.example.web.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Set;

@Entity(name = "order")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String note;
    @ManyToOne
    @JoinColumn(name = "payment_method_id")
    private PaymentMethod paymentMethod;
    private double totalPrice;
    private int totalQuantity;
    private Date orderDate;
    private double shippingFee;
    @OneToMany(mappedBy = "order")
    private Set<OrderDetail> details;
}
