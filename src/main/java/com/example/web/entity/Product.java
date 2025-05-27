package com.example.web.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "product")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private long id;
    private String name;
    private double price;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    private String image;
    private String description;
    private int ratting;

    @ManyToOne
    @JoinColumn(name = "cart_item_id")
    private CartItem cartItem;
}
